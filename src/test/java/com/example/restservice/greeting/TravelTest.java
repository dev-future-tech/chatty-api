package com.example.restservice.greeting;

import com.example.restservice.tools.CustomerBookingTools;
import com.example.restservice.tools.CustomerTool;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class TravelTest {

    @Autowired
    ChatModel chatModel;

    @Autowired
    ChatClient.Builder chatClientBuilder;

    @Autowired
    private CustomerBookingTools customerBookingTools;

    @Autowired
    private CustomerTool customerTool;

    @Test
    public void allSeasonalTravelPlans() {
        String promptText = """
                Show me all travel plans for customerId {customerId}
                and break them into the different seasons of the year.
                """;
        PromptTemplate promptTemplate = PromptTemplate
                .builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('{').endDelimiterToken('}').build())
                .template(promptText)
                .build();
        Message travelMessage = promptTemplate.createMessage(Map.of("customerId", 1));

        String systemText = """
                You are a travel agent tasked with helping the customer.
                Dates are always represented as YYYY-MM-DD.
                You will display the customer name, destination, start_date and end_date only.
                """;
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage();


        Prompt prompt = new Prompt(List.of(travelMessage, systemMessage));
        ChatClient chatClient = chatClientBuilder.defaultAdvisors(new SimpleLoggerAdvisor()).build();

        String response = chatClient
                .prompt(prompt)
                .tools(customerBookingTools)
                .call()
                .content();

        System.out.println(response);


    }

    @Test
    public void testTravelRecommendations() {
        String promptText = """
                Show me the travel plans for customerId {customerId}
                that happen during {season} time in the United States of America
                """;

        PromptTemplate promptTemplate = PromptTemplate
                .builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('{').endDelimiterToken('}').build())
                .template(promptText)
                .build();

        Message travelMessage = promptTemplate.createMessage(Map.of("customerId", 1, "season", "summer"));

        Prompt prompt = new Prompt(travelMessage);
        ChatClient chatClient = chatClientBuilder.defaultAdvisors(new SimpleLoggerAdvisor()).build();

        String response = chatClient
                .prompt(prompt)
                .tools(customerBookingTools)
                .call()
                .content();

        System.out.println(response);
    }
}

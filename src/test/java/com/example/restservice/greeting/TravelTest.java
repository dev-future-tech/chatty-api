package com.example.restservice.greeting;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.restservice.tools.CustomerBookingTools;
import com.example.restservice.tools.CustomerTool;
import com.example.restservice.tools.DestinationTool;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Map;

@Disabled
@Profile(value={"ML"})
@SpringBootTest()
public class TravelTest {

    @Autowired
    PromptChatMemoryAdvisor jdbcAdvisor;


    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:14")
            .withUsername("booking_controller")
            .withPassword("letmein")
            .withDatabaseName("bookings_db");

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @MockitoBean
    private SocketIOServer socketIOServer;

    @Autowired
    ChatModel chatModel;

    @Autowired
    ChatClient.Builder chatClientBuilder;

    @Autowired
    private CustomerBookingTools customerBookingTools;

    @Autowired
    private DestinationTool destinationTool;

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


    @Test
    public void createFlightDatabaseInserts() throws Exception {
        String promptText = """
                I need to insert data into a PostgreSQL database table with the following schema:
                
                ```sql
                create table public.flights
                (
                    id             bigint generated by default as identity
                        primary key,
                    origin_id      bigint
                        constraint fk_origin_destination
                            references public.destinations,
                    destination_id bigint
                        constraint fk_flight_destination
                            references public.destinations,
                    airport_id     bigint
                        constraint fk_flight_airport
                            references public.airports,
                    departure_date date,
                    departure_time time,
                    arrival_date   date,
                    arrival_time   time
                );
                ```
                The flight.origin_id and flight.destination_id refer to the destination_id on the public.destinations table.
                The flight.airport_id refers to the airports.airport_id.
                
                The departure_time and arrival_time should be valid 24 hour times.
                
                I want to generate 10 insert statements to insert new flight records.
                
                The output should be in SQL.
                
                """;
        PromptTemplate promptTemplate = PromptTemplate
                .builder()
                .template(promptText)
                .build();

        ChatClient chatClient = chatClientBuilder
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), jdbcAdvisor))
                .build();

        String response = chatClient
                .prompt(promptTemplate.create())
                .tools(destinationTool)
                .call()
                .content();

        System.out.println(response);
    }

}

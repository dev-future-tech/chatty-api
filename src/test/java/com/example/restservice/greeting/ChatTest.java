package com.example.restservice.greeting;

import com.example.restservice.tools.PoemTools;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;

import java.util.List;

@SpringBootTest
public class ChatTest {

    @Autowired
    OpenAiApi openAiApi;

    @Autowired
    ChatClient.Builder chatClientBuilder;

    @Test
    public void testChat() {
        String content = """
                Write a poem made up of 5 lines
                Your response should be in JSON format.
                Do not include any explanations, only provide a RFC8259 compliant JSON response following this format without deviation.
                Do not include markdown code blocks in your response.
                Remove the ```json markdown from the output.
                Here is the JSON Schema instance your output must adhere to:
                ```
                { "$schema" : "https://json-schema.org/draft/2020-12/schema" }
                ```
                """;
        OpenAiApi.ChatCompletionMessage message = new OpenAiApi.ChatCompletionMessage(
                content,
                OpenAiApi.ChatCompletionMessage.Role.USER,
                "anthony",
                "abn",
                null,
                null,
                null, null
        );
        OpenAiApi.ChatCompletionRequest request = new OpenAiApi.ChatCompletionRequest(List.of(message), "meta-llama-3.1-8b-instruct", 0.7);

        ResponseEntity<OpenAiApi.ChatCompletion> completion = openAiApi.chatCompletionEntity(request);
        System.out.println(completion.toString());
    }


    @Test
    public void chatTest2() {
        PoemTools poemTools = new PoemTools();

        ChatClient chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();

        PromptTemplate pt = new PromptTemplate("""
        Name me 3 composers
        """);

        String result = chatClient.prompt(pt.create())
                .tools(poemTools)
                .call()
                .content();

        System.out.println(result);
    }
}

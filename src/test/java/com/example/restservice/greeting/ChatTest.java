package com.example.restservice.greeting;

import com.example.restservice.tools.PoemTools;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.*;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class ChatTest {

    @Autowired
    OpenAiApi openAiApi;

    @Autowired
    ChatModel chatModel;

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

    @Test
    public void testPromptTemplate() {
        PromptTemplate template = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .template("Give me 3 songs that were sung by <artist>.")
                .build();

        String prompt = template.render(Map.of("artist", "Michael Jackson"));

        System.out.println(prompt);

        PromptTemplateMessageActions messageTemplate = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .template("List me the best IDE for writing <language> applications.")
                .build();


        Message message = messageTemplate.createMessage(Map.of("language", "Java"));

        System.out.println(message.getMessageType());
        System.out.println(message.getText());

        PromptTemplateActions templateActions = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('{').endDelimiterToken('}').build())
                .template("Show me 5 people that can write programs in {language}")
                .build();

        Prompt promptPrompt = templateActions.create(Map.of("language", "Java"));
        System.out.println(promptPrompt.getContents());
    }

    record MovieOutlines(String genre, List<String> movies) { }

    @Test
    public void testUserMessage() {
        BeanOutputConverter<MovieOutlines> beanOutputConverter = new BeanOutputConverter<>(MovieOutlines.class);
        String format = beanOutputConverter.getFormat();
        System.out.println(format);

        String userText = """
                Tell me the titles of famous science fiction tv shows from the 70s.
                Write at least a sentence giving an overview of the show.
                {format}
                """;

        PromptTemplate promptTemplate = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('{').endDelimiterToken('}').build())
                .template(userText)
                .build();

        Message userMessage = promptTemplate.createMessage(Map.of("format", format));

        String systemText = """
                You are an desk clerk with a premium airline company.
                Your name is {name}
                You are also {gender}
                You should reply to the user's requests with your name and also in the style of {voice}.
                """;

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", "Roberto", "gender", "male", "voice", "masculine"));
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        Generation response = chatModel.call(prompt).getResult();


        System.out.println(response.getOutput().getText());

        MovieOutlines outlines = beanOutputConverter.convert(response.getOutput().getText());
        System.out.println(outlines.genre);
        System.out.println(outlines.movies);

    }

    @Test
    public void testOutputConvertorMap() {
        MapOutputConverter mapOutputConverter = new MapOutputConverter();

        String format = mapOutputConverter.getFormat();

        String template = """
                Provide me a List of {subject}
                {format}
                """;

        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template(template)
                .build();
        Prompt prompt = promptTemplate.create(Map.of("subject", "an array of domestic pets under the key name 'pets'",
                "format", format));

        Generation response = chatModel.call(prompt).getResult();

        Map<String, Object> result = mapOutputConverter.convert(response.getOutput().getText());
        System.out.println(result);
    }

    @Test
    public void listingSportsTest() {
        ListOutputConverter listOutputConverter = new ListOutputConverter(new DefaultConversionService());

        String format = listOutputConverter.getFormat();
        String template = """
                List five {subject}
                {format}
                """;

        Prompt prompt = PromptTemplate.builder()
                .template(template)
                .build().create(Map.of("subject", "sports", "format", format));

        Generation response = chatModel.call(prompt).getResult();
        List<String> list = listOutputConverter.convert(response.getOutput().getText());

        System.out.println(list);
    }


}

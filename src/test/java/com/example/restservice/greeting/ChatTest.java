package com.example.restservice.greeting;

import com.example.restservice.tools.CodeRetrievalTool;
import com.example.restservice.tools.PoemTools;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.*;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.ollama.OllamaContainer;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles(value="ML")
public class ChatTest {

    @Container
    static OllamaContainer ollamaContainer = new OllamaContainer("ollama/ollama:0.1.32");

    @Autowired
    OpenAiApi openAiApi;

    @Autowired
    ChatModel chatModel;

    @Autowired
    PromptChatMemoryAdvisor jdbcAdvisor;

    @Autowired
    ChatClient.Builder chatClientBuilder;

    @Autowired
    private DataSource dataSource;

    @Test
    public void testChat() {
        String content = """
                Write a poem made up of 5 lines
                Your response should be in JSON format.
                Do not include any explanations, only provide a RFC8259 compliant JSON response following this format without deviation.
                Do not include markdown code blocks in your response.
                Remove the json markdown from the output.
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
        OpenAiApi.ChatCompletionRequest request = new OpenAiApi.ChatCompletionRequest(List.of(message), "llama3.2", 0.7);

        ResponseEntity<OpenAiApi.ChatCompletion> completion = openAiApi.chatCompletionEntity(request);
        System.out.println(completion.toString());
    }


    @Test
    public void chatTest2() {
        PoemTools poemTools = new PoemTools();

        ChatClient chatClient = chatClientBuilder
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), jdbcAdvisor))
                .build();

        PromptTemplate template = PromptTemplate.builder()
                .template("Name 3 composers")
                .build();

        String result = chatClient.prompt(template.create())
                .tools(poemTools)
                .advisors(List.of(new SimpleLoggerAdvisor(), jdbcAdvisor))
                .call()
                .content();

        System.out.printf("Result: %s%n", result);

        PromptTemplate qTemplate = PromptTemplate.builder()
                .template("Which of these composers were alive during the classical period of music?")
                .build();

        String eraResult = chatClient.prompt(qTemplate.create())
                .advisors(List.of(new SimpleLoggerAdvisor(), jdbcAdvisor))
                .call()
                .content();

        System.out.printf("Result: %s%n", eraResult);

    }

    @Test
    public void testPromptTemplate() {
        PromptTemplate template = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .template("Give me 3 songs that were sung by <artist>.")
                .build();

        String prompt = template.render(Map.of("artist", "Michael Jackson"));

        System.out.printf("Prompt: %s%n", prompt);

        PromptTemplateMessageActions messageTemplate = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .template("List me the best IDE for writing <language> applications.")
                .build();


        Message message = messageTemplate.createMessage(Map.of("language", "Java"));

        System.out.printf("Message Type: %s%n", message.getMessageType());
        System.out.printf("Message text: %s%n", message.getText());

        PromptTemplateActions templateActions = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('{').endDelimiterToken('}').build())
                .template("Show me 5 people that can write programs in {language}")
                .build();

        Prompt promptPrompt = templateActions.create(Map.of("language", "Java"));
        System.out.printf("Response: %s%n", promptPrompt.getContents());
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

    @Test
    public void testCodeAnalysis() {
        BeanOutputConverter<String> beanOutputConverter = new BeanOutputConverter<>(String.class);
        CodeRetrievalTool codeTool = new CodeRetrievalTool();

        String format = beanOutputConverter.getFormat();

        ChatClient chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();

        PromptTemplate pt = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('{').endDelimiterToken('}').build())
                .template("""
                Analyze this code and give me a non-technical description of what it does.
                {format}
            """)
                .build();

        String result = chatClient.prompt(pt.create(Map.of("format", format)))
                .tools(codeTool)
                .call()
                .content();

        System.out.println(result);

    }

}

package com.example.restservice;

import org.springframework.ai.model.NoopApiKey;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class PromptConfiguration {

    @Bean
    OpenAiApi openAiApi() {
        return OpenAiApi.builder()
                .apiKey(new NoopApiKey())
                .baseUrl("http://localhost:8090")
                .restClientBuilder(RestClient.builder()
                        // Force HTTP/1.1 for both streaming and non-streaming
                        .requestFactory(new JdkClientHttpRequestFactory(HttpClient.newBuilder()
                                .version(HttpClient.Version.HTTP_1_1)
                                .connectTimeout(Duration.ofSeconds(30))
                                .build())))
                .build();
    }

}

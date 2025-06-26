package com.example.restservice;

import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepositoryDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class RAGConfiguration {

    /**
     * Writes the Chat Hsitory to the database
     * The Advisor ensures the context can be maintained throughout the conversation.
     * @param dataSource
     * @return
     */
    @Bean
    PromptChatMemoryAdvisor promptChatMemoryAdvisor(DataSource dataSource) {
        var jdbc = JdbcChatMemoryRepository.builder()
                .dataSource(dataSource)
                .dialect(JdbcChatMemoryRepositoryDialect.from(dataSource))
                .build();

        ChatMemory memory = MessageWindowChatMemory.builder().chatMemoryRepository(jdbc).build();
        return PromptChatMemoryAdvisor.builder(memory)
                .build();
    }


}

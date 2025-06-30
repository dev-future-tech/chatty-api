package com.example.restservice.bookings;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.restservice.SharedMocks;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@SharedMocks
class DestinationsControllerTest {
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


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SocketIOServer socketIOServer;

    @Autowired
    private DestinationRepository destinationRepository;

    @Test
    @WithMockUser(username="peter.garrison@gmail.com", roles = {"USER"})
    void findAll() throws Exception {

        List<Destination> results = new ArrayList<>();
        var dest1 = new Destination();
        dest1.setCity("Sydney, Australia");
        dest1.setDestinationId(54);
        dest1.setDescription("Located in Australia - nice but expensive");
        results.add(dest1);

        Mockito.when(destinationRepository.findAll()).thenReturn(results);

        this.mockMvc.perform(get("/destinations")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].destination_id").value(54))
                .andExpect(jsonPath("$[0].city").value("Sydney, Australia"))
                .andExpect(jsonPath("$[0].image_url").isEmpty());

    }

    @Test
    @WithMockUser(username="peter.garrison@gmail.com", roles = {"USER"})
    void findOne() throws Exception {
        var dest1 = new Destination();
        dest1.setCity("Sydney, Australia");
        dest1.setDestinationId(54);
        dest1.setDescription("Located in Australia - nice but expensive");

        Mockito.when(destinationRepository.findByDestinationId(Mockito.anyLong())).thenReturn(dest1);

        this.mockMvc.perform(get("/destinations/54"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Sydney, Australia"))
                .andExpect(jsonPath("$.image_url").isEmpty())
                .andExpect(jsonPath("$.destination_id").value(54));
    }
}
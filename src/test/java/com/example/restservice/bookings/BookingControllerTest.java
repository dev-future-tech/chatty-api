package com.example.restservice.bookings;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.restservice.SharedMocks;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@SharedMocks
class BookingControllerTest {
    private final Logger log = LoggerFactory.getLogger(BookingControllerTest.class);

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
    BookingRepository bookingRepository;

    @Autowired
    DestinationRepository destinationRepository;

    @Autowired
    CustomerRepository customerRepository;


    @Test
    @WithMockUser(username="peter.garrison@gmail.com", roles = {"USER"})
    public void testGetCustomerBookingsForCustomer() throws Exception {
        var customer = new Customer();
        customer.setCustomerId(3);
        customer.setCustomerName("Peter Garrison");
        customer.setEmail("peter.garrison@gmail.com");

        var booking1 = new CustomerBooking();
        booking1.setBookingId(1);
        booking1.setStartDate(LocalDate.parse("2025-12-07"));
        booking1.setEndDate(LocalDate.parse("2025-12-15"));
        booking1.setDestination("Auckland, New Zealand");
        booking1.setCustomer(customer);

        List<CustomerBooking> results = new ArrayList<>();
        results.add(booking1);

        Mockito.when(bookingRepository.findCustomerBookingByCustomerId(Mockito.anyInt())).thenReturn(results);

        this.mockMvc.perform(get("/booking/3"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="peter.garrison@gmail.com", roles = {"USER", "TRAVELLER"})
    public void testCreateBooking() throws Exception {
        var customer = new Customer();
        customer.setCustomerId(3);
        customer.setCustomerName("Peter Garrison");
        customer.setEmail("peter.garrison@gmail.com");

        var destination = new Destination();
        destination.setDestinationId(3);
        destination.setCity("Auckland, New Zealand");
        destination.setDescription("Home of the long white cloud");

        var booking1 = new CustomerBooking();
        booking1.setBookingId(1);
        booking1.setStartDate(LocalDate.parse("2025-12-07"));
        booking1.setEndDate(LocalDate.parse("2025-12-15"));
        booking1.setDestination("Auckland, New Zealand");
        booking1.setCustomer(customer);

        Mockito.when(customerRepository.findCustomerByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.when(destinationRepository.findByDestinationId(Mockito.anyLong())).thenReturn(destination);
        Mockito.when(bookingRepository.save(Mockito.any(CustomerBooking.class))).thenReturn(booking1);

        var request = new BookingController.BookingRequest("peter.garrison@gmail.com", "2025-12-07", "2025-12-15", 3);

        StringWriter stringWriter = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(stringWriter, request);

        String body = stringWriter.toString();
        log.debug("Body: {}", body);

        this.mockMvc.perform(post("/booking")
                        .contentType("application/json")
                        .content(body)
                        .with(jwt())
                )
                .andDo(print())
                .andExpect(status().isCreated());

    }


}
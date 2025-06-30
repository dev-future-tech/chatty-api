package com.example.restservice;

import com.example.restservice.bookings.BookingRepository;
import com.example.restservice.bookings.CustomerRepository;
import com.example.restservice.bookings.DestinationRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@MockitoBean(types={DestinationRepository.class, BookingRepository.class, CustomerRepository.class})
public @interface SharedMocks {
}

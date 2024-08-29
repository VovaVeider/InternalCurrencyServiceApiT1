package org.vladimir.t1.currency.service.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class InternalCurrencyServiceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(InternalCurrencyServiceApiApplication.class, args);
    }

}

package com.study.shoestrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Optional;
import java.util.UUID;

@EnableScheduling
@SpringBootApplication
public class ShoestradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoestradeApplication.class, args);
    }

}

package com.example.rna.exam.ia.config;

import com.example.rna.exam.ia.RNA;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public RNA readerText() {
        return new RNA();
    }
}

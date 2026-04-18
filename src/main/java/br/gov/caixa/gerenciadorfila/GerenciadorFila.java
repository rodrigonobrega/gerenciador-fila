package br.gov.caixa.gerenciadorfila;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GerenciadorFila {
    public static void main(String[] args) {
        SpringApplication.run(GerenciadorFila.class, args);
    }
}


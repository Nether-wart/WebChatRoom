package io.github.nether_wart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Main {
    static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

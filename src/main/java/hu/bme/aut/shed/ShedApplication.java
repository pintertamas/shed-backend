package hu.bme.aut.shed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShedApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShedApplication.class, args);
    }
}

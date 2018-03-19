package pl.galuszka.mapchecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VwMapUpdatesCheckerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VwMapUpdatesCheckerApplication.class, args);
    }
}

package pl.utp.scrumban;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableScheduling
@EnableSwagger2
public class ScrumbanApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrumbanApplication.class, args);
    }

}

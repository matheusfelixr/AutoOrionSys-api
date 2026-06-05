ackage com.autoorion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class autoorionBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(autoorionBackendApplication.class, args);
    }
}

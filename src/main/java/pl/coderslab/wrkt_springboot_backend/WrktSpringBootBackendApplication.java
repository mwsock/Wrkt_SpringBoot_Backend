package pl.coderslab.wrkt_springboot_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;

@SpringBootApplication
public class WrktSpringBootBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WrktSpringBootBackendApplication.class, args);
	}

}

package umk.mat.jakuburb.AtiperaTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AtiperaTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtiperaTaskApplication.class, args);
	}

}

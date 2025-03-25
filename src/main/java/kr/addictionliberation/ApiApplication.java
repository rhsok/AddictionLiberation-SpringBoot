package kr.addictionliberation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv; // dotenv-java
import org.springframework.boot.SpringApplication;


@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		
		 // .env 파일 로드
		Dotenv dotenv = Dotenv.load();
	    dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
	

		 SpringApplication.run(ApiApplication.class, args);
	}

}
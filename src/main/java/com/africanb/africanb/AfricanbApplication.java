package com.africanb.africanb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootApplication
@EnableCaching
public class AfricanbApplication  /*implements CommandLineRunner*/ {
	public static void main(String[] args) {
		SpringApplication.run(AfricanbApplication.class, args);
	}
   /*
	@Override
	public void run(String... args) throws Exception {
			get("https://api.countrylayer.com/v2/all");
	}

	private void get(String uri) throws Exception {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))
				.build();
		HttpResponse<String> response =
				client.send(request, HttpResponse.BodyHandlers.ofString());

		System.out.println(response.body());
	}*/

}

package br.com.EasyTable.API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.EasyTable")
@EnableMongoRepositories(basePackages = "br.com.EasyTable.Repositories")
@EntityScan(basePackages = "br.com.EasyTable.Borders.Entities")
public class EasyTableApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyTableApplication.class, args);
	}
}
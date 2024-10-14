package com.adi.adijwtprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = {"com.adi.adijwtprovider.*"})
@PropertySource( "classpath:application-secret.properties" )
public class AdijwtproviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdijwtproviderApplication.class, args);
	}

}

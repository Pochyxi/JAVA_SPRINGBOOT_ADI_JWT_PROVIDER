package com.adi.adijwtprovider;

import com.adi.adijwtprovider.security.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = {"com.adi.adijwtprovider.*"})
@PropertySource( "classpath:application-secret.properties" )
@EnableConfigurationProperties(JwtConfig.class)
public class AdijwtproviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdijwtproviderApplication.class, args);
	}

}

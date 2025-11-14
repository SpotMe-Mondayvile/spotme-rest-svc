package com.mts.spotmerest;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
// import springfox.documentation.oas.annotations.EnableOpenApi;
// import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EntityScan
@SpringBootApplication
@SecurityScheme(
		name = "JWT",
		scheme = "bearer",
		bearerFormat = "JWT",
		type = SecuritySchemeType.HTTP,
		in = SecuritySchemeIn.HEADER
)
public class SpotmeRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpotmeRestApplication.class, args);
	}

}

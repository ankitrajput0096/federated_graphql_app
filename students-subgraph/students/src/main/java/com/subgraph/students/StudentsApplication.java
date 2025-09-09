package com.subgraph.students;

import com.subgraph.students.config.CustomGpaScalar;
import com.subgraph.students.config.RestTemplateConfig;
import com.subgraph.students.config.TypeResolverConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.data.federation.FederationSchemaFactory;

@SpringBootApplication
@Import({CustomGpaScalar.class, TypeResolverConfig.class, RestTemplateConfig.class})
public class StudentsApplication {

	/**
	 * Gradle commands to manage this application.
	 * ./gradlew tasks → lists available tasks
	 * ./gradlew clean build → compiles + tests + packages
	 * ./gradlew bootRun → runs Spring Boot app
	 * ./gradlew bootJar → creates executable JAR
	 * ./gradlew clean → removes build artifacts
	 */

	public static void main(String[] args) {
		SpringApplication.run(StudentsApplication.class, args);
	}

	@Bean
	public GraphQlSourceBuilderCustomizer customizer(FederationSchemaFactory factory) {
		return builder -> builder.schemaFactory(factory::createGraphQLSchema);
	}

	@Bean
	FederationSchemaFactory federationSchemaFactory() {
		return new FederationSchemaFactory();
	}
}

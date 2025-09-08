package com.subgraph.university;

import com.subgraph.university.config.CustomInstrumentation;
import com.subgraph.university.config.ErrorHandler;
import com.subgraph.university.config.MetadataDirective;
import com.subgraph.university.model.Campus;
import com.subgraph.university.model.CustomDateScalar;
import com.subgraph.university.model.University;
import graphql.execution.instrumentation.Instrumentation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.data.federation.FederationSchemaFactory;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@SpringBootApplication
public class UniversityApplication {

	/**
	 * Gradle commands to manage this application.
	 * ./gradlew tasks → lists available tasks
	 * ./gradlew clean build → compiles + tests + packages
	 * ./gradlew bootRun → runs Spring Boot app
	 * ./gradlew bootJar → creates executable JAR
	 * ./gradlew clean → removes build artifacts
	 */

	public static void main(String[] args) {
		SpringApplication.run(UniversityApplication.class, args);
	}

	@Bean
	public GraphQlSourceBuilderCustomizer customizer(FederationSchemaFactory factory) {
		return builder -> builder.schemaFactory(factory::createGraphQLSchema);
	}

	@Bean
	public FederationSchemaFactory federationSchemaFactory() {
		return new FederationSchemaFactory();
	}

	@Bean
	public RuntimeWiringConfigurer runtimeWiringConfigurer() {
		return wiring -> wiring
				.scalar(CustomDateScalar.DATE)
				.directive("metadata", new MetadataDirective())
				.type("Entity", builder -> builder.typeResolver(env -> {
					Object object = env.getObject();
					if (object instanceof University) {
						return env.getSchema().getObjectType("University");
					} else if (object instanceof Campus) {
						return env.getSchema().getObjectType("Campus");
					}
					return null;
				}))
				.type("SearchResult", builder -> builder.typeResolver(env -> {
					Object object = env.getObject();
					if (object instanceof University) {
						return env.getSchema().getObjectType("University");
					} else if (object instanceof Campus) {
						return env.getSchema().getObjectType("Campus");
					}
					return null;
				}));
	}

	@Bean
	public Instrumentation instrumentation(CustomInstrumentation customInstrumentation) {
		return customInstrumentation;
	}
}

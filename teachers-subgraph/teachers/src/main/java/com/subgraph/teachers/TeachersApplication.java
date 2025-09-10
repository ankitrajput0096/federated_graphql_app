package com.subgraph.teachers;

import com.subgraph.teachers.config.TypeResolverConfig;
import com.subgraph.teachers.directive.LogDirective;
import com.subgraph.teachers.directive.MetadataDirective;
import com.subgraph.teachers.directive.TransformDirective;
import com.subgraph.teachers.config.CustomTimeScalar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.data.federation.FederationSchemaFactory;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@SpringBootApplication
public class TeachersApplication {

	/**
	 * Gradle commands to manage this application.
	 * ./gradlew tasks → lists available tasks
	 * ./gradlew clean build → compiles + tests + packages
	 * ./gradlew bootRun → runs Spring Boot app
	 * ./gradlew bootJar → creates executable JAR
	 * ./gradlew clean → removes build artifacts
	 */
	public static void main(String[] args) {
		SpringApplication.run(TeachersApplication.class, args);
	}

	@Bean
	public GraphQlSourceBuilderCustomizer customizer(FederationSchemaFactory factory) {
		return builder -> builder.schemaFactory(factory::createGraphQLSchema);
	}

	@Bean
	FederationSchemaFactory federationSchemaFactory() {
		return new FederationSchemaFactory();
	}

	@Bean
	public RuntimeWiringConfigurer runtimeWiringConfigurer(TypeResolverConfig typeResolverConfig) {
		return wiringBuilder -> wiringBuilder
				.scalar(CustomTimeScalar.getTimeScalar())
				.directive("transform", new TransformDirective.TransformDirectiveWiring())
				.directive("log", new LogDirective.LogDirectiveWiring())
				.directive("metadata", new MetadataDirective.MetadataDirectiveWiring())
				.type("Teachable", builder -> builder.typeResolver(typeResolverConfig.teachableResolver()))
				.type("TeacherDetail", builder -> builder.typeResolver(typeResolverConfig.teacherDetailResolver()));
	}
}

package com.subgraph.students.config;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import graphql.schema.idl.RuntimeWiring;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Slf4j
@Configuration
public class CustomGpaScalar implements RuntimeWiringConfigurer {

    @Bean
    public GraphQLScalarType gpaScalar() {
        log.info("Creating GPA scalar bean");
        return GraphQLScalarType.newScalar()
                .name("GPA")
                .description("A custom scalar representing a GPA value (0.0 to 4.0)")
                .coercing(new Coercing<Float, Float>() {
                    @Override
                    public Float serialize(Object dataFetcherResult) throws CoercingSerializeException {
                        if (dataFetcherResult instanceof Float) {
                            float value = (Float) dataFetcherResult;
                            if (value >= 0.0f && value <= 4.0f) {
                                return value;
                            }
                            throw new CoercingSerializeException("GPA must be between 0.0 and 4.0, got " + value);
                        }
                        throw new CoercingSerializeException("Expected a Float for GPA, got " + (dataFetcherResult != null ? dataFetcherResult.getClass().getSimpleName() : "null"));
                    }

                    @Override
                    public Float parseValue(Object input) throws CoercingParseValueException {
                        if (input instanceof Float) {
                            float value = (Float) input;
                            if (value >= 0.0f && value <= 4.0f) {
                                return value;
                            }
                            throw new CoercingParseValueException("GPA must be between 0.0 and 4.0, got " + value);
                        }
                        throw new CoercingParseValueException("Expected a Float for GPA, got " + (input != null ? input.getClass().getSimpleName() : "null"));
                    }

                    @Override
                    public Float parseLiteral(Object input) throws CoercingParseLiteralException {
                        if (input instanceof Float) {
                            float value = (Float) input;
                            if (value >= 0.0f && value <= 4.0f) {
                                return value;
                            }
                            throw new CoercingParseLiteralException("GPA must be between 0.0 and 4.0, got " + value);
                        }
                        throw new CoercingParseLiteralException("Expected a Float literal for GPA, got " + (input != null ? input.getClass().getSimpleName() : "null"));
                    }
                })
                .build();
    }

    @Override
    public void configure(RuntimeWiring.Builder builder) {
        log.info("Wiring GPA scalar to GraphQL runtime");
        builder.scalar(gpaScalar());
    }
}
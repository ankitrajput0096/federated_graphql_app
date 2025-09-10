package com.graphql.classes.config;

import graphql.schema.*;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class CustomTimeScalar {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static GraphQLScalarType getTimeScalar() {
        return GraphQLScalarType.newScalar()
                .name("Time")
                .description("A custom scalar type for representing time in HH:mm format")
                .coercing(new Coercing<LocalTime, String>() {
                    @Override
                    public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                        if (dataFetcherResult instanceof LocalTime time) {
                            return time.format(FORMATTER);
                        }
                        throw new CoercingSerializeException("Expected a LocalTime object, but got " + dataFetcherResult);
                    }

                    @Override
                    public LocalTime parseValue(Object input) throws CoercingParseValueException {
                        if (input instanceof String string) {
                            try {
                                return LocalTime.parse(string, FORMATTER);
                            } catch (DateTimeParseException e) {
                                throw new CoercingParseValueException("Invalid time format: " + input + ". Expected HH:mm");
                            }
                        }
                        throw new CoercingParseValueException("Expected a String input, but got " + input);
                    }

                    @Override
                    public LocalTime parseLiteral(Object input) throws CoercingParseLiteralException {
                        if (input instanceof graphql.language.StringValue stringValue) {
                            try {
                                return LocalTime.parse(stringValue.getValue(), FORMATTER);
                            } catch (DateTimeParseException e) {
                                throw new CoercingParseLiteralException("Invalid time format: " + stringValue.getValue() + ". Expected HH:mm");
                            }
                        }
                        throw new CoercingParseLiteralException("Expected a StringValue, but got " + input);
                    }
                })
                .build();
    }
}
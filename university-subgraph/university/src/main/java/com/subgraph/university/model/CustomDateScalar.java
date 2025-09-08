package com.subgraph.university.model;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomDateScalar {
    public static final GraphQLScalarType DATE = GraphQLScalarType.newScalar()
            .name("Date")
            .description("A custom scalar representing a date in ISO format (yyyy-MM-dd)")
            .coercing(new Coercing<LocalDate, String>() {
                private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

                @Override
                public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                    if (dataFetcherResult instanceof LocalDate) {
                        return formatter.format((LocalDate) dataFetcherResult);
                    }
                    throw new CoercingSerializeException("Expected a LocalDate object.");
                }

                @Override
                public LocalDate parseValue(Object input) throws CoercingParseValueException {
                    try {
                        return LocalDate.parse(input.toString(), formatter);
                    } catch (Exception e) {
                        throw new CoercingParseValueException("Invalid ISO date: " + input, e);
                    }
                }

                @Override
                public LocalDate parseLiteral(Object input) throws CoercingParseLiteralException {
                    if (input instanceof StringValue) {
                        try {
                            return LocalDate.parse(((StringValue) input).getValue(), formatter);
                        } catch (Exception e) {
                            throw new CoercingParseLiteralException("Invalid ISO date: " + input, e);
                        }
                    }
                    throw new CoercingParseLiteralException("Expected a StringValue.");
                }
            })
            .build();
}

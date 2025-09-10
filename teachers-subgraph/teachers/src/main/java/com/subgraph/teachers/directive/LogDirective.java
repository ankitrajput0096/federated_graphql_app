package com.subgraph.teachers.directive;

import graphql.language.StringValue;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import graphql.schema.GraphQLFieldDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.stereotype.Component;

@Component
public class LogDirective implements RuntimeWiringConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(LogDirective.class);

    @Override
    public void configure(graphql.schema.idl.RuntimeWiring.Builder wiringBuilder) {
        wiringBuilder.directive("log", new LogDirectiveWiring());
    }

    public static class LogDirectiveWiring implements SchemaDirectiveWiring {
        @Override
        public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
            GraphQLFieldDefinition field = env.getElement();
            DataFetcher<?> originalDataFetcher = env.getFieldDataFetcher();
            String level = ((StringValue) env.getDirective().getArgument("level").getArgumentValue().getValue()).getValue();

            DataFetcher<?> loggingDataFetcher = (DataFetchingEnvironment dfe) -> {
                String message = String.format("Executing field %s with log level: %s", field.getName(), level);
                switch (level.toUpperCase()) {
                    case "DEBUG" -> logger.debug(message);
                    case "WARN" -> logger.warn(message);
                    case "ERROR" -> logger.error(message);
                    case "TRACE" -> logger.trace(message);
                    default -> logger.info(message); // Default to INFO
                }
                return originalDataFetcher.get(dfe);
            };

            env.setFieldDataFetcher(loggingDataFetcher);
            return field;
        }
    }
}

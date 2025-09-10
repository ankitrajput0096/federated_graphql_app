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
public class TransformDirective implements RuntimeWiringConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(TransformDirective.class);

    @Override
    public void configure(graphql.schema.idl.RuntimeWiring.Builder wiringBuilder) {
        wiringBuilder.directive("transform", new TransformDirectiveWiring());
    }

    public static class TransformDirectiveWiring implements SchemaDirectiveWiring {
        @Override
        public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
            GraphQLFieldDefinition field = env.getElement();
            DataFetcher<?> originalDataFetcher = env.getFieldDataFetcher();
            String format = ((StringValue) env.getDirective().getArgument("format").getArgumentValue().getValue()).getValue();

            DataFetcher<?> transformedDataFetcher = (DataFetchingEnvironment dfe) -> {
                Object result = originalDataFetcher.get(dfe);
                logger.info("Applying transform (format: {}) to field {} with value: {}", format, field.getName(), result);
                if (result instanceof String string) {
                    return switch (format) {
                        case "uppercase" -> string.toUpperCase();
                        case "capitalize" -> string.length() > 0 ? string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase() : string;
                        default -> string;
                    };
                }
                return result;
            };

            env.setFieldDataFetcher(transformedDataFetcher);
            return field;
        }
    }
}

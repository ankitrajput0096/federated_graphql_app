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
public class MetadataDirective implements RuntimeWiringConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(MetadataDirective.class);

    @Override
    public void configure(graphql.schema.idl.RuntimeWiring.Builder wiringBuilder) {
        wiringBuilder.directive("metadata", new MetadataDirectiveWiring());
    }

    public static class MetadataDirectiveWiring implements SchemaDirectiveWiring {
        @Override
        public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
            GraphQLFieldDefinition field = env.getElement();
            DataFetcher<?> originalDataFetcher = env.getFieldDataFetcher();
            String tag = ((StringValue) env.getDirective().getArgument("tag").getArgumentValue().getValue()).getValue();

            DataFetcher<?> metadataDataFetcher = (DataFetchingEnvironment dfe) -> {
                logger.info("Accessing field {} with metadata tag: {}", field.getName(), tag);
                return originalDataFetcher.get(dfe);
            };

            env.setFieldDataFetcher(metadataDataFetcher);
            return field;
        }
    }
}

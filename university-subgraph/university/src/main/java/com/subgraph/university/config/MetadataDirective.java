package com.subgraph.university.config;

import graphql.language.StringValue;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import graphql.schema.GraphQLFieldDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetadataDirective implements SchemaDirectiveWiring {
    private static final Logger logger = LoggerFactory.getLogger(MetadataDirective.class);

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
        GraphQLFieldDefinition field = env.getElement();
        DataFetcher<?> originalDataFetcher = env.getFieldDataFetcher();
        String tag = ((StringValue) env.getDirective().getArgument("tag").getArgumentValue().getValue()).getValue();

        DataFetcher<?> loggingDataFetcher = (DataFetchingEnvironment dfe) -> {
            logger.info("Accessing field {} with metadata tag: {}", field.getName(), tag);
            return originalDataFetcher.get(dfe);
        };

        env.setFieldDataFetcher(loggingDataFetcher);
        return field;
    }
}

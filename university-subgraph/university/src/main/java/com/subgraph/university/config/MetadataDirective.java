package com.subgraph.university.config;

import graphql.language.StringValue;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import graphql.schema.GraphQLFieldDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the @metadata directive, which logs access to fields tagged with a metadata label (e.g., "core") in
 * the GraphQL schema. It enhances traceability by logging when specific fields are queried, aiding in debugging,
 * auditing, or monitoring. When a query accesses a field with @metadata (e.g., status), the system logs the access
 * and then resolves the field as usual.
 */
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

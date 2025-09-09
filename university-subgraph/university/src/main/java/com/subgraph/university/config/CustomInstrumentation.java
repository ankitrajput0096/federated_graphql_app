package com.subgraph.university.config;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.language.Document;
import graphql.language.Field;
import graphql.language.OperationDefinition;
import graphql.language.SelectionSet;
import graphql.parser.Parser;
import graphql.ExecutionResult;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Intercepts the GraphQL query execution at the start to validate syntax and enforce query depth limits.
 * Acts as a gatekeeper, rejecting invalid or overly complex queries before they reach the resolvers,
 * improving security and performance.
 */
@Component
public class CustomInstrumentation implements Instrumentation {
    private static final Logger logger = LoggerFactory.getLogger(CustomInstrumentation.class);
    private static final int MAX_DEPTH = 5;
    private static final Parser parser = new Parser();

    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters, InstrumentationState state) {
        logger.info("Intercepting GraphQL request: {}", parameters.getQuery());

        // Parse the query string into a Document
        String query = parameters.getExecutionInput().getQuery();
        Document document;
        try {
            document = parser.parseDocument(query);
        } catch (Exception e) {
            GraphQLError error = GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.ValidationError)
                    .message("Invalid query syntax: " + e.getMessage())
                    .extensions(Map.of("code", "INVALID_QUERY"))
                    .build();
            throw new QueryDepthExceededException("Invalid query", error);
        }

        // Check query depth
        int depth = calculateQueryDepth(document);
        if (depth > MAX_DEPTH) {
            GraphQLError error = GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.ValidationError)
                    .message("Query depth exceeds maximum allowed: " + MAX_DEPTH)
                    .extensions(Map.of("code", "MAX_DEPTH_EXCEEDED"))
                    .build();
            throw new QueryDepthExceededException("Query too deep", error);
        }

        return new InstrumentationContext<>() {
            @Override
            public void onDispatched() {}
            @Override
            public void onCompleted(ExecutionResult result, Throwable t) {}
        };
    }

    private int calculateQueryDepth(Document document) {
        if (document == null) {
            return 0;
        }
        return document.getDefinitions().stream()
                .filter(def -> def instanceof OperationDefinition)
                .mapToInt(def -> getSelectionDepth(((OperationDefinition) def).getSelectionSet(), 1))
                .max()
                .orElse(0);
    }

    private int getSelectionDepth(SelectionSet selectionSet, int currentDepth) {
        if (selectionSet == null || selectionSet.getSelections().isEmpty()) {
            return currentDepth;
        }
        return selectionSet.getSelections().stream()
                .filter(sel -> sel instanceof Field)
                .mapToInt(sel -> getSelectionDepth(((Field) sel).getSelectionSet(), currentDepth + 1))
                .max()
                .orElse(currentDepth);
    }

    // Custom exception to carry GraphQLError
    @Getter
    public static class QueryDepthExceededException extends RuntimeException {
        private final GraphQLError error;

        public QueryDepthExceededException(String message, GraphQLError error) {
            super(message);
            this.error = error;
        }

    }
}
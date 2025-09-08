package com.subgraph.university.config;

import com.subgraph.university.config.CustomInstrumentation.QueryDepthExceededException;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class ErrorHandler implements DataFetcherExceptionResolver {

    @Override
    public Mono<List<GraphQLError>> resolveException(Throwable exception, DataFetchingEnvironment environment) {
        GraphQLError error;

        if (exception instanceof QueryDepthExceededException qde) {
            error = qde.getError();
        } else if (exception instanceof IllegalArgumentException) {
            error = GraphqlErrorBuilder.newError(environment)
                    .errorType(ErrorType.ValidationError)
                    .message(exception.getMessage())
                    .extensions(Map.of("code", HttpStatus.BAD_REQUEST.value()))
                    .build();
        }  else if (exception instanceof DataIntegrityViolationException) {
            error = GraphqlErrorBuilder.newError(environment)
                    .errorType(ErrorType.DataFetchingException)
                    .message("Database constraint violation: " + exception.getMessage())
                    .extensions(Map.of("code", HttpStatus.BAD_REQUEST.value()))
                    .build();
        }
        else if (exception instanceof NoSuchElementException) {
            error = GraphqlErrorBuilder.newError(environment)
                    .errorType(ErrorType.DataFetchingException)
                    .message("University not found")
                    .extensions(Map.of("code", HttpStatus.NOT_FOUND.value()))
                    .build();
        } else if (exception instanceof JpaSystemException) {
            error = GraphqlErrorBuilder.newError(environment)
                    .errorType(ErrorType.DataFetchingException)
                    .message("Database error: " + exception.getMessage())
                    .extensions(Map.of("code", HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .build();
        } else {
            error = GraphqlErrorBuilder.newError(environment)
                    .errorType(ErrorType.ExecutionAborted)
                    .message("Internal server error")
                    .extensions(Map.of("code", HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .build();
        }

        return Mono.just(List.of(error));
    }
}
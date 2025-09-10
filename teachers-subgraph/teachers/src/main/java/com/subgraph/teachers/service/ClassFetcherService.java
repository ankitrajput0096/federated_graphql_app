package com.subgraph.teachers.service;

import com.subgraph.teachers.model.Class; // Import or create if needed; Align with classes-service model
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ClassFetcherService {
    private final GraphQlClient graphQlClient;

    public ClassFetcherService() {
        this.graphQlClient = HttpGraphQlClient.builder()
                .url("http://classes:8085/graphql") // Use 'classes-service:8084' in Docker
                .build();
    }

    public Mono<List<Class>> fetchClasses(String teacherId) {
        String query = String.format("query { classes(teacherId: \"%s\") { id subject teacherId duration } }", teacherId);
        return graphQlClient.document(query)
                .retrieve("classes")
                .toEntityList(Class.class);
    }

    public Mono<List<Class>> fetchBatchClasses(List<String> teacherIds) {
        String query = String.format("query { batchClasses(teacherIds: %s) { id subject teacherId duration } }", teacherIds);
        return graphQlClient.document(query)
                .retrieve("batchClasses")
                .toEntityList(Class.class);
    }
}
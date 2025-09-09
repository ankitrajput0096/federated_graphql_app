package com.subgraph.students.config;

import com.subgraph.students.model.Grade;
import com.subgraph.students.model.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class TypeResolverConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiring -> wiring
                .type("Rateable", builder -> builder.typeResolver(env -> {
                    Object obj = env.getObject();
                    if (obj instanceof Grade) {
                        return env.getSchema().getObjectType("Grade");
                    }
                    throw new IllegalArgumentException("Unknown Rateable type: " + obj.getClass().getName());
                }))
                .type("StudentInfo", builder -> builder.typeResolver(env -> {
                    Object obj = env.getObject();
                    if (obj instanceof Student) {
                        return env.getSchema().getObjectType("Student");
                    } else if (obj instanceof Grade) {
                        return env.getSchema().getObjectType("Grade");
                    }
                    throw new IllegalArgumentException("Unknown StudentInfo type: " + obj.getClass().getName());
                }));
    }
}
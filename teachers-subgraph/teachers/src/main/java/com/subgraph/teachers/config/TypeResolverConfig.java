package com.subgraph.teachers.config;

import com.subgraph.teachers.model.Class;
import com.subgraph.teachers.model.Teacher;
import graphql.schema.TypeResolver;
import org.springframework.stereotype.Component;

@Component
public class TypeResolverConfig {

    public TypeResolver teachableResolver() {
        return env -> {
            Object object = env.getObject();
            if (object instanceof Class) {
                return env.getSchema().getObjectType("Class");
            }
            return null; // Add other types if Teachable extends
        };
    }

    public TypeResolver teacherDetailResolver() {
        return env -> {
            Object object = env.getObject();
            if (object instanceof Teacher) {
                return env.getSchema().getObjectType("Teacher");
            } else if (object instanceof Class) {
                return env.getSchema().getObjectType("Class");
            }
            return null;
        };
    }
}

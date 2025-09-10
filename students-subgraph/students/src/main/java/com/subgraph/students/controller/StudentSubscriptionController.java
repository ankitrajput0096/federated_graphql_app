package com.subgraph.students.controller;

import com.subgraph.students.model.Student;
import com.subgraph.students.model.StudentSource;
import com.subgraph.students.model.University;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.List;

@Slf4j
@Controller
public class StudentSubscriptionController {

    @SubscriptionMapping
    public Flux<Student> studentUpdated(@Argument String studentId) {
        log.info("Subscription started for studentId: {}", studentId);
        return Flux.interval(Duration.ofSeconds(5))
                .map(i -> {
                    log.info("Emitting update for studentId: {}", studentId);
                    return StudentSource.getStudentById(studentId);
                })
                .filter(student -> student != null) // Avoid emitting null if student not found
                .onErrorResume(e -> {
                    log.error("Error in studentUpdated subscription: {}", e.getMessage());
                    return Flux.empty();
                });
    }
}

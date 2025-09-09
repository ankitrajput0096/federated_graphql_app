package com.subgraph.students.service;

import com.subgraph.students.model.Grade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class GradeFetcherService {
    private final RestTemplate restTemplate;
    private final String gradesBaseUrl;

    public GradeFetcherService(RestTemplate restTemplate, @Value("${grades.service.base-url}") String gradesBaseUrl) {
        this.restTemplate = restTemplate;
        this.gradesBaseUrl = gradesBaseUrl;
    }

    public Grade fetchGrade(String studentId) {
        String url = gradesBaseUrl + "/" + studentId;
        log.info("Fetching grade from URL: {}", url);
        try {
            return restTemplate.getForObject(url, Grade.class);
        } catch (Exception e) {
            log.error("Failed to fetch grade for student {}: {}", studentId, e.getMessage());
            return null; // To be improved in sub-phase 2.3 with proper error handling
        }
    }

    public List<Grade> fetchBatchGrades(List<String> studentIds) {
        // RestTemplate expects array for POST body; Grade[].class for response
        Grade[] grades = restTemplate.postForObject(gradesBaseUrl + "/batch", studentIds, Grade[].class);
        return grades != null ? List.of(grades) : List.of();
    }
}
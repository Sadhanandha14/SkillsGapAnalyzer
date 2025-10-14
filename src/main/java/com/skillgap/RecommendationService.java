package com.skillgap;

import java.util.HashMap;
import java.util.Map;

public class RecommendationService {

    private final Map<String, String> recommendations = new HashMap<>();

    public RecommendationService() {
        recommendations.put("Java", "Practice OOP, Collections, and Streams on LeetCode.");
        recommendations.put("Spring", "Learn from official Spring Boot docs and build REST APIs.");
        recommendations.put("MongoDB", "Go through MongoDB University free courses.");
        recommendations.put("Git", "Practice GitHub workflows, branching, and merging.");
        recommendations.put("REST API", "Learn HTTP methods and build REST services in Java.");
        recommendations.put("HTML", "Practice HTML5 structure and semantic tags.");
        recommendations.put("CSS", "Learn Flexbox, Grid, and responsive design.");
        recommendations.put("JavaScript", "Understand DOM, ES6, and basic frameworks.");
    }

    public String getRecommendation(String skill) {
        return recommendations.getOrDefault(skill, "Explore online tutorials or courses for " + skill);
    }
}


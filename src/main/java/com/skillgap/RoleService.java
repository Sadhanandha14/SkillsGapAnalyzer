package com.skillgap;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.List;

public class RoleService {
    private final MongoCollection<Document> collection;

    public RoleService(MongoDatabase db) {
        this.collection = db.getCollection("roles");
    }

    // Add a new job role
    public void addRole(String role, List<String> requiredSkills) {
        Document doc = new Document("role", role)
                .append("required_skills", requiredSkills);
        collection.insertOne(doc);
        System.out.println("Role added successfully!");
    }

    // Get role by name
    public Document getRole(String role) {
        return collection.find(new Document("role", role)).first();
    }
}


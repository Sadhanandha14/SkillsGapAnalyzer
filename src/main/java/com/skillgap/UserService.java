

package com.skillgap;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.model.UpdateOptions;

import java.util.*;

public class UserService {
    private final MongoCollection<Document> collection;

    public UserService(MongoDatabase db) {
        this.collection = db.getCollection("users");
    }

    // Add or update user
    public void addUser(String userId, String name, List<String> skills) {
        Document doc = new Document("_id", userId)
                .append("name", name)
                .append("skills", skills);

        collection.updateOne(
                new Document("_id", userId),
                new Document("$set", doc),
                new UpdateOptions().upsert(true) // adds if not exists
        );

        //System.out.println(" User added or updated successfully!");
    }

    // Get user by ID
    public Document getUser(String userId) {
        return collection.find(new Document("_id", userId)).first();
    }

    //  Update user skills (merge old + new skills)
    public void updateUserSkills(String userId, List<String> newSkills) {
        Document user = collection.find(new Document("_id", userId)).first();

        if (user != null) {
            List<String> currentSkills = user.getList("skills", String.class);

            // Merge old + new skills without duplicates
            Set<String> updatedSkills = new HashSet<>(currentSkills);
            updatedSkills.addAll(newSkills);

            collection.updateOne(
                    new Document("_id", userId),
                    new Document("$set", new Document("skills", updatedSkills))
            );

            //System.out.println(" Skills merged successfully! (Old + New saved)");
        } else {
            System.out.println(" User not found!");
        }
    }

    // Delete user
    public boolean deleteUser(String userId) {
        long deletedCount = collection.deleteOne(new Document("_id", userId)).getDeletedCount();
        return deletedCount > 0;
    }
}


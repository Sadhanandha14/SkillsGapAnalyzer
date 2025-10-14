package com.skillgap;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DBConnection {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "skillgapdb";

    // Create MongoClient (caller must close)
    public static MongoClient createClient() {
        return MongoClients.create(CONNECTION_STRING);
    }

    // Get database using the given client
    public static MongoDatabase getDatabase(MongoClient client) {
        return client.getDatabase(DATABASE_NAME);
    }
}



package com.skillgap;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.util.*;

public class SkillGapAnalyzer {

    public static void main(String[] args) {
        System.out.println("===== Skill Gap Analyzer =====");

        try (MongoClient client = DBConnection.createClient()) { // Auto-close client
            MongoDatabase db = DBConnection.getDatabase(client);

            RoleService roleService = new RoleService(db);
            UserService userService = new UserService(db);
            RecommendationService recommendationService = new RecommendationService();

            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.println("\n==============================");
                System.out.println("1.  Add User");
                System.out.println("2.  Find Skill Gap and Recommend Learning");
                System.out.println("3.  Update User Skills");
                System.out.println("4.  Delete User");
                System.out.println("5.  Exit");
                System.out.println("==============================");
                System.out.print("Choose an option: ");
                String choice = sc.nextLine().trim();

                switch (choice) {
                    case "1":
                        // ------------------- ADD USER -------------------
                        System.out.print("Enter User ID: ");
                        String userId = sc.nextLine().trim();

                        System.out.print("Enter Name: ");
                        String name = sc.nextLine().trim();

                        System.out.print("Enter Skills (comma separated): ");
                        List<String> skills = Arrays.stream(sc.nextLine().split(","))
                                                    .map(String::trim)
                                                    .filter(s -> !s.isEmpty())
                                                    .toList();

                        userService.addUser(userId, name, skills);
                       System.out.println("User added successfully!");
                        break;

                    case "2":
                        // ------------------- SKILL GAP ANALYSIS -------------------
                        System.out.print("Enter User ID: ");
                        String userIdGap = sc.nextLine().trim();

                        Document userDoc = userService.getUser(userIdGap);
                        if (userDoc == null) {
                            System.out.println("User not found!");
                            break;
                        }

                        System.out.print("Enter desired Job Role: ");
                        String role = sc.nextLine().trim();

                        Document jobRole = roleService.getRole(role);
                        if (jobRole == null) {
                            System.out.println("No such role found. Adding sample role data...");
                            List<String> required = List.of("Java", "Spring", "MongoDB", "Git", "REST API");
                            roleService.addRole(role, required);
                            jobRole = roleService.getRole(role);
                            System.out.println("Sample role added successfully!");
                        }

                        List<String> userSkills = (List<String>) userDoc.get("skills");
                        List<String> requiredSkills = (List<String>) jobRole.get("required_skills");

                        Set<String> missingSkills = new HashSet<>(requiredSkills);
                        missingSkills.removeAll(userSkills);

                        Set<String> matchedSkills = new HashSet<>(userSkills);
                        matchedSkills.retainAll(requiredSkills);

                        System.out.println("\nSkill Gap Analysis for " + userDoc.getString("name") + " (" + userIdGap + ")");
                        System.out.println("Role: " + role);
                        System.out.println("\nMatched Skills: " + matchedSkills);
                        System.out.println("Missing Skills: " + missingSkills);

                        if (!missingSkills.isEmpty()) {
                            System.out.println("\nLearning Recommendations:");
                            for (String skill : missingSkills) {
                                System.out.println("- " + skill + ": " + recommendationService.getRecommendation(skill));
                            }
                        } else {
                            System.out.println("\nou already have all required skills for this role!");
                        }
                        break;

                    case "3":
                        // ------------------- UPDATE USER SKILLS -------------------
                        System.out.print("Enter User ID to update: ");
                        String updateId = sc.nextLine().trim();

                        Document existingUser = userService.getUser(updateId);
                        if (existingUser == null) {
                            System.out.println("User not found!");
                            break;
                        }

                        System.out.println("Current skills: " + existingUser.get("skills"));
                        System.out.print("Enter new skills (comma separated): ");
                        List<String> newSkills = Arrays.stream(sc.nextLine().split(","))
                                                       .map(String::trim)
                                                       .filter(s -> !s.isEmpty())
                                                       .toList();

                        userService.updateUserSkills(updateId, newSkills);
                        System.out.println("User skills updated successfully!");
                        break;

                    case "4":
                        // ------------------- DELETE USER -------------------
                        System.out.print("Enter User ID to delete: ");
                        String deleteId = sc.nextLine().trim();

                        boolean deleted = userService.deleteUser(deleteId);
                        if (deleted) {
                            System.out.println("User deleted successfully!");
                        } else {
                            System.out.println("User not found or could not be deleted!");
                        }
                        break;

                    case "5":
                        // ------------------- EXIT -------------------
                        System.out.println("\nExiting Skill Gap Analyzer. Goodbye!");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid choice! Please select a valid option (1–5).");
                }
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


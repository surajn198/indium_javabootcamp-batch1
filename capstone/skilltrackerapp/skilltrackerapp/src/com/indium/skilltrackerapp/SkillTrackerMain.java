package com.indium.skilltrackerapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.indium.skilltrackerapp.dao.AssociateDAO;
import com.indium.skilltrackerapp.dao.AssociateDAOImpl;
import com.indium.skilltrackerapp.dao.SkillDAO;
import com.indium.skilltrackerapp.dao.SkillDAOImpl;
import com.indium.skilltrackerapp.model.Associate;
import com.indium.skilltrackerapp.model.Skill;
import com.indium.skilltrackerapp.model.AssociateSkillCount;

public class SkillTrackerMain {
	private static final Connection dbConnection = initializeDatabaseConnection(); // database connection logic
    private static final AssociateDAO associateDAO = new AssociateDAOImpl(dbConnection);
    private static final SkillDAO skillDAO = new SkillDAOImpl(dbConnection);
    private static final Scanner scanner = new Scanner(System.in); // Initialize the scanner


    public static void main(String[] args) {
        while (true) {
            displayMainMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    addNewAssociate();
                    break;
                case 2:
                    listAssociates();
                    break;
                case 3:
                    searchAssociate();
                    break;
                case 4:
                    editAssociate();
                    break;
                case 5:
                    deleteAssociate();
                    break;
                case 6:
                    addSkillToAssociate();
                    break;
                case 7:
                    editSkill();
                    break;
                case 8:
                    deleteSkill();
                    break;
                case 9:
                    displayMetrics();
                    break;
                case 10:
                    System.out.println("Exiting Skill Tracker App.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
    private static Connection initializeDatabaseConnection() {
        //  database connection logic returning the Connection object.

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://localhost:3306/skilltracker";
            String username = "root";
            String password = "sriganesh@002";
            return DriverManager.getConnection(jdbcUrl, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database connection.");
        }
    }
    
    private static void deleteAssociate() {
        System.out.print("Enter Associate ID to delete: ");
        int associateId = getUserChoice();

        if (associateDAO.deleteAssociate(associateId)) {
            System.out.println("Associate deleted successfully.");
        } else {
            System.out.println("Associate not found.");
        }
    }

    private static void addSkillToAssociate() {
        System.out.print("Enter Associate ID to add a skill: ");
        int associateId = getUserChoice();

        Associate associate = associateDAO.getAssociateById(associateId);
        if (associate == null) {
            System.out.println("Associate not found.");
            return;
        }

        System.out.print("Enter Skill Name: ");
        String skillName = scanner.nextLine();
        System.out.print("Enter Skill Description: ");
        String skillDescription = scanner.nextLine();
        System.out.print("Enter Skill Category (Primary/Secondary): ");
        String skillCategory = scanner.nextLine();
        System.out.print("Enter Skill Experience (in months): ");
        int skillExperience = getUserChoice();

        Skill skill = new Skill();
        skill.setName(skillName);
        skill.setDescription(skillDescription);
        skill.setCategory(skillCategory);
        skill.setExperience(skillExperience);

        if (skillDAO.addSkillToAssociate(associate.getId(), skill)) {
            System.out.println("Skill added to associate successfully.");
        } else {
            System.out.println("Failed to add the skill to the associate.");
        }
    }

    private static void editSkill() {
        System.out.print("Enter Skill ID to edit: ");
        int skillId = getUserChoice();

        Skill skill = skillDAO.getSkillById(skillId);

        if (skill == null) {
            System.out.println("Skill not found.");
            return;
        }

        System.out.print("Enter updated Skill Name: ");
        String updatedSkillName = scanner.nextLine();
        System.out.print("Enter updated Skill Description: ");
        String updatedSkillDescription = scanner.nextLine();
        System.out.print("Enter updated Skill Category (Primary/Secondary): ");
        String updatedSkillCategory = scanner.nextLine();
        System.out.print("Enter updated Skill Experience (in months): ");
        int updatedSkillExperience = getUserChoice();

        skill.setName(updatedSkillName);
        skill.setDescription(updatedSkillDescription);
        skill.setCategory(updatedSkillCategory);
        skill.setExperience(updatedSkillExperience);

        skillDAO.updateSkill(skill);

        System.out.println("Skill updated successfully.");
    }

    private static void editAssociate() {
        System.out.print("Enter Associate ID to edit: ");
        int associateId = getUserChoice();

        Associate associate = associateDAO.getAssociateById(associateId);

        if (associate == null) {
            System.out.println("Associate not found.");
            return;
        }

        System.out.print("Enter updated Associate Name: ");
        String updatedName = scanner.nextLine();
        System.out.print("Enter updated Associate Age: ");
        int updatedAge = getUserChoice();

        associate.setName(updatedName);
        associate.setAge(updatedAge);

        associateDAO.updateAssociate(associate);

        System.out.println("Associate updated successfully.");
    }

    private static void displayMetrics() {
    	int n = 0;
        while (true) {
            System.out.println("Skill Tracker App Metrics:");
            System.out.println("0. Back to Main Menu");
            System.out.println("1. Total No of Associates");
            System.out.println("2. Total No of Associates who have more than N skills");
            System.out.println("3. List of Associate IDs who have more than N skills");
            System.out.println("4. Total No of Associates who have the given set of skills");
            System.out.println("5. Associate-wise Skill Count");
            System.out.println("6. BU-wise Associate Count");
            System.out.println("7. Location-wise Skill Count");
            System.out.println("8. Skill-wise Associate Count");
            System.out.println("9. Top N Skills");
            System.out.println("10. Skill-wise Avg Associate Experience");

            System.out.print("Enter your choice: ");
            int choice = getUserChoice();
            
            switch (choice) {
                case 0:
                    return; // Back to Main Menu
                case 1:
                    int totalAssociates = associateDAO.getTotalAssociateCount();
                    System.out.println("Total No of Associates: " + totalAssociates);
                    break;
                case 2:
                    System.out.print("Enter N: ");
                    n = getUserChoice(); //  'n' variable
                    int associatesWithMoreThanNSkills = associateDAO.getAssociatesWithMoreThanNSkills(n);
                    System.out.println("Total No of Associates who have more than " + n + " skills: " + associatesWithMoreThanNSkills);
                    break;
                case 3:
                    List<Integer> associateIdsWithMoreThanNSkills = associateDAO.getAssociateIdsWithMoreThanNSkills(n);
                    System.out.println("List of Associate IDs who have more than " + n + " skills: " + associateIdsWithMoreThanNSkills);
                    break;
                case 4:
                    System.out.print("Enter the skills (comma-separated) to search for: ");
                    String skillsInput = scanner.nextLine();
                    String[] skills = skillsInput.split(",");
                    int associatesWithGivenSkills = associateDAO.getAssociatesWithGivenSkills(skills);
                    System.out.println("Total No of Associates who have the given set of skills: " + associatesWithGivenSkills);
                    break;
                case 5:
                    System.out.println("Associate-wise Skill Count:");
                    List<AssociateSkillCount> associateSkillCounts = associateDAO.getAssociateSkillCounts();
                    for (AssociateSkillCount associateSkillCount : associateSkillCounts) {
                        System.out.println("Associate ID: " + associateSkillCount.getAssociateId() +
                                ", Skill Count: " + associateSkillCount.getSkillCount());
                    }
                    break;
                case 6:
                    System.out.println("BU-wise Associate Count:");
                    Map<String, Integer> buWiseAssociateCount = associateDAO.getBuWiseAssociateCount();
                    for (Map.Entry<String, Integer> entry : buWiseAssociateCount.entrySet()) {
                        System.out.println("BU: " + entry.getKey() + ", Associate Count: " + entry.getValue());
                    }
                    break;
                case 7:
                    System.out.println("Location-wise Skill Count:");
                    Map<String, Integer> locationWiseSkillCount = skillDAO.getLocationWiseSkillCount();
                    for (Map.Entry<String, Integer> entry : locationWiseSkillCount.entrySet()) {
                        System.out.println("Location: " + entry.getKey() + ", Skill Count: " + entry.getValue());
                    }
                    break;
                case 8:
                    System.out.println("Skill-wise Associate Count:");
                    Map<String, Integer> skillWiseAssociateCount = associateDAO.getSkillWiseAssociateCount();
                    for (Map.Entry<String, Integer> entry : skillWiseAssociateCount.entrySet()) {
                        System.out.println("Skill: " + entry.getKey() + ", Associate Count: " + entry.getValue());
                    }
                    break;
                case 9:
                    System.out.print("Enter the value of N for Top N skills: ");
                    int topNSkills = getUserChoice();
                    List<Skill> topNSkillsList = skillDAO.getTopNSkills(topNSkills);
                    System.out.println("Top " + topNSkills + " Skills (Primary vs Secondary):");
                    for (Skill skill : topNSkillsList) {
                        System.out.println("Skill ID: " + skill.getId() + ", Skill Name: " + skill.getName() +
                                ", Category: " + skill.getCategory());
                    }
                    break;
                case 10:
                    System.out.println("Skill-wise Avg Associate Experience:");
                    Map<String, Double> skillWiseAvgExperience = skillDAO.getSkillWiseAvgExperience();
                    for (Map.Entry<String, Double> entry : skillWiseAvgExperience.entrySet()) {
                        System.out.println("Skill: " + entry.getKey() + ", Avg Experience: " + entry.getValue() + " months");
                    }
                    break;
                default:
                    System.out.println("Invalid option. Please enter a valid option.");
                    break;
            }
        }
    }

    private static int getUserChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        return choice;
    }

    private static void displayMainMenu() {
        System.out.println("Skill Tracker App Main Menu");
        System.out.println("1. Add new Associate");
        System.out.println("2. List Associates");
        System.out.println("3. Search Associate");
        System.out.println("4. Edit Associate");
        System.out.println("5. Delete Associate");
        System.out.println("6. Add Skill to Associate");
        System.out.println("7. Edit Skill");
        System.out.println("8. Delete Skill");
        System.out.println("9. Display Metrics");
        System.out.println("10. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addNewAssociate() {
        System.out.println("Add New Associate:");

        System.out.print("Enter Associate Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Associate Age: ");
        int age = getUserChoice();
        System.out.print("Enter Business Unit: ");
        String businessUnit = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Location: ");
        String location = scanner.nextLine();

        System.out.print("Enter Skills (comma-separated): ");
        String skillsInput = scanner.nextLine();
        String[] skills = skillsInput.split("\\s*,\\s*"); // Split by comma with optional spaces

        Associate newAssociate = associateDAO.addAssociate(name, age, businessUnit, email, location, skills);

        if (newAssociate != null) {
            System.out.println("New Associate added successfully with ID: " + newAssociate.getId());
        } else {
            System.out.println("Failed to add the new Associate.");
        }
    }

    private static void listAssociates() {
        System.out.println("List Associates:");

        List<Associate> associates = associateDAO.getAllAssociates();

        if (associates.isEmpty()) {
            System.out.println("No associates found.");
        } else {
            System.out.println("List of Associates:");

            for (Associate associate : associates) {
                System.out.println("ID: " + associate.getId());
                System.out.println("Name: " + associate.getName());
                System.out.println("Age: " + associate.getAge());
                System.out.println("Business Unit: " + associate.getBusinessUnit());
                System.out.println("Email: " + associate.getEmail());
                System.out.println("Location: " + associate.getLocation());
                System.out.println("Skills: " + String.join(", ", associate.getSkills()));
                System.out.println("Create Time: " + associate.getCreateTime());
                System.out.println("Update Time: " + associate.getUpdateTime());
                System.out.println("==================================");
            }
        }
    }

    private static void deleteSkill() {
        System.out.print("Enter Skill ID to delete: ");
        int skillId = getUserChoice();

        if (skillDAO.deleteSkill(skillId)) {
            System.out.println("Skill deleted successfully.");
        } else {
            System.out.println("Skill not found.");
        }
    }

    private static void searchAssociate() {
        System.out.print("Enter Associate ID to search: ");
        int associateId = getUserChoice();

        Associate associate = associateDAO.getAssociateById(associateId);

        if (associate != null) {
            System.out.println("Associate Details:");
            System.out.println("ID: " + associate.getId());
            System.out.println("Name: " + associate.getName());
            System.out.println("Age: " + associate.getAge());
            System.out.println("Business Unit: " + associate.getBusinessUnit());
            System.out.println("Email: " + associate.getEmail());
            System.out.println("Location: " + associate.getLocation());
            System.out.println("Skills: " + String.join(", ", associate.getSkills()));
            System.out.println("Create Time: " + associate.getCreateTime());
            System.out.println("Update Time: " + associate.getUpdateTime());
        } else {
            System.out.println("Associate not found.");
        }
    }
}
package com.indium.skilltrackerapp.dao;

import com.indium.skilltrackerapp.model.Skill;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillDAOImpl implements SkillDAO {
    private final Connection connection;

    public SkillDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Skill findById(int id) {
        String sql = "SELECT * FROM skills WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToSkill(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Skill> findAll() {
        String sql = "SELECT * FROM skills";
        List<Skill> skills = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                skills.add(mapResultSetToSkill(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return skills;
    }

    @Override
    public int save(Skill skill) {
        String sql = "INSERT INTO skills (name, description, category, experience, location) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, skill.getName());
            statement.setString(2, skill.getDescription());
            statement.setString(3, skill.getCategory());
            statement.setInt(4, skill.getExperience());
            statement.setString(5, skill.getLocation());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return 0; // Insertion failed
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                skill.setId(generatedId); // Set the generated ID in the skill object
                return generatedId; // Return the generated ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean update(Skill skill) {
        String sql = "UPDATE skills SET name = ?, description = ?, category = ?, experience = ?, location = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, skill.getName());
            statement.setString(2, skill.getDescription());
            statement.setString(3, skill.getCategory());
            statement.setInt(4, skill.getExperience());
            statement.setString(5, skill.getLocation());
            statement.setInt(6, skill.getId());
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0; // Return true if the update is successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM skills WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0; // Return true if the deletion is successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String, Integer> getLocationWiseSkillCount() {
        String sql = "SELECT location, COUNT(*) as skill_count FROM skills GROUP BY location";
        Map<String, Integer> locationWiseSkillCount = new HashMap<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String location = resultSet.getString("location");
                int skillCount = resultSet.getInt("skill_count");
                locationWiseSkillCount.put(location, skillCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locationWiseSkillCount;
    }

    @Override
    public List<Skill> getTopNSkills(int n) {
        String sql = "SELECT * FROM skills ORDER BY experience DESC LIMIT ?";
        List<Skill> topSkills = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, n);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                topSkills.add(mapResultSetToSkill(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topSkills;
    }

    @Override
    public Map<String, Double> getSkillWiseAvgExperience() {
        String sql = "SELECT category, AVG(experience) as avg_experience FROM skills GROUP BY category";
        Map<String, Double> skillWiseAvgExperience = new HashMap<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String category = resultSet.getString("category");
                double avgExperience = resultSet.getDouble("avg_experience");
                skillWiseAvgExperience.put(category, avgExperience);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return skillWiseAvgExperience;
    }

    private Skill mapResultSetToSkill(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        String category = resultSet.getString("category");
        int experience = resultSet.getInt("experience");
        String location = resultSet.getString("location");
        return new Skill(id, name, description, category, experience, location);
    }
    
    @Override
    public boolean addSkillToAssociate(int associateId, Skill skill) {
        // Define SQL queries for inserting records into the skills and associate_skill tables
        String insertSkillQuery = "INSERT INTO skills (name, description, category, experience) VALUES (?, ?, ?, ?)";
        String insertAssociateSkillQuery = "INSERT INTO associate_skill (associate_id, skill_id) VALUES (?, ?)";

        // Declare PreparedStatement variables 
        PreparedStatement insertSkillStatement = null;
        PreparedStatement insertAssociateSkillStatement = null;

        try {
            // database connection setup
            String jdbcUrl = "jdbc:mysql://localhost:3306/skilltracker";
            String dbUser = "root";
            String dbPassword = "sriganesh@002";

            // Load the MySQL JDBC driver (if not already loaded)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Obtain a connection to the database
            Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);

            // Initialize PreparedStatement variables
            insertSkillStatement = connection.prepareStatement(insertSkillQuery,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            insertAssociateSkillStatement = connection.prepareStatement(insertAssociateSkillQuery);

            // Insert the skill into the skills table
            insertSkillStatement.setString(1, skill.getName());
            insertSkillStatement.setString(2, skill.getDescription());
            insertSkillStatement.setString(3, skill.getCategory());
            insertSkillStatement.setInt(4, skill.getExperience());

            int skillRowsAffected = insertSkillStatement.executeUpdate();

            if (skillRowsAffected == 1) {
                // Retrieve the generated skill ID
                int generatedSkillId = -1;
                ResultSet skillKeys = insertSkillStatement.getGeneratedKeys();
                if (skillKeys.next()) {
                    generatedSkillId = skillKeys.getInt(1);
                }

                // Insert a record into the associate_skill table to associate the skill with the associate
                insertAssociateSkillStatement.setInt(1, associateId);
                insertAssociateSkillStatement.setInt(2, generatedSkillId);

                int associateSkillRowsAffected = insertAssociateSkillStatement.executeUpdate();

                if (associateSkillRowsAffected == 1) {
                    // Both skill and association were added successfully
                    return true;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Handle any database-related exceptions here
        } finally {
            // Close PreparedStatement objects in a finally block to ensure they are always closed
            if (insertSkillStatement != null) {
                try {
                    insertSkillStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (insertAssociateSkillStatement != null) {
                try {
                    insertAssociateSkillStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // If any step fails, return false
        return false;
    }


    @Override
    public boolean addSkill(Skill skill) {
        String sql = "INSERT INTO skills (name, description, category, experience, location) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, skill.getName());
            statement.setString(2, skill.getDescription());
            statement.setString(3, skill.getCategory());
            statement.setInt(4, skill.getExperience());
            statement.setString(5, skill.getLocation());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if skill was added successfully
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Skill> searchSkillsByName(String name) {
        List<Skill> skills = new ArrayList<>();
        String sql = "SELECT * FROM skills WHERE name LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                skills.add(mapResultSetToSkill(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return skills;
    }
    
    @Override
    public List<Skill> searchSkillsByCategory(String category) {
        List<Skill> skills = new ArrayList<>();
        String sql = "SELECT * FROM skills WHERE category = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                skills.add(mapResultSetToSkill(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return skills;
    }
    
    @Override
    public List<Skill> searchSkillsByExperience(int minExperience, int maxExperience) {
        List<Skill> skills = new ArrayList<>();
        String sql = "SELECT * FROM skills WHERE experience >= ? AND experience <= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, minExperience);
            statement.setInt(2, maxExperience);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                skills.add(mapResultSetToSkill(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return skills;
    }

    @Override
    public List<Skill> getAllSkills() {
        List<Skill> skills = new ArrayList<>();
        String sql = "SELECT * FROM skills";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                skills.add(mapResultSetToSkill(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return skills;
    }
    
    @Override
    public Skill getSkillById(int id) {
        String sql = "SELECT * FROM skills WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToSkill(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    @Override
    public boolean updateSkill(Skill skill) {
        String sql = "UPDATE skills SET name = ?, description = ?, category = ?, experience = ?, location = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, skill.getName());
            statement.setString(2, skill.getDescription());
            statement.setString(3, skill.getCategory());
            statement.setInt(4, skill.getExperience());
            statement.setString(5, skill.getLocation());
            statement.setInt(6, skill.getId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if the skill was updated successfully
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteSkill(int id) {
        String sql = "DELETE FROM skills WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if the skill was deleted successfully
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
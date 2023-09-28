package com.indium.skilltrackerapp.dao;

import com.indium.skilltrackerapp.model.Associate;
import com.indium.skilltrackerapp.model.AssociateSkillCount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssociateDAOImpl implements AssociateDAO {
    private final Connection connection;

    public AssociateDAOImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public Associate findById(int id) {
        String sql = "SELECT * FROM associates WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToAssociate(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Associate> findAll() {
        String sql = "SELECT * FROM associates";
        List<Associate> associates = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                associates.add(mapResultSetToAssociate(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return associates;
    }

    @Override
    public int save(Associate associate) {
        String sql = "INSERT INTO associates (name, age, business_unit, email, location) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, associate.getName());
            statement.setInt(2, associate.getAge());
            statement.setString(3, associate.getBusinessUnit());
            statement.setString(4, associate.getEmail());
            statement.setString(5, associate.getLocation());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return -1; // Insertion failed
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // Return the generated ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Insertion failed
    }

    @Override
    public boolean update(Associate associate) {
        String sql = "UPDATE associates SET name = ?, age = ?, business_unit = ?, email = ?, location = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, associate.getName());
            statement.setInt(2, associate.getAge());
            statement.setString(3, associate.getBusinessUnit());
            statement.setString(4, associate.getEmail());
            statement.setString(5, associate.getLocation());
            statement.setInt(6, associate.getId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0; // Return true if the update is successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM associates WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0; // Return true if the deletion is successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<String> getSkillsForAssociate(int associateId) {
        List<String> skills = new ArrayList<>();
        String sql = "SELECT s.name FROM skills s " +
                     "INNER JOIN associate_skills a ON s.id = a.skill_id " +
                     "WHERE a.associate_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, associateId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String skillName = resultSet.getString("name");
                skills.add(skillName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return skills;
    }

    @Override
    public List<Integer> getAssociateIdsWithMoreThanNSkills(int n) {
        // this method retrieves associate IDs with more than N skills from the database
        List<Integer> associateIds = new ArrayList<>();
        String sql = "SELECT id FROM associates WHERE id IN (SELECT associate_id FROM associate_skills GROUP BY associate_id HAVING COUNT(skill_id) > ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, n);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                associateIds.add(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return associateIds;
    }

    @Override
    public int getAssociatesWithMoreThanNSkills(int n) {
        // this method counts associates with more than N skills in the database
        String sql = "SELECT COUNT(*) FROM associates WHERE id IN (SELECT associate_id FROM associate_skills GROUP BY associate_id HAVING COUNT(skill_id) > ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, n);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public int getAssociatesWithGivenSkills(String[] skills) {
        // this method counts associates with given skills in the database
        String sql = "SELECT COUNT(DISTINCT a.id) FROM associates a " +
                     "INNER JOIN associate_skills s ON a.id = s.associate_id " +
                     "INNER JOIN skills sk ON s.skill_id = sk.id " +
                     "WHERE sk.name IN (" + getPlaceholderString(skills.length) + ")";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < skills.length; i++) {
                statement.setString(i + 1, skills[i]);
            }
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public List<AssociateSkillCount> getAssociateSkillCounts() {
        // this method retrieves skill counts for each associate from the database
        List<AssociateSkillCount> associateSkillCounts = new ArrayList<>();
        String sql = "SELECT a.id AS associate_id, a.name AS associate_name, COUNT(s.id) AS skill_count " +
                     "FROM associates a " +
                     "LEFT JOIN associate_skills sa ON a.id = sa.associate_id " +
                     "LEFT JOIN skills s ON sa.skill_id = s.id " +
                     "GROUP BY a.id, a.name";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int associateId = resultSet.getInt("associate_id");
                String associateName = resultSet.getString("associate_name");
                int skillCount = resultSet.getInt("skill_count");
                associateSkillCounts.add(new AssociateSkillCount(associateId, skillCount, associateName, skillCount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return associateSkillCounts;
    }


    @Override
    public Map<String, Integer> getBuWiseAssociateCount() {
        // this method retrieves business unit-wise associate counts from the database
        Map<String, Integer> buWiseAssociateCount = new HashMap<>();
        String sql = "SELECT business_unit, COUNT(id) AS associate_count FROM associates GROUP BY business_unit";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String businessUnit = resultSet.getString("business_unit");
                int associateCount = resultSet.getInt("associate_count");
                buWiseAssociateCount.put(businessUnit, associateCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return buWiseAssociateCount;
    }

    @Override
    public Map<String, Integer> getSkillWiseAssociateCount() {
        // this method retrieves skill-wise associate counts from the database
        Map<String, Integer> skillWiseAssociateCount = new HashMap<>();
        String sql = "SELECT s.name AS skill_name, COUNT(sa.associate_id) AS associate_count " +
                     "FROM skills s " +
                     "LEFT JOIN associate_skills sa ON s.id = sa.skill_id " +
                     "GROUP BY s.name";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String skillName = resultSet.getString("skill_name");
                int associateCount = resultSet.getInt("associate_count");
                skillWiseAssociateCount.put(skillName, associateCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return skillWiseAssociateCount;
    }


    @Override
    public boolean deleteAssociate(int associateId) {
        // this method deletes an associate from the database
        String sql = "DELETE FROM associates WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, associateId);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0; // Return true if the deletion is successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Associate getAssociateById(int associateId) {
        // this method retrieves an associate by ID from the database
        String sql = "SELECT * FROM associates WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, associateId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToAssociate(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateAssociate(Associate associate) {
        // this method updates an associate in the database
        String sql = "UPDATE associates SET name = ?, age = ?, business_unit = ?, email = ?, location = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, associate.getName());
            statement.setInt(2, associate.getAge());
            statement.setString(3, associate.getBusinessUnit());
            statement.setString(4, associate.getEmail());
            statement.setString(5, associate.getLocation());
            statement.setInt(6, associate.getId());

            int affectedRows = statement.executeUpdate();
            // Handles the result as needed
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getTotalAssociateCount() {
        // this method gets the total count of associates from the database
        String sql = "SELECT COUNT(*) FROM associates";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    @Override
    public Associate addAssociate(String name, int age, String businessUnit, String email, String location, List<String> skills) {
        String sql = "INSERT INTO associates (name, age, business_unit, email, location) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, businessUnit);
            statement.setString(4, email);
            statement.setString(5, location);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return null; // Insertion failed
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int associateId = generatedKeys.getInt(1);

                // Insert skills for the associate
                if (skills != null && !skills.isEmpty()) {
                    insertSkillsForAssociate(associateId, skills);
                }

                return new Associate(associateId, name, age, businessUnit, email, location, skills);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Insertion failed
    }
    
    @Override
    public List<Associate> getAllAssociates() {
        // this method retrieves all associates from the database
        String sql = "SELECT * FROM associates";
        List<Associate> associates = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                associates.add(mapResultSetToAssociate(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return associates;
    }
    
	private String getPlaceholderString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("?");
            if (i < length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
	 // Helper method to map a ResultSet to an Associate object
    private Associate mapResultSetToAssociate(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int age = resultSet.getInt("age");
        String businessUnit = resultSet.getString("business_unit");
        String email = resultSet.getString("email");
        String location = resultSet.getString("location");

        // Retrieve and set associated skills 
        List<String> skills = getSkillsForAssociate(id); // 

        return new Associate(id, name, age, businessUnit, email, location, skills);
    }

    // Helper method to insert skills for an associate
    private void insertSkillsForAssociate(int associateId, List<String> skills) {
        String sql = "INSERT INTO associate_skills (associate_id, skill_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (String skill : skills) {
                int skillId = getSkillIdByName(skill);
                statement.setInt(1, associateId);
                statement.setInt(2, skillId);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to get skill ID by name
    private int getSkillIdByName(String skillName) {
        String sql = "SELECT id FROM skills WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, skillName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Skill not found
    }

	@Override
	public Associate addAssociate(String name, int age, String businessUnit, String email, String location,
			String[] skills) {
		
		return null;
	}
}
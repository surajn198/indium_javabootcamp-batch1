package com.indium.skilltrackerapp.service;

import com.indium.skilltrackerapp.dao.SkillDAO;
import com.indium.skilltrackerapp.model.Skill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillServiceImpl implements SkillService {
    private final SkillDAO skillDAO;

    public SkillServiceImpl(SkillDAO skillDAO) {
        this.skillDAO = skillDAO;
    }

    @Override
    public boolean addSkill(Skill skill) {
        // Implement the SkillDAO method for adding a skill
        return skillDAO.addSkill(skill);
    }

    @Override
    public List<Skill> getAllSkills() {
        // Implement the SkillDAO method for fetching all skills
        return skillDAO.getAllSkills();
    }

    @Override
    public Skill getSkillById(int id) {
        // Implement the SkillDAO method for fetching a skill by ID
        return skillDAO.getSkillById(id);
    }

    @Override
    public boolean updateSkill(Skill skill) {
        // Implement the SkillDAO method for updating a skill
        return skillDAO.updateSkill(skill);
    }

    @Override
    public boolean deleteSkill(int id) {
        // Implement the SkillDAO method for deleting a skill
        return skillDAO.deleteSkill(id);
    }

    @Override
    public Map<String, Integer> getLocationWiseSkillCount() {
        Map<String, Integer> locationWiseSkillCount = new HashMap<>();
        List<Skill> skills = skillDAO.getAllSkills();

        for (Skill skill : skills) {
            String location = skill.getLocation();
            locationWiseSkillCount.put(location, locationWiseSkillCount.getOrDefault(location, 0) + 1);
        }
        return locationWiseSkillCount;
    }

    @Override
    public Map<String, Integer> getSkillWiseAssociateCount() {
        Map<String, Integer> skillWiseAssociateCount = new HashMap<>();
        List<Skill> skills = skillDAO.getAllSkills();

        for (Skill skill : skills) {
            List<String> skillNames = skill.getSkills();

            for (String skillName : skillNames) {
                skillWiseAssociateCount.put(skillName, skillWiseAssociateCount.getOrDefault(skillName, 0) + 1);
            }
        }
        return skillWiseAssociateCount;
    }

    @Override
    public List<Skill> searchSkillsByName(String name) {
        List<Skill> result = new ArrayList<>();
        List<Skill> skills = skillDAO.getAllSkills();

        for (Skill skill : skills) {
            if (skill.getName().equalsIgnoreCase(name)) {
                result.add(skill);
            }
        }
        return result;
    }

    @Override
    public List<Skill> searchSkillsByCategory(String category) {
        List<Skill> result = new ArrayList<>();
        List<Skill> skills = skillDAO.getAllSkills();

        for (Skill skill : skills) {
            if (skill.getCategory().equalsIgnoreCase(category)) {
                result.add(skill);
            }
        }
        return result;
    }

    @Override
    public List<Skill> searchSkillsByExperience(int minExperience, int maxExperience) {
        List<Skill> result = new ArrayList<>();
        List<Skill> skills = skillDAO.getAllSkills();

        for (Skill skill : skills) {
            if (skill.getExperience() >= minExperience && skill.getExperience() <= maxExperience) {
                result.add(skill);
            }
        }
        return result;
    }

    @Override
    public List<Skill> searchSkillsByMultipleCriteria(String name, String category, int minExperience, int maxExperience) {
        List<Skill> result = new ArrayList<>();
        List<Skill> skills = skillDAO.getAllSkills();

        for (Skill skill : skills) {
            boolean nameMatch = name == null || name.isEmpty() || skill.getName().equalsIgnoreCase(name);
            boolean categoryMatch = category == null || category.isEmpty() || skill.getCategory().equalsIgnoreCase(category);
            boolean experienceMatch = skill.getExperience() >= minExperience && skill.getExperience() <= maxExperience;

            if (nameMatch && categoryMatch && experienceMatch) {
                result.add(skill);
            }
        }
        return result;
    }

    @Override
    public List<Skill> getTopNSkills(int n) {
        List<Skill> skills = skillDAO.getAllSkills();
        skills.sort((s1, s2) -> Integer.compare(s2.getExperience(), s1.getExperience())); // Sort by experience in descending order
        return skills.subList(0, Math.min(n, skills.size())); // Return the top N skills or all skills if there are fewer than N
    }

    @Override
    public Map<String, Double> getSkillWiseAvgExperience() {
        Map<String, Double> skillWiseAvgExperience = new HashMap<>();
        List<Skill> skills = skillDAO.getAllSkills();

        for (Skill skill : skills) {
            String skillName = skill.getName();
            int experience = skill.getExperience();

            if (skillWiseAvgExperience.containsKey(skillName)) {
                double currentAvg = skillWiseAvgExperience.get(skillName);
                Double count = skillWiseAvgExperience.get(skillName); // Get the count from the map
                double newAvg = (currentAvg * count + experience) / (count + 1);
                skillWiseAvgExperience.put(skillName, newAvg);
            } else {
                skillWiseAvgExperience.put(skillName, (double) experience);
            }
        }
        return skillWiseAvgExperience;
    }

	@Override
	public void updateSkill(int id, String name, String description, String category, int experience) {
		// TODO Auto-generated method stub
		
	}
}
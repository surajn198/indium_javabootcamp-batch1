package com.indium.skilltrackerapp.service;

import com.indium.skilltrackerapp.dao.AssociateDAO;
import com.indium.skilltrackerapp.model.Associate;
import com.indium.skilltrackerapp.model.AssociateSkillCount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssociateServiceImpl implements AssociateService {
    private final AssociateDAO associateDAO;

    public AssociateServiceImpl(AssociateDAO associateDAO) {
        this.associateDAO = associateDAO;
    }

    @Override
    public Associate addAssociate(String name, int age, String businessUnit, String email, String location, String[] skills) {
        Associate associate = new Associate(0, name, age, businessUnit, email, location, List.of(skills));
        int id = associateDAO.save(associate);
        if (id > 0) {
            return associate;
        }
        return null;
    }


    @Override
    public List<Associate> getAllAssociates() {
        return associateDAO.findAll();
    }

    @Override
    public Associate getAssociateById(int id) {
        return associateDAO.findById(id);
    }

    @Override
    public boolean updateAssociate(int id, String name, int age, String businessUnit, String email, String location) {
        Associate associate = associateDAO.findById(id);
        if (associate != null) {
            associate.setName(name);
            associate.setAge(age);
            associate.setBusinessUnit(businessUnit);
            associate.setEmail(email);
            associate.setLocation(location);
            return associateDAO.update(associate);
        }
        return false;
    }

    @Override
    public void deleteAssociate(int id) {
        associateDAO.delete(id);
    }

    @Override
    public void addSkillsToAssociate(int id, String[] skills) {
        Associate associate = associateDAO.findById(id);
        if (associate != null) {
            for (String skill : skills) {
                associate.addSkill(skill);
            }
            associateDAO.update(associate);
        }
    }


    @Override
    public void updateSkill(int associateId, int skillId, String name, String description, String category, int experience) {
        // Since skills are stored as strings in Associate class, we'll update the skill name directly
        Associate associate = associateDAO.findById(associateId);
        if (associate != null) {
            List<String> skills = associate.getSkills();
            if (skillId >= 0 && skillId < skills.size()) {
                skills.set(skillId, name);
                associateDAO.update(associate);
            }
        }
    }
    
    @Override
    public void deleteSkill(int associateId, int skillId) {
        // Since skills are stored as strings in Associate class, we'll remove the skill name by index
        Associate associate = associateDAO.findById(associateId);
        if (associate != null) {
            List<String> skills = associate.getSkills();
            if (skillId >= 0 && skillId < skills.size()) {
                skills.remove(skillId);
                associateDAO.update(associate);
            }
        }
    }

    @Override
    public List<Associate> searchAssociatesByName(String name) {
        // search logic based on the given name
        List<Associate> result = new ArrayList<>();
        List<Associate> associates = associateDAO.findAll();

        for (Associate associate : associates) {
            if (associate.getName().equalsIgnoreCase(name)) {
                result.add(associate);
            }
        }

        return result;
    }

    @Override
    public List<Associate> searchAssociatesByLocation(String location) {
        //  search logic based on the given location
        List<Associate> result = new ArrayList<>();
        List<Associate> associates = associateDAO.findAll();

        for (Associate associate : associates) {
            if (associate.getLocation().equalsIgnoreCase(location)) {
                result.add(associate);
            }
        }

        return result;
    }

    @Override
    public List<Associate> searchAssociatesBySkills(String[] skills) {
        //  search logic based on the given skills
        List<Associate> result = new ArrayList<>();
        List<Associate> associates = associateDAO.findAll();

        for (Associate associate : associates) {
            List<String> associateSkills = associate.getSkills();
            boolean foundAllSkills = true;

            for (String skillName : skills) {
                if (!associateSkills.contains(skillName)) {
                    foundAllSkills = false;
                    break;
                }
            }

            if (foundAllSkills) {
                result.add(associate);
            }
        }

        return result;
    }

    @Override
    public List<Associate> searchAssociatesByMultipleCriteria(String name, String location, String[] skills) {
        //  search logic based on multiple criteria
        List<Associate> result = new ArrayList<>();
        List<Associate> associates = associateDAO.findAll();

        for (Associate associate : associates) {
            boolean nameMatch = name == null || name.isEmpty() || associate.getName().equalsIgnoreCase(name);
            boolean locationMatch = location == null || location.isEmpty() || associate.getLocation().equalsIgnoreCase(location);
            boolean skillsMatch = skills == null || skills.length == 0 || associate.hasAllSkills(skills);

            if (nameMatch && locationMatch && skillsMatch) {
                result.add(associate);
            }
        }

        return result;
    }


    @Override
    public int getTotalAssociateCount() {
        return associateDAO.findAll().size();
    }

    @Override
    public int getAssociatesWithMoreThanNSkills(int n) {
        int count = 0;
        List<Associate> associates = associateDAO.findAll();

        for (Associate associate : associates) {
            if (associate.getSkills().size() > n) {
                count++;
            }
        }

        return count;
    }

    @Override
    public List<Integer> getAssociateIdsWithMoreThanNSkills(int n) {
        List<Integer> result = new ArrayList<>();
        List<Associate> associates = associateDAO.findAll();

        for (Associate associate : associates) {
            if (associate.getSkills().size() > n) {
                result.add(associate.getId());
            }
        }

        return result;
    }

    @Override
    public int getAssociatesWithGivenSkills(String[] skills) {
        int count = 0;
        List<Associate> associates = associateDAO.findAll();

        for (Associate associate : associates) {
            if (associate.hasAllSkills(skills)) {
                count++;
            }
        }

        return count;
    }

    @Override
    public List<AssociateSkillCount> getAssociateSkillCounts() {
        List<AssociateSkillCount> result = new ArrayList<>();
        List<Associate> associates = associateDAO.findAll();

        for (Associate associate : associates) {
            AssociateSkillCount skillCount = new AssociateSkillCount(associate.getId(), associate.getSkills().size(), associate.getName(), associate.getSkills().size());
            result.add(skillCount);
        }
        return result;
    }

    @Override
    public Map<String, Integer> getBuWiseAssociateCount() {
        Map<String, Integer> result = new HashMap<>();
        List<Associate> associates = associateDAO.findAll();

        for (Associate associate : associates) {
            String bu = associate.getBusinessUnit();
            result.put(bu, result.getOrDefault(bu, 0) + 1);
        }

        return result;
    }

    @Override
    public Map<String, Integer> getSkillWiseAssociateCount() {
        Map<String, Integer> result = new HashMap<>();
        List<Associate> associates = associateDAO.findAll();

        for (Associate associate : associates) {
            for (String skill : associate.getSkills()) {
                result.put(skill, result.getOrDefault(skill, 0) + 1);
            }
        }

        return result;
    }

	@Override
	public Map<String, Double> getSkillWiseAvgExperience() {
		// TODO Auto-generated method stub
		return null;
	}
}
package com.indium.skilltrackerapp.service;

import java.util.List;
import java.util.Map;

import com.indium.skilltrackerapp.model.Skill;

public interface SkillService {
    List<Skill> getAllSkills();
    Skill getSkillById(int id);
    boolean addSkill(Skill skill);
    boolean updateSkill(Skill skill);
    boolean deleteSkill(int id);
    void updateSkill(int id, String name, String description, String category, int experience);
    List<Skill> searchSkillsByName(String name);
    List<Skill> searchSkillsByCategory(String category);
    List<Skill> searchSkillsByExperience(int minExperience, int maxExperience);
    List<Skill> searchSkillsByMultipleCriteria(String name, String category, int minExperience, int maxExperience);
    List<Skill> getTopNSkills(int n);
    Map<String, Integer> getLocationWiseSkillCount();
    Map<String, Double> getSkillWiseAvgExperience();
    Map<String, Integer> getSkillWiseAssociateCount();
}
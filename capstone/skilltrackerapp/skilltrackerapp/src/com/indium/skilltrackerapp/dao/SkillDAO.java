package com.indium.skilltrackerapp.dao;

import com.indium.skilltrackerapp.model.Skill;

import java.util.List;
import java.util.Map;

public interface SkillDAO {
    Skill findById(int id);
    List<Skill> findAll();
    int save(Skill skill);
    boolean update(Skill skill);
    boolean delete(int id);
    Map<String, Integer> getLocationWiseSkillCount();
    List<Skill> getTopNSkills(int n);
    Map<String, Double> getSkillWiseAvgExperience();
    boolean addSkill(Skill skill);
    List<Skill> searchSkillsByName(String name);
    List<Skill> searchSkillsByCategory(String category);
    List<Skill> searchSkillsByExperience(int minExperience, int maxExperience);
	List<Skill> getAllSkills();
	Skill getSkillById(int id);
	boolean updateSkill(Skill skill);
	boolean deleteSkill(int id);
	boolean addSkillToAssociate(int associateId, Skill skill);
}
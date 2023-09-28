package com.indium.skilltrackerapp.dao;

import com.indium.skilltrackerapp.model.Associate;
import com.indium.skilltrackerapp.model.AssociateSkillCount;
import com.indium.skilltrackerapp.model.Skill;

import java.util.List;
import java.util.Map;

public interface AssociateDAO {
    Associate findById(int id);
    List<Associate> findAll();
    int save(Associate associate);
    boolean update(Associate associate);
    boolean delete(int id);
    List<Integer> getAssociateIdsWithMoreThanNSkills(int n);
    int getAssociatesWithMoreThanNSkills(int n);
    int getAssociatesWithGivenSkills(String[] skills);
    List<AssociateSkillCount> getAssociateSkillCounts();
    Map<String, Integer> getBuWiseAssociateCount();
    Map<String, Integer> getSkillWiseAssociateCount();
	boolean deleteAssociate(int associateId);
	Associate getAssociateById(int associateId);
	void updateAssociate(Associate associate);
	int getTotalAssociateCount();
	Associate addAssociate(String name, int age, String businessUnit, String email, String location, String[] skills);
	List<Associate> getAllAssociates();
	Associate addAssociate(String name, int age, String businessUnit, String email, String location,
	List<String> skills);
}
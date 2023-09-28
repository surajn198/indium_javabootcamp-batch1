package com.indium.skilltrackerapp.service;

import java.util.List;
import java.util.Map;

import com.indium.skilltrackerapp.model.Associate;
import com.indium.skilltrackerapp.model.AssociateSkillCount;

public interface AssociateService {
    Associate addAssociate(String name, int age, String businessUnit, String email, String location, String[] skills);
    List<Associate> getAllAssociates();
    Associate getAssociateById(int id);
    boolean updateAssociate(int id, String name, int age, String businessUnit, String email, String location);
    void deleteAssociate(int id);
    void addSkillsToAssociate(int id, String[] skills);
    void updateSkill(int associateId, int skillId, String name, String description, String category, int experience);
    void deleteSkill(int associateId, int skillId);
    List<Associate> searchAssociatesByName(String name);
    List<Associate> searchAssociatesByLocation(String location);
    List<Associate> searchAssociatesBySkills(String[] skills);
    List<Associate> searchAssociatesByMultipleCriteria(String name, String location, String[] skills);
    int getTotalAssociateCount();
    int getAssociatesWithMoreThanNSkills(int n);
    List<Integer> getAssociateIdsWithMoreThanNSkills(int n);
    int getAssociatesWithGivenSkills(String[] skills);
    List<AssociateSkillCount> getAssociateSkillCounts();
    Map<String, Integer> getBuWiseAssociateCount();
    Map<String, Integer> getSkillWiseAssociateCount();
    Map<String, Double> getSkillWiseAvgExperience();
}
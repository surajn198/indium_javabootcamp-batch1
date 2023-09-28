package com.indium.skilltrackerapp.model;

import java.util.HashMap;
import java.util.Map;

public class AssociateSkillCount {
    private int associateId;
    private int skillCount;
    private String associateName;
    private Map<String, Integer> skillCountMap;

    public AssociateSkillCount(int associateId, int skillCount, String associateName, int initialCount) {
        this.associateId = associateId;
        this.skillCount = skillCount;
        this.associateName = associateName;
        this.skillCountMap = new HashMap<>();
        this.skillCountMap.put("TotalSkills", initialCount);
    }

    public int getSkillCount() {
        return skillCount;
    }

    public void setAssociateId(int associateId) {
        this.associateId = associateId;
    }

    public void setAssociateName(String associateName) {
        this.associateName = associateName;
    }

    public void setSkillCountMap(Map<String, Integer> skillCountMap) {
        this.skillCountMap = skillCountMap;
    }

    public void setSkillCount(int skillCount) {
        this.skillCount = skillCount;
    }

    public int getAssociateId() {
        return associateId;
    }

    public String getAssociateName() {
        return associateName;
    }

    public int getSkillCount(String skillName) {
        return skillCountMap.getOrDefault(skillName, 0);
    }

    public void incrementSkillCount(String skillName) {
        int currentCount = skillCountMap.getOrDefault(skillName, 0);
        skillCountMap.put(skillName, currentCount + 1);
        // Increment the total skill count as well
        int totalSkills = skillCountMap.getOrDefault("TotalSkills", 0);
        skillCountMap.put("TotalSkills", totalSkills + 1);
    }

    public int getTotalSkillCount() {
        return skillCountMap.getOrDefault("TotalSkills", 0);
    }

    public void setSkillCount(String skillName, int count) {
        skillCountMap.put(skillName, count);
    }

    public void setTotalSkillCount(int count) {
        skillCountMap.put("TotalSkills", count);
    }

    public Map<String, Integer> getSkillCountMap() {
        return skillCountMap;
    }

    @Override
    public String toString() {
        return "AssociateSkillCount{" +
                "associateId=" + associateId +
                ", associateName='" + associateName + '\'' +
                ", skillCountMap=" + skillCountMap +
                '}';
    }
}
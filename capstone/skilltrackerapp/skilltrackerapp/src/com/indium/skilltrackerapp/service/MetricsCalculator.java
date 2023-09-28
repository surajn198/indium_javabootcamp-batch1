package com.indium.skilltrackerapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.indium.skilltrackerapp.model.Associate;
import com.indium.skilltrackerapp.model.Skill;

public class MetricsCalculator {
    private SkillService skillService;
    private AssociateService associateService;

    public MetricsCalculator(SkillService skillService, AssociateService associateService) {
        this.skillService = skillService;
        this.associateService = associateService;
    }

    public int getTotalAssociates() {
        return associateService.getAllAssociates().size();
    }

    public int getTotalAssociatesWithSkills(int minSkills) {
        List<Associate> associates = associateService.getAllAssociates();
        int count = 0;
        for (Associate associate : associates) {
            if (associate.getSkills().size() >= minSkills) {
                count++;
            }
        }
        return count;
    }

    public List<Integer> getAssociateIdsWithSkills(int minSkills) {
        List<Integer> associateIds = new ArrayList<>();
        List<Associate> associates = associateService.getAllAssociates();
        for (Associate associate : associates) {
            if (associate.getSkills().size() >= minSkills) {
                associateIds.add(associate.getId());
            }
        }
        return associateIds;
    }

    public int getTotalAssociatesWithGivenSkills(List<String> skills) {
        List<Associate> associates = associateService.getAllAssociates();
        int count = 0;
        for (Associate associate : associates) {
            if (associate.getSkills().containsAll(skills)) {
                count++;
            }
        }
        return count;
    }

    public Map<String, Integer> getAssociateSkillCounts() {
        return skillService.getSkillWiseAssociateCount();
    }

    public Map<String, Integer> getBusinessUnitAssociateCounts() {
        return associateService.getBuWiseAssociateCount();
    }

    public Map<String, Integer> getLocationSkillCounts() {
        return skillService.getLocationWiseSkillCount();
    }

    public Map<String, Integer> getSkillAssociateCounts() {
        return associateService.getSkillWiseAssociateCount();
    }

   // Calculate top N skills (Primary vs Secondary)
    public List<String> getTopNSkills(int n) {
        List<Skill> skills = skillService.getTopNSkills(n);
        // Extract skill names from the Skill objects and return them as a list of strings.
        List<String> skillNames = new ArrayList<>();
        for (Skill skill : skills) {
            skillNames.add(skill.getName());
        }
        return skillNames;
    }


    // Calculate skill-wise average associate experience
    public Map<String, Double> getSkillWiseAvgExperience() {
        return associateService.getSkillWiseAvgExperience();
    }

}
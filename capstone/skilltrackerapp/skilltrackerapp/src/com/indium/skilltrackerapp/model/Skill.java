package com.indium.skilltrackerapp.model;

import java.util.ArrayList;
import java.util.List;

public class Skill {
    private int id;
    private String name;
    private String description;
    private String category;
    private int experience;
    private String location;
    private int associateId;
    private List<String> skills;

    public Skill() {
        this.skills = new ArrayList<>();
    }

    public Skill(int id, String name, String description, String category, int experience, String location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.experience = experience;
        this.location = location;
        this.skills = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getAssociateId() {
        return associateId;
    }

    public void setAssociateId(int associateId) {
        this.associateId = associateId;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public void addSkill(String skill) {
        skills.add(skill);
    }

    public void removeSkill(String skill) {
        skills.remove(skill);
    }

    @Override
    public String toString() {
        return "Skill ID: " + id +
               "\nName: " + name +
               "\nDescription: " + description +
               "\nCategory: " + category +
               "\nExperience: " + experience +
               "\nLocation: " + location +
               "\nAssociate ID: " + associateId +
               "\nSkills: " + skills;
    }
}
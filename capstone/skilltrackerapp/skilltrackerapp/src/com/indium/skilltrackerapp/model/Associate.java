package com.indium.skilltrackerapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.indium.skilltrackerapp.dao.AssociateDAO;

public class Associate {
    private int id;
    private String name;
    private int age;
    private String businessUnit;
    private String email;
    private String location;
    private List<String> skills; // Change from String[] to List<String>
    private Date createTime; //createTime field
    private Date updateTime; // updateTime field

    // Constructors

    public Associate(int id, String name, int age, String businessUnit, String email, String location, List<String> skills) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.businessUnit = businessUnit;
        this.email = email;
        this.location = location;
        this.skills = skills; // Assign the List<String> directly
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	// Getters and setters
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public void addSkill(String skill) {
        if (skills == null) {
            skills = new ArrayList<>();
        }
        skills.add(skill);
    }
    public void addSkillsToAssociate(int id, String[] skills, AssociateDAO associateDAO) {
        Associate associate = associateDAO.findById(id);
        if (associate != null) {
            for (String skill : skills) {
                associate.addSkill(skill);
            }
            associateDAO.update(associate);
        }
    }
    public boolean hasAllSkills(String[] requiredSkills) {
        List<String> associateSkills = getSkills();
        for (String skill : requiredSkills) {
            if (!associateSkills.contains(skill)) {
                return false; // If any required skill is not found, return false
            }
        }
        return true; // All required skills are found
    }

    // toString method for string representation

    @Override
    public String toString() {
        return "Associate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", businessUnit='" + businessUnit + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", skills=" + skills +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
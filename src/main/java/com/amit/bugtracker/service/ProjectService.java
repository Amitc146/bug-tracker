package com.amit.bugtracker.service;

import com.amit.bugtracker.entity.Project;
import com.amit.bugtracker.entity.User;

import java.util.List;

public interface ProjectService {

    List<Project> findAll();

    List<Project> findAllByUser(User user);

    List<Project> findAllByName(String name);

    List<Project> findAllByUserAndName(User user, String name);

    Project findById(Integer id);

    void save(Project project);

    void deleteById(Integer id);

}

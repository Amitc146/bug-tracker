package com.amit.bugtracker.service;

import com.amit.bugtracker.dao.ProjectRepository;
import com.amit.bugtracker.entity.Project;
import com.amit.bugtracker.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project findById(Integer id) {
        Optional<Project> result = projectRepository.findById(id);
        if (!result.isPresent())
            throw new RuntimeException("Did not find project id - " + id);
        return result.get();
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public List<Project> findAllByUser(User user) {
        return projectRepository.findAllByUsersContains(user);
    }

    @Override
    public List<Project> findAllAllowedByUser(User user) {
        if (user.isAdmin() || user.isManager())
            return findAll();

        return findAllByUser(user);
    }

    @Override
    public List<Project> findAllByName(String name) {
        return projectRepository.findAllByNameIsContaining(name);
    }

    @Override
    public List<Project> findAllByUserAndName(User user, String name) {
        if (name == null || name.isEmpty() || name.trim().isEmpty())
            return null;

        return projectRepository.findAllByUsersContainsAndNameIsContaining(user, name);
    }

    @Override
    public void save(Project project) {
        projectRepository.save(project);
    }

    @Override
    public void deleteById(Integer id) {
        projectRepository.deleteById(id);
    }

}

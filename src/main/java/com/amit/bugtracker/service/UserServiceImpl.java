package com.amit.bugtracker.service;

import com.amit.bugtracker.dao.UserRepository;
import com.amit.bugtracker.dto.ChartData;
import com.amit.bugtracker.entity.Role;
import com.amit.bugtracker.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public User findById(Integer id) {
        Optional<User> result = userRepository.findById(id);
        if (!result.isPresent())
            throw new RuntimeException("Could not find user id - " + id);
        return result.get();
    }

    @Override
    public Set<User> findByName(String name) {
        Set<User> users = new HashSet<>();
        String[] names = name.split("\\s+");
        for (String tempString : names) {
            users.addAll(userRepository.findAllByFirstNameIsContaining(tempString));
            users.addAll(userRepository.findAllByLastNameIsContaining(tempString));
            users.addAll(userRepository.findAllByUserNameIsContaining(tempString));
        }
        return users;
    }

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName);
        if (user == null)
            throw new UsernameNotFoundException("Invalid username or password.");

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<ChartData> getProjectsCount() {
        return userRepository.getProjectsCount();
    }

}


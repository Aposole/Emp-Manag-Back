package com.example.demo.service;

import com.example.demo.models.Admin;
import com.example.demo.repo.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class AdminDetailsServiceImpl implements UserDetailsService {

    private final AdminRepository adminRepository;

    public AdminDetailsServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(admin.getUsername(), admin.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));  // Assuming all admins have the ADMIN role
    }
}

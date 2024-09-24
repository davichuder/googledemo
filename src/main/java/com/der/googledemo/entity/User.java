package com.der.googledemo.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.der.googledemo.enums.RoleEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Entity
@Data
@NoArgsConstructor
public class User implements UserDetails, Principal{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    private String password;
    private String openId;

    @Enumerated(EnumType.STRING)
	private RoleEnum role;

    private boolean accountLocked;
    private boolean enabled;
    private boolean credentialsExpired;

    public User(String openId){
        this.openId = openId;
        this.role = RoleEnum.ROLE_CLIENT;
        this.accountLocked = false;
        this.enabled = true;
        this.credentialsExpired = false;
    }

    public User(String email, String password){
        this.email = email;
        this.password = password;
        this.role = RoleEnum.ROLE_CLIENT;
        this.accountLocked = false;
        this.enabled = true;
        this.credentialsExpired = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return email;
    }
}
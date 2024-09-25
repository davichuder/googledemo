package com.der.googledemo.entity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

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
public class User implements UserDetails, Principal, OAuth2User {
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

    public User(String openId) {
        this.openId = openId;
        this.role = RoleEnum.ROLE_CLIENT;
        this.accountLocked = false;
        this.enabled = true;
        this.credentialsExpired = false;
    }

    public User(String email, String password) {
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

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", id);
        attributes.put("email", email);
        attributes.put("role", role.toString());
        attributes.put("accountLocked", accountLocked);
        attributes.put("enabled", enabled);
        attributes.put("credentialsExpired", credentialsExpired);
        return attributes;
    }
}
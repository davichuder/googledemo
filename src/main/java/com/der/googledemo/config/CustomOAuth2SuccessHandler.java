package com.der.googledemo.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.der.googledemo.entity.User;
import com.der.googledemo.payload.response.JwtResponse;
import com.der.googledemo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String openId = oAuth2User.getAttribute("sub");
        User user = userRepository.findByOpenId(openId);
        if (user == null) {
            user = new User(openId);
            userRepository.save(user);
        }
        String accessToken = jwtUtil.generateAccessToken(user);
        Long access_expires_in = jwtUtil.getAccessTokenDuration();
        String refreshToken = jwtUtil.generateAccessToken(user);
        Long refresh_expires_in = jwtUtil.getRefreshTokenDuration();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JwtResponse jwtResponse = new JwtResponse(accessToken, access_expires_in, refreshToken, refresh_expires_in);
        response.getWriter().write(new ObjectMapper().writeValueAsString(jwtResponse));
    }
}
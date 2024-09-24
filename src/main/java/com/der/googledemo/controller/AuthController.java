package com.der.googledemo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.der.googledemo.config.JwtUtil;
import com.der.googledemo.entity.User;
import com.der.googledemo.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/auth/")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/run")
    public String getRun() {
        return "run";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String email, @RequestParam String password) {
        // Aquí puedes agregar lógica para validar que el email no exista ya
        userService.createUserWithEmail(email, password);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        try {
            UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(email,
                    password);
            // Autenticamos el usuario
            Authentication authentication = authenticationManager.authenticate(credentials);

            // Si la autenticación es correcta, generamos el token JWT
            User user = (User) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(user);
            logger.info("JWT: generado", jwt);

            return ResponseEntity.ok(jwt);
        } catch (BadCredentialsException e) {
            // Este caso manejará credenciales incorrectas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        } catch (Exception e) {
            // Si ocurre otro tipo de error
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/login-with-google")
    public String loginWithGoogle(OAuth2AuthenticationToken authentication) {
        String openId = authentication.getPrincipal().getAttribute("sub");
        User user = userService.findByOpenId(openId);

        if (user == null) {
            // Crear el usuario si no existe
            userService.createUserWithOpenId(openId);
            user = userService.findByOpenId(openId);
        }

        // Generar JWT
        String jwt = jwtUtil.generateToken(user);
        return "Usuario autenticado con Google: " + jwt;
    }
}

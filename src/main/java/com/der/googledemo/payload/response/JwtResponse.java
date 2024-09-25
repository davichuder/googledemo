package com.der.googledemo.payload.response;

public record JwtResponse(String accessToken, Long accessExpiresEn, String refreshToken, Long refreshExpiresIn) {
}

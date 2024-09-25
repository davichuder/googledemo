package com.der.googledemo.payload.response;

public record RefreshTokenResponse(String refreshToken, Long expiresIn) {
}

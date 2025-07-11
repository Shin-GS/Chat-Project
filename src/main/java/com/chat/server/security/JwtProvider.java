package com.chat.server.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.Constants;
import com.chat.server.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtProperties jwtProperties;

    public String createToken(Long userId, String username) {
        checkUserInfo(userId, username);
        Instant now = Instant.now();
        Instant expiresAt = now.plus(jwtProperties.getTokenTime(), ChronoUnit.MINUTES);
        return JWT.create()
                .withClaim(Constants.JWT_TOKEN_TYPE, Constants.JWT_TOKEN_TYPE_ACCESS_TOKEN)
                .withClaim(Constants.JWT_USER_ID, userId)
                .withClaim(Constants.JWT_USER_NAME, username)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expiresAt))
                .sign(Algorithm.HMAC256(jwtProperties.getSecretKey()));
    }

    public String createRefreshToken(Long userId, String username) {
        checkUserInfo(userId, username);
        Instant now = Instant.now();
        Instant expiresAt = now.plus(jwtProperties.getRefreshTokenTime(), ChronoUnit.MINUTES);
        return JWT.create()
                .withClaim(Constants.JWT_TOKEN_TYPE, Constants.JWT_TOKEN_TYPE_REFRESH_TOKEN)
                .withClaim(Constants.JWT_USER_ID, userId)
                .withClaim(Constants.JWT_USER_NAME, username)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expiresAt))
                .sign(Algorithm.HMAC256(jwtProperties.getRefreshSecretKey()));
    }

    public void checkUserInfo(Long userId, String username) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
    }

    // Refresh 요청 전에 Access Token 이 실제 만료되었는지 확인.
    // 만료된 경우에만 디코딩하여 반환. 유효하거나 조작된 경우 예외 발생.
    public DecodedJWT checkTokenForRefresh(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(jwtProperties.getSecretKey()))
                    .build()
                    .verify(token);

            log.warn("Access token is not expired. userId: {}, username: {}",
                    decodedJWT.getClaim(Constants.JWT_USER_ID).asLong(),
                    JwtUtils.maskSubject(decodedJWT.getClaim(Constants.JWT_USER_NAME).asString())
            );
            throw new CustomException(ErrorCode.TOKEN_NOT_EXPIRED);

        } catch (AlgorithmMismatchException | SignatureVerificationException | InvalidClaimException e) {
            log.error("Access token verification failed due to algorithm/signature/claim issue", e);
            throw new CustomException(ErrorCode.TOKEN_INVALID);

        } catch (TokenExpiredException e) {
            return JWT.decode(token); // Token is correctly expired

        } catch (Exception e) {
            log.error("Unexpected error occurred while verifying access token", e);
            throw new CustomException(ErrorCode.TOKEN_INVALID);
        }
    }

    // 서명 검증 후 AccessToken 디코딩
    public DecodedJWT decodeAccessToken(String token) {
        return verifyAndDecodeToken(token, jwtProperties.getSecretKey());
    }

    // 서명 검증 후 RefreshToken 디코딩
    public DecodedJWT decodeRefreshToken(String token) {
        return verifyAndDecodeToken(token, jwtProperties.getRefreshSecretKey());
    }

    // 검증 서명 후 사용자 ID 조회
    public Long getUserIdFromToken(String token) {
        DecodedJWT decodedJWT = decodeAccessToken(token);
        return decodedJWT.getClaim(Constants.JWT_USER_ID).asLong();
    }

    // 검증 서명 후 사용자 이름 조회
    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = decodeAccessToken(token);
        return decodedJWT.getClaim(Constants.JWT_USER_NAME).asString();
    }

    // 서명 검증을 포함한 디코딩. 유효하지 않거나 만료된 경우 예외 발생.
    private DecodedJWT verifyAndDecodeToken(String token, String secretKey) {
        try {
            return JWT.require(Algorithm.HMAC256(secretKey))
                    .build()
                    .verify(token);

        } catch (AlgorithmMismatchException | SignatureVerificationException | InvalidClaimException e) {
            log.error("Token verification failed due to algorithm/signature/claim issue", e);
            throw new CustomException(ErrorCode.TOKEN_INVALID);

        } catch (TokenExpiredException e) {
            log.error("Token is expired", e);
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);

        } catch (Exception e) {
            log.error("Unexpected error occurred while verifying token", e);
            throw new CustomException(ErrorCode.TOKEN_INVALID);
        }
    }

    // 서명 검증 없이 JWT 디코딩 (Claim 만 필요할 때 사용)
    public DecodedJWT decodeJWT(String token) {
        return JWT.decode(token);
    }
}

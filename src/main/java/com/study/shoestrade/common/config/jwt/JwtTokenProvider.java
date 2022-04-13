package com.study.shoestrade.common.config.jwt;

import com.study.shoestrade.domain.member.Role;
import com.study.shoestrade.exception.token.TokenNotFoundException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${spring.jwt.secretKey}")
    private String secretKey;

//    private long accessTokenValidTime = 2 * 1000L;  // 토큰 유효시간 : 2초 (테스트 용)
    private long accessTokenValidTime = 30 * 60 * 1000L;  // 토큰 유효시간 : 30분
    private long refreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L; // 7일

    private final UserDetailsService memberDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createAccessToken(String email, Role role){
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", role);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)  // 저장 정보
                .setIssuedAt(now)  // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))  // 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 서명할 때 사용되는 알고리즘 HS256과 키값
                .compact();
    }

    public String createRefreshToken(){
        Date now = new Date();

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 토큰으로 인증 객체(Authentication)을 얻기 위한 메소드
    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token){
        UserDetails userDetails = memberDetailsService.loadUserByUsername(getMemberEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getMemberEmail(String token) {
        try{
            if(token == null || token.isEmpty()) throw new TokenNotFoundException();

            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch (TokenNotFoundException e){
            throw new TokenNotFoundException();
        } catch (ExpiredJwtException e){
            log.info("d");
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), e.getMessage());
//            return e.getClaims().getSubject();
        } catch (SignatureException e){
            log.info("e");
            throw new SignatureException(e.getMessage());
        } catch (MalformedJwtException e){
            log.info("f");
            throw new MalformedJwtException(e.getMessage());
        }

//        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 값을 가져온다. "X-AUTH-TOKEN" : "TOKEN값"
    public String resolveToken(HttpServletRequest request){
        return request.getHeader("X-AUTH-TOKEN");
    }

    // 토큰의 유효성 + 만료 시간 확인
    public boolean validateTokenExpiration(String token){
        try{
            if(token == null || token.isEmpty()) throw new TokenNotFoundException();

            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (TokenNotFoundException e){
            throw new TokenNotFoundException();
        } catch (ExpiredJwtException e){
            log.info("a");
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), e.getMessage());
//            return e.getClaims().getSubject();
        } catch (SignatureException e){
            log.info("b");
            throw new SignatureException(e.getMessage());
        } catch (MalformedJwtException e){
            log.info("c");
            throw new MalformedJwtException(e.getMessage());
        }

        /*Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());*/
    }

}
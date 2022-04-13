package com.study.shoestrade.common.config.jwt;

import com.study.shoestrade.common.config.security.code.Code;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.exception.token.TokenNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try{
            // 헤더에서 JWT를 받아온다.
            String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

            if(token != null && jwtTokenProvider.validateTokenExpiration(token)){
                // 토큰이 유효하면 토큰으로부터 유저 정보를 받아온다.
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                // SecurityContext에 Authentication 객체를 저장
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }catch (MemberNotFoundException e){
            request.setAttribute("exception", Code.NOT_FOUND_MEMBER.getCode());
        } catch (TokenNotFoundException e){
            request.setAttribute("exception", Code.UNKNOWN_ERROR.getCode());
        } catch (ExpiredJwtException e){
            request.setAttribute("exception", Code.EXPIRED_TOKEN.getCode());
        } catch (SignatureException | MalformedJwtException e){
            request.setAttribute("exception", Code.WRONG_TYPE_TOKEN.getCode());
        }

//        // 유효한 토큰인지 확인
//        if(token != null && jwtTokenProvider.validateTokenExpiration(token){
//            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아온다.
//            Authentication auth = jwtTokenProvider.getAuthentication(token);
//            // SecurityContext에 Authentication 객체를 저장
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }
        chain.doFilter(request, response);
    }
}
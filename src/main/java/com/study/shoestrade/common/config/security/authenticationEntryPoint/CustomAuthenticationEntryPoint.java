package com.study.shoestrade.common.config.security.authenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.shoestrade.common.config.security.code.Code;
import com.study.shoestrade.common.config.security.dto.JsonDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = String.valueOf(request.getAttribute("exception"));

        if(exception.equals(String.valueOf(Code.UNKNOWN_ERROR.getCode()))){
            setResponse(response, Code.UNKNOWN_ERROR);
        }
        // 잘못된 타입의 토큰
        else if(exception.equals(String.valueOf(Code.WRONG_TYPE_TOKEN.getCode()))){
            setResponse(response, Code.WRONG_TYPE_TOKEN);
        }
        // 토큰이 만료된 경우
        else if(exception.equals(String.valueOf(Code.EXPIRED_TOKEN.getCode()))){
            setResponse(response, Code.EXPIRED_TOKEN);
        }else if (exception.equals(String.valueOf(Code.NOT_FOUND_MEMBER.getCode()))){
            setResponse(response, Code.NOT_FOUND_MEMBER);
        } else{
            setResponse(response, Code.ACCESS_DENIED);
        }
    }

    //한글 출력을 위해 getWriter() 사용
    private void setResponse(HttpServletResponse response, Code code) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().write(objectMapper.writeValueAsString(
                JsonDto.builder()
                        .success(false)
                        .code(code.getCode())
                        .msg(code.getMessage())
                        .build()
        ));
    }
}

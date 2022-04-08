package com.study.shoestrade.common.config.login;

import com.study.shoestrade.common.annotation.LoginMember;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private LoginService loginService;

    public LoginMemberArgumentResolver(@Lazy LoginService loginService){
        this.loginService = loginService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginMemberAnnotation = parameter.getParameterAnnotation(LoginMember.class) != null;
        return isLoginMemberAnnotation;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return loginService.getLoginMember();
    }
}

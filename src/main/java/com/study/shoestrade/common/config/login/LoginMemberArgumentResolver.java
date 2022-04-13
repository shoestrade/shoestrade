package com.study.shoestrade.common.config.login;

import com.study.shoestrade.common.annotation.LoginMember;
import com.study.shoestrade.service.member.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private LoginService loginService;
    private HttpServletRequest request;

    public LoginMemberArgumentResolver(@Lazy LoginService loginService,
                                       @Lazy HttpServletRequest request){
        this.loginService = loginService;
        this.request = request;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginMemberAnnotation = parameter.getParameterAnnotation(LoginMember.class) != null;
        return isLoginMemberAnnotation;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return loginService.getLoginMemberEmail(request);
    }
}

package com.study.shoestrade.common.config.security;

import com.study.shoestrade.common.config.security.member.MemberDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberDetailsService memberDetailsService;

    // 암호와에 필요한 PasswordEncoder를 Bean에 등록
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 인증을 무시할 경로 설정
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // spring security에서 csrf 토큰 없이 요청하면 요청을 막음
            .httpBasic().disable()

            .authorizeRequests()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/member/**").hasRole("MEMBER")
            .antMatchers("/**").permitAll()
        .and()
            .logout()
                .logoutSuccessUrl("/hello")  // 수정?
                .invalidateHttpSession(true); // 로그아웃 이후 세션 날리기
    }

    // 로그인할 때 필요한 정보를 가져오는 곳
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberDetailsService)
                .passwordEncoder(passwordEncoder());
        // 어떤 해쉬로 암호화했는지 알려줌
    }
}

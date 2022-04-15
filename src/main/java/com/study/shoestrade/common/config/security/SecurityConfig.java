package com.study.shoestrade.common.config.security;

import com.study.shoestrade.common.config.jwt.JwtAuthenticationFilter;
import com.study.shoestrade.common.config.jwt.JwtTokenProvider;
import com.study.shoestrade.common.config.security.accessDeniedHandler.CustomAccessDeniedHandler;
import com.study.shoestrade.common.config.security.authenticationEntryPoint.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    // 암호와에 필요한 PasswordEncoder를 Bean에 등록
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // 인증을 무시할 경로 설정
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()  // rest api이므로 기본 설정 미사용
                .csrf().disable()  // rest api이므로 csrf 보안 미사용
                .formLogin().disable()
                .logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // jwt로 인증하므로 세션 미사용
가                .and()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/brand/**").hasRole("ADMIN")
                .antMatchers("/product/**").hasRole("ADMIN")
                .antMatchers("/member/**").hasRole("MEMBER")
                .antMatchers("/my/**").hasRole("MEMBER")
                .antMatchers("/**").permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 추가
    }

}
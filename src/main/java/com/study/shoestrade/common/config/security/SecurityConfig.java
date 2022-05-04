package com.study.shoestrade.common.config.security;

import com.study.shoestrade.common.config.jwt.JwtAuthenticationFilter;
import com.study.shoestrade.common.config.jwt.JwtTokenProvider;
import com.study.shoestrade.common.config.security.accessDeniedHandler.CustomAccessDeniedHandler;
import com.study.shoestrade.common.config.security.authenticationEntryPoint.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                .and()
                .authorizeRequests()
                // AdminController
                .antMatchers("/admin/**").hasRole("ADMIN")

                // BrandController
                .antMatchers(HttpMethod.GET,"/brands").hasAnyRole("ADMIN", "MEMBER")
                .antMatchers("/brands/**").hasRole("ADMIN")

                // InterestController
                .antMatchers("/products/*/interests").hasRole("MEMBER")
                .antMatchers("/member/interests/**").hasRole("MEMBER")

                // MemberController
                .antMatchers(HttpMethod.POST, "/member",
                        "/member/join/send-mail",
                        "/member/join/check-mail",
                        "/member/login",
                        "/member/find-email",
                        "/member/find-password").permitAll()
                .antMatchers("member/token/reissuance").hasAnyRole("ADMIN", "MEMBER")
                .antMatchers("/member/**").hasRole("MEMBER")

                // ProductController
                .antMatchers(HttpMethod.GET, "/products").permitAll()
                .antMatchers(HttpMethod.GET, "/products/*").permitAll()
                .antMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/products/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/products/*").hasRole("ADMIN")
                .antMatchers("/products/*/images").hasRole("ADMIN")
                .antMatchers("/products/*/trades/**").hasAnyRole("ADMIN", "MEMBER")

                // TradeController
                .antMatchers("/trades").hasRole("MEMBER")

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
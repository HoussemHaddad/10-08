package com.gestionformation.config;

import com.gestionformation.security.*;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.security.*;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;

@Configuration

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserDetailsService userDetailsService;

    private final JHipsterProperties jHipsterProperties;

    private final RememberMeServices rememberMeServices;

    private final CorsFilter corsFilter;

    private final SecurityProblemSupport problemSupport;



    public SecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder, UserDetailsService userDetailsService,
                                 JHipsterProperties jHipsterProperties, RememberMeServices rememberMeServices, CorsFilter corsFilter, SecurityProblemSupport problemSupport, DataSource dataSource) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDetailsService = userDetailsService;
        this.jHipsterProperties = jHipsterProperties;
        this.rememberMeServices = rememberMeServices;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;

    }

    @PostConstruct
    public void init() {
        try {
            authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        } catch (Exception e) {
            throw new BeanInitializationException("Security configuration failed", e);
        }
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
        return new AjaxAuthenticationSuccessHandler();
    }

    @Bean
    public AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
        return new AjaxAuthenticationFailureHandler();
    }

    @Bean
    public AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler() {
        return new AjaxLogoutSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//            .antMatchers(HttpMethod.OPTIONS, "/**")
//            .antMatchers("/app/**/*.{js,html}")
//            .antMatchers("/i18n/**")
//            .antMatchers("/content/**")
//            .antMatchers("/swagger-ui/index.html")
//            .antMatchers("/test/**");
//    }

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.ldapAuthentication()
//            .userDnPatterns("uid={0},ou=otherpeople")
//
//            .groupSearchBase("ou=groups")
////            .userSearchBase("o=myO,ou=myOu") //don't add the base
////            .userSearchFilter("(uid={0})")
////            .groupSearchBase("ou=Groups") //don't add the base
//            .groupSearchFilter("member={0}")
//            .contextSource(getContextSource());
        auth
            .ldapAuthentication()
            .userDnPatterns("uid={0},ou=otherpeople")

            .groupSearchBase("ou=groups")
            .contextSource()
            .url("ldap://localhost:12345/dc=springframework,dc=org")
            .and()
            .passwordCompare()
          .passwordEncoder(new LdapShaPasswordEncoder())
            .passwordAttribute("userPassword");


    }



    @Bean
    public LdapContextSource getContextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://127.0.0.1:12345");
        contextSource.setBase("dc=springframework,dc=org");
        contextSource.setUserDn("uid=admin,dc=springframework,dc=org");
        contextSource.setPassword("secret");
        contextSource.afterPropertiesSet(); //needed otherwise you will have a NullPointerException in spring

        return contextSource;
    }

//    @Inject
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//            .ldapAuthentication()
//            .userDnPatterns("uid={0},ou=otherpeople")
//
//            .groupSearchBase("ou=groups")
//            .contextSource()
//            .url("ldap://localhost:12345/dc=springframework,dc=org")
//            .and()
//            .passwordCompare()
////                .passwordEncoder(new LdapShaPasswordEncoder())
//            .passwordAttribute("userPassword");
//
//
//    }


//
////    @InjectService
////    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
////        auth.ldapAuthentication()
////            //.userSearchBase("o=myO,ou=myOu") //don't add the base
//////            .userSearchFilter("(uid={0})")
////            .userDnPatterns("uid={0},ou=otherpeople")
////
////            .groupSearchBase("ou=groups") //don't add the base
////            .groupSearchFilter("member={0}")
////
////            .contextSource(getContextSource());
//////            .url("ldap://localhost:12345/dc=springframework,dc=org")
//////            .and()
//////            .passwordCompare()
////////                .passwordEncoder(new LdapShaPasswordEncoder())
//////            .passwordAttribute("userPassword");
////    }
////    @Bean
////    public LdapContextSource getContextSource() {
////        LdapContextSource contextSource = new LdapContextSource();
////        contextSource.setUrl("ldap://localhost:12345");
////        contextSource.setBase("dc=springframework,dc=org");
////        contextSource.setUserDn("uid=admin,dc=springframework,dc=org");
////        contextSource.setPassword("secret");
////        contextSource.afterPropertiesSet();
////System.out.println("conexion iciiiiiiiiiiiiiiiiiiii!");
////        return contextSource;
////    }
//



//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
////        auth.ldapAuthentication()
////            .userDnPatterns("uid={0},ou=otherpeople")
//////            .userSearchBase("o=myO,ou=myOu") //don't add the base
//////            .userSearchFilter("(uid={0})")
////            .groupSearchBase("ou=groups") //don't add the base
////            .groupSearchFilter("member={0}")
////            .contextSource(getContextSource());
//
//
////        auth .ldapAuthentication()
////            .userDnPatterns("uid={0},ou=people")
////            .userSearchFilter("(uid={0})")
////            .groupSearchBase("ou=groups")
////            .contextSource()
////            .url("ldap://localhost:12345/dc=springframework,dc=org")
////            .and()
////            .passwordCompare()
////            .passwordAttribute("userPassword");
//
//
//
//
//
//         auth
//                .ldapAuthentication()
//             .userSearchFilter("(uid={0})")
////                .userDnPatterns("uid={0},ou=otherpeople")
////
////                .groupSearchBase("ou=groups")
//                .contextSource()
//                .url("ldap://localhost:12345/dc=springframework,dc=org")
//                .and()
//                .passwordCompare()
////                .passwordEncoder(new LdapShaPasswordEncoder())
//                .passwordAttribute("userPassword");
//
//    }
//    @Bean
//    public LdapContextSource getContextSource() {
//        LdapContextSource contextSource = new LdapContextSource();
//        contextSource.setUrl("ldap://localhost:12345");
//        contextSource.setBase("dc=springframework,dc=org");
//        contextSource.setUserDn("uid=admin,dc=springframework,dc=org");
//        contextSource.setPassword("secret");
//        contextSource.afterPropertiesSet(); //needed otherwise you will have a NullPointerException in spring
//
//        return contextSource;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .anyRequest().fullyAuthenticated()
            .and()
            .formLogin()
//            .loginPage("/api/login")
//            .loginProcessingUrl("/api/login")
//            .loginProcessingUrl("/login")
       .loginPage("/login5")
            .permitAll()
//            .and()
//            .anyRequest().authenticated()

                .successForwardUrl("/login1")
//            .successForwardUrl("/login1")
////            .successHandler(ajaxAuthenticationSuccessHandler())
////            .failureHandler(ajaxAuthenticationFailureHandler())
//            .usernameParameter("j_username")
//            .passwordParameter("j_password")

//            .permitAll()
          .and()
            .authorizeRequests()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/admin/user-management").permitAll()
            .antMatchers("/api/testing").permitAll()
            .antMatchers("/t").permitAll()
            .antMatchers("/api/login").permitAll()
            .antMatchers("/login5").permitAll()
            .antMatchers("/api/yolo").permitAll()
            .antMatchers("/testing").permitAll()
            .antMatchers("/test").permitAll()
            .antMatchers("/login1").permitAll()
            .antMatchers("/houss").permitAll()
            .antMatchers("/prod").permitAll()
            .antMatchers("/form").permitAll()
            .antMatchers("/delete").permitAll()
            .antMatchers("/save").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/account/reset-password/init").permitAll()
            .antMatchers("/api/account/reset-password/finish").permitAll()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/info").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/v2/api-docs/**").permitAll()
            .antMatchers("/swagger-resources/configuration/ui").permitAll()
            .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN);


    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .csrf()
//            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//        .and()
//            .addFilterBefore(corsFilter, CsrfFilter.class)
//            .exceptionHandling()
//            .authenticationEntryPoint(problemSupport)
//            .accessDeniedHandler(problemSupport)
//        .and()
//            .rememberMe()
//            .rememberMeServices(rememberMeServices)
//            .rememberMeParameter("remember-me")
//            .key(jHipsterProperties.getSecurity().getRememberMe().getKey())
//        .and()
//            .formLogin()
//            .loginProcessingUrl("/api/authentication")
//            .successHandler(ajaxAuthenticationSuccessHandler())
//            .failureHandler(ajaxAuthenticationFailureHandler())
//            .usernameParameter("j_username")
//            .passwordParameter("j_password")
//            .permitAll()
//        .and()
//            .logout()
//            .logoutUrl("/api/logout")
//            .logoutSuccessHandler(ajaxLogoutSuccessHandler())
//            .permitAll()
//        .and()
//            .headers()
//            .frameOptions()
//            .disable()
//        .and()
//            .authorizeRequests()
//            .antMatchers("/api/register").permitAll()
//            .antMatchers("/api/yolo").permitAll()
//            .antMatchers("/test").permitAll()
//            .antMatchers("/login").permitAll()
//            .antMatchers("/houss").permitAll()
//            .antMatchers("/prod").permitAll()
//            .antMatchers("/form").permitAll()
//            .antMatchers("/delete").permitAll()
//            .antMatchers("/save").permitAll()
//            .antMatchers("/api/activate").permitAll()
//            .antMatchers("/api/authenticate").permitAll()
//            .antMatchers("/api/account/reset-password/init").permitAll()
//            .antMatchers("/api/account/reset-password/finish").permitAll()
//            .antMatchers("/api/**").authenticated()
//            .antMatchers("/management/health").permitAll()
//            .antMatchers("/management/info").permitAll()
//            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
//            .antMatchers("/v2/api-docs/**").permitAll()
//            .antMatchers("/swagger-resources/configuration/ui").permitAll()
//            .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN);
//
//    }






    }








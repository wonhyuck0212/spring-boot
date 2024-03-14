

package com.elice.agora.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
            .csrf((csrf) -> csrf
                .ignoringRequestMatchers(new AntPathRequestMatcher("/**")))
            .headers((headers) -> headers
                .addHeaderWriter(new XFrameOptionsHeaderWriter(
                    XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
            .formLogin((formLogin) -> formLogin
                .loginPage("/user/login")
                .defaultSuccessUrl("/"))
        ;

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}




// package com.elice.agora.security.config;

// import jakarta.servlet.DispatcherType;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;

// import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

// import static org.springframework.security.config.Customizer.withDefaults;

// @Configuration
// @EnableWebSecurity
// public class WebSecurityConfig {

//     // @Bean
//     // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         // http.csrf().disable().cors().disable()
//         //         .authorizeHttpRequests(request -> request
//         //                 .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
//         //                 .requestMatchers("/me").permitAll()
//         //                 .anyRequest().authenticated()
//         //         )
//         //         .formLogin(login -> login
//         //                 .loginPage("/view/login")
//         //                 .loginProcessingUrl("/login-process")
//         //                 .usernameParameter("userid")
//         //                 .passwordParameter("pw")
//         //                 .defaultSuccessUrl("/view/dashboard", true)
//         //                 .permitAll()
//         //         )
//         //         .logout(withDefaults());

//         // return http.build();

//   @Bean
//   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//       http.csrf().disable().cors().disable()
//               .authorizeHttpRequests(request -> request
//                       .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
//                       .requestMatchers("/api/users/**", "/h2-console/**").permitAll()
//                       .anyRequest().authenticated()
//               )
//               .formLogin(login -> login
//                       .loginPage("/view/login")
//                       .loginProcessingUrl("/login-process")
//                       .usernameParameter("userid")
//                       .passwordParameter("pw")
//                       .defaultSuccessUrl("/view/dashboard", true)
//                       .permitAll()
//               )
//               .logout(withDefaults()
//               )
//               .headers((headers) -> headers
//               .addHeaderWriter(new XFrameOptionsHeaderWriter(
//                   XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)));
              
//             return http.build();
//     // http.authorizeHttpRequests(
//     //     request -> request.anyRequest().permitAll()
//     // );  


//   }
    

    
// }



// package com.elice.agora.security.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
// import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;


// @Configuration
// @EnableWebSecurity
// public class WebSecurityConfig {
//     @Bean
//     SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//             .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
//                 .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
//                 .csrf((csrf) -> csrf
//                 .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
//                 .headers((headers) -> headers
//                 .addHeaderWriter(new XFrameOptionsHeaderWriter(
//                     XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
//         ;
//         return http.build();
//     }

//     @Bean
//     PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }
// }



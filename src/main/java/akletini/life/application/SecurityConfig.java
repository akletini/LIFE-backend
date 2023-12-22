package akletini.life.application;



//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtAuthenticationFilter jwtAuthFilter;
//    private final AuthenticationProvider authenticationProvider;
//    private final LogoutHandler logoutHandler;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf()
//                .disable()
//                .cors(Customizer.withDefaults())
//                .authorizeHttpRequests()
//                .requestMatchers("/auth/**")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .logout()
//                .logoutUrl("/auth/logout")
//                .addLogoutHandler(logoutHandler)
//                .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()));
//        return http.build();
//    }
//
//
//}

package mysite.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.security.UserDetailsServiceImpl;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import java.io.IOException;

@SpringBootConfiguration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return webSecurity -> webSecurity.httpFirewall(new DefaultHttpFirewall());
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin((formLogin) -> {
					formLogin
							.loginPage("/user/login")
							.loginProcessingUrl("/user/auth")
							.usernameParameter("email")
							.passwordParameter("password")
							.defaultSuccessUrl("/")
							// .failureUrl("/user/login?result=fail");
							.failureHandler(new AuthenticationFailureHandler() {
								@Override
								public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
									request.setAttribute("email", request.getParameter("email"));
									request
											.getRequestDispatcher("/user/login")
											.forward(request, response);
								}
							});
				})
				.logout(logout -> {
					logout
							.logoutUrl("/user/logout")
							.logoutSuccessUrl("/");
				})
				.authorizeHttpRequests((authorizeRequests) -> {
					authorizeRequests
							/* ACL */
							.requestMatchers(new RegexRequestMatcher("^/admin/?.*$", null))
							.hasRole("ADMIN")

							.requestMatchers(new RegexRequestMatcher("^/board/?(write|reply|delete|modify).*$", null))
							.hasAnyRole("ADMIN", "USER")

							.requestMatchers(new RegexRequestMatcher("^/user/update$", null))
							.hasAnyRole("ADMIN", "USER")

							.anyRequest()
							.permitAll();
				})
				.exceptionHandling(exceptionHandlingConfigurer -> {
					exceptionHandlingConfigurer
							// .accessDeniedPage("/WEB-INF/views/errors/403.jsp")
							.accessDeniedHandler(new AccessDeniedHandler() {
								@Override
								public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
									response.sendRedirect(request.getContextPath());
								}
							});
				});


		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		authenticationProvider.setUserDetailsService(userDetailsService);

		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4 /* 4 ~ 31 */);
	}
}

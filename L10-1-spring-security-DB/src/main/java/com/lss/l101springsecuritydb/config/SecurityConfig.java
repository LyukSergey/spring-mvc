package com.lss.l101springsecuritydb.config;

import com.lss.l101springsecuritydb.repository.UserRepository;
import com.lss.l101springsecuritydb.security.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserRepository userRepository) throws Exception {
        return http
                // CSRF (Cross-Site Request Forgery) — це атака, коли зловмисник змушує користувача виконати небажану дію на сайті,
                // на якому він авторизований.
                // CSRF-захист вимикають для stateless API або якщо захист реалізовано іншим способом, наприклад, через токени.
                // Користувач увійшов у свій акаунт на bank.com і має активну сесію.
                // Зловмисник створює шкідливий сайт із таким HTML:
                // <img src="http://bank.com/transfer?amount=1000&to=attacker" />
                // Коли користувач заходить на шкідливий сайт, браузер автоматично відправляє запит на bank.com,
                // використовуючи активну сесію користувача.
                // Як зловмисник отримує активну сесію користувача?
                // Коли користувач заходить на шкідливий сайт, браузер автоматично відправляє запит на bank.com,
                // використовуючи активну сесію користувача.
                // Це відбувається тому, що браузер зберігає куки (cookies) для всіх сайтів, на які заходив користувач.
                // Коли браузер відправляє запит на bank.com, він автоматично додає всі куки, пов'язані з цим доменом.
                // Це може призвести до несанкціонованого переказу грошей.
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(userDetailsService(userRepository))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                // Вимога аутентифікації для всіх запитів
                                .requestMatchers("/users/admin/**").hasRole("ADMIN")
                                .requestMatchers("/users/me/**").hasAnyRole("USER", "ADMIN")
                                //Ця конфігурація дозволяє всім користувачам (навіть неавторизованим) доступ до сторінок входу,
                                // реєстрації та статичних ресурсів (/login, /register, /css/**, /js/**).
                                // Це потрібно, щоб користувачі могли бачити форму входу, реєструватися та завантажувати стилі й скрипти без авторизації.
                                .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()
                                // Відкриття доступу до всіх інших
                                .anyRequest().permitAll()
                )
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        .accessDeniedHandler((req, res, ex) -> {
                            // Відправка кастомного повідомлення про помилку 403
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.getWriter().write("Access Denied");
                        })
                )
                .formLogin(form -> form
                        // Налаштування сторінки входу
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            response.sendRedirect("/profile");
                        })
                        // Дозволити всім
                        .permitAll())
                // Вимога аутентифікації через форму
                .logout(logout -> logout
                        // Налаштування URL для виходу
                        .logoutUrl("/logout")
                        // Налаштування URL для обробки виходу
                        .logoutSuccessUrl("/login")
                        //Цей рядок вказує Spring Security при виході користувача (logout) інвалідовувати (знищити) HTTP-сесію.
                        // Це означає, що всі дані, пов'язані із сесією користувача (наприклад, атрибути сесії), будуть видалені.
                        // Це важливо для безпеки, щоб після виходу не залишалося жодної інформації,
                        // яка могла б бути використана повторно для доступу до захищених ресурсів.
                        .invalidateHttpSession(true)
                        // Видалення cookie після виходу
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                // Вимога аутентифікації через вихід
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder — це реалізація PasswordEncoder, яка використовує алгоритм bcrypt для хешування паролів.
        return new BCryptPasswordEncoder();
    }

    // DaoAuthenticationProvider — це компонент Spring Security, який відповідає за аутентифікацію користувачів.
    // Він використовує UserDetailsService для отримання деталей користувача (ім'я користувача, пароль, ролі тощо) з бази даних
    // та PasswordEncoder для перевірки пароля. DaoAuthenticationProvider є частиною механізму аутентифікації Spring Security.
    // Він дозволяє налаштувати, як Spring Security отримує дані користувача та перевіряє їх.
    // DaoAuthenticationProvider використовує UserDetailsService для отримання деталей користувача з бази даних.
    // Це дозволяє Spring Security перевіряти, чи існує користувач з вказаним ім'ям користувача та паролем.
    // DaoAuthenticationProvider також використовує PasswordEncoder для перевірки пароля.
    // PasswordEncoder — це інтерфейс, який визначає методи для хешування паролів та перевірки їх відповідності.
    // DaoAuthenticationProvider використовує реалізацію PasswordEncoder, яку ви надаєте, для хешування паролів та перевірки їх відповідності.
    // Це дозволяє вам використовувати різні алгоритми хешування паролів, такі як bcrypt, PBKDF2, Argon2 тощо.
    // DaoAuthenticationProvider також дозволяє вам налаштувати, як Spring Security обробляє аутентифікацію.
    // Ви можете налаштувати, які дані користувача потрібно отримати з бази даних, як перевіряти пароль та які ролі призначати користувачу
    // після успішної аутентифікації. Це дозволяє вам налаштувати механізм аутентифікації відповідно до ваших потреб.
    // DaoAuthenticationProvider є частиною механізму аутентифікації Spring Security
    // і дозволяє вам налаштувати, як Spring Security отримує дані користувача та перевіряє їх.
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserRepository userRepository) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService(userRepository));
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // AuthenticationManager — це інтерфейс, який визначає методи для аутентифікації користувачів.
    // Він використовується для перевірки облікових даних користувача (ім'я користувача та пароль) та надання доступу до захищених ресурсів.
    // AuthenticationManager є частиною механізму аутентифікації Spring Security
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }
}

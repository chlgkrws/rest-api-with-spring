package com.hj.restapi.configs;

import com.hj.restapi.accounts.Account;
import com.hj.restapi.accounts.AccountRepository;
import com.hj.restapi.accounts.AccountRole;
import com.hj.restapi.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner(){
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account testUser = Account.builder()
                        .email("user@naver.com")
                        .password("gkrwns")
                        .roles(Set.of(AccountRole.USER))
                        .build();
                accountService.saveAccount(testUser);

                Account testAdmin = Account.builder()
                        .email("admin@naver.com")
                        .password("gkrwns")
                        .roles(Set.of(AccountRole.ADMIN))
                        .build();
                accountService.saveAccount(testAdmin);
            }
        };
    }
}

package com.kanban.auth.service;

import com.kanban.user.entity.User;
import com.kanban.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByAccount(username);
        if (user.isEmpty()) {
            log.error("{} 유저를 찾을 수 없습니다. ", username);
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
        }
        log.info("{} 유저 인증 성공!",username);
        return user.get();
    }
}

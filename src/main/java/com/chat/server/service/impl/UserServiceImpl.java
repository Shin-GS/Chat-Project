package com.chat.server.service.impl;

import com.chat.server.domain.repository.UserRepository;
import com.chat.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<String> findSimilarNamesExcludingExactMatch(String pattern, String username) {
        if (pattern == null || username == null) {
            return new ArrayList<>();
        }

        return userRepository.findSimilarNamesExcludingExactMatch(pattern, username);
    }
}

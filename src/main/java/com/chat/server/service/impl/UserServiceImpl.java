package com.chat.server.service.impl;

import com.chat.server.domain.repository.UserRepository;
import com.chat.server.service.UserService;
import com.chat.server.service.response.UserInfoResponse;
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
    public List<UserInfoResponse> findSimilarNamesExcludingExactMatch(String pattern, Long userId) {
        if (pattern == null || userId == null) {
            return new ArrayList<>();
        }

        return userRepository.findSimilarNamesExcludingExactMatch(pattern, userId).stream()
                .map(UserInfoResponse::of)
                .toList();
    }
}

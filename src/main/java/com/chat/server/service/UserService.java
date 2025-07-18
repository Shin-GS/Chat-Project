package com.chat.server.service;

import java.util.List;

public interface UserService {
    List<String> findSimilarNamesExcludingExactMatch(String pattern, String username);
}

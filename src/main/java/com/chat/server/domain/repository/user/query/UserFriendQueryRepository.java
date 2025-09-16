package com.chat.server.domain.repository.user.query;

import com.chat.server.domain.dto.UserDto;
import com.chat.server.domain.vo.UserId;
import org.hibernate.query.SortDirection;

import java.util.List;

public interface UserFriendQueryRepository {
    List<UserDto> findAllByUserId(UserId userId,
                                  String order,
                                  SortDirection direction);

    List<UserDto> findSimilarNamesExcludingExactMatch(String pattern,
                                                      UserId userId,
                                                      String order,
                                                      SortDirection direction);
}

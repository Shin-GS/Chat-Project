package com.chat.server.service.common.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;

public record CustomPageRequest(Integer page,
                                Integer limit,
                                List<String> order) {
    public static CustomPageRequest of(Integer page,
                                       Integer limit,
                                       List<String> order) {
        return new CustomPageRequest(page, limit, order);
    }

    @JsonIgnore
    public Sort getSort() {
        if (order == null || order.isEmpty()) {
            return Sort.unsorted();
        }

        List<Sort.Order> orders = order.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(o -> {
                    String[] parts = o.split(",");
                    String property = parts[0].trim();
                    Sort.Direction direction = (parts.length > 1 && "asc".equalsIgnoreCase(parts[1])) ? Sort.Direction.ASC : Sort.Direction.DESC;
                    return new Sort.Order(direction, property);
                }).toList();
        return Sort.by(orders);
    }

    @JsonIgnore
    public Pageable toPageable() {
        return PageRequest.of(page, limit, getSort());
    }
}

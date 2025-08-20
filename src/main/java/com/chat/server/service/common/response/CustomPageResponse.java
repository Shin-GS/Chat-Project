package com.chat.server.service.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.function.Function;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record CustomPageResponse<T>(List<T> content,
                                    int page,
                                    int size,
                                    int numberOfElements,
                                    boolean first,
                                    boolean last,
                                    boolean hasNext,
                                    boolean hasPrevious,
                                    Long totalElements,
                                    Integer totalPages) {
    // if you use infinity scroll
    public static <E, T> CustomPageResponse<T> fromSlice(Slice<E> slice,
                                                         Function<E, T> mapper) {
        return new CustomPageResponse<>(
                slice.getContent().stream().map(mapper).toList(),
                slice.getNumber(),
                slice.getSize(),
                slice.getNumberOfElements(),
                slice.isFirst(),
                slice.isLast(),
                slice.hasNext(),
                slice.hasPrevious(),
                null, // null when Slice
                null // null when Slice
        );
    }

    // if you use normal list page
    public static <E, T> CustomPageResponse<T> fromPage(Page<E> page,
                                                        Function<E, T> mapper) {
        return new CustomPageResponse<>(
                page.getContent().stream().map(mapper).toList(),
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    public static <T> CustomPageResponse<T> emptySlice(Pageable pageable) {
        return new CustomPageResponse<>(
                List.of(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                0,
                true,
                true,
                false,
                pageable.getPageNumber() > 0,
                null,
                null
        );
    }

    public static <T> CustomPageResponse<T> emptyPage(Pageable pageable) {
        return new CustomPageResponse<>(
                List.of(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                0,
                true,
                true,
                false,
                pageable.getPageNumber() > 0,
                0L,
                0
        );
    }
}

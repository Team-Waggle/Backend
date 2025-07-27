package com.waggle.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.Function;

@Schema(description = "커서 기반 페이지네이션 응답")
public record CursorResponse<T>(
    @Schema(description = "데이터 목록")
    List<T> content,

    @Schema(description = "다음 페이지 존재 여부")
    boolean hasNext,

    @Schema(description = "다음 페이지 커서")
    Long nextCursor,

    @Schema(description = "현재 페이지 데이터 개수")
    int size
) {

    public static <E, D> CursorResponse<D> of(
        List<E> entities,
        int requestedSize,
        Function<E, D> mapper,
        Function<E, Long> idExtractor
    ) {
        boolean hasNext = entities.size() > requestedSize;

        List<E> content = hasNext ? entities.subList(0, requestedSize) : entities;

        Long nextCursor = hasNext ? idExtractor.apply(content.get(content.size() - 1)) : null;

        List<D> mappedContent = content.stream()
            .map(mapper)
            .toList();

        return new CursorResponse<>(mappedContent, hasNext, nextCursor, mappedContent.size());
    }
}

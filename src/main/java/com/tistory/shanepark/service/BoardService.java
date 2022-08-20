package com.tistory.shanepark.service;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.*;
import com.tistory.shanepark.domain.Board;
import com.tistory.shanepark.utils.BoardPostRequestConverter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BoardService {
    private final Map<Long, Board> boards = new ConcurrentHashMap<>();

    @Post("/boards")
    @RequestConverter(BoardPostRequestConverter.class)
    public HttpResponse createBoard(Board board) {
        boards.put(board.getId(), board);
        return HttpResponse.ofJson(board);
    }

    @Get("/boards/:id")
    public HttpResponse getBoard(@Param long id) {
        Board board = boards.get(id);
        return HttpResponse.ofJson(board);
    }

    @Get("/boards")
    @ProducesJson
    public List<Board> getBoards(@Param @Default("true") boolean descOrder) {
        if (descOrder) {
            return boards.entrySet().stream()
                    .sorted(Collections.reverseOrder(Comparator.comparingLong(Map.Entry::getKey)))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }
        return boards.values().stream().collect(Collectors.toList());
    }

}

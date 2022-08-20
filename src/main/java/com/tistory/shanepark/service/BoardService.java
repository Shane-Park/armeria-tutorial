package com.tistory.shanepark.service;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.annotation.*;
import com.tistory.shanepark.domain.Board;
import com.tistory.shanepark.utils.BadRequestExceptionHandler;
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

    @Put("/boards/:id")
    public HttpResponse updateBoard(@Param long id, @RequestObject Board board) {
        Board oldBoard = boards.get(id);
        if (oldBoard == null) {
            return HttpResponse.of(HttpStatus.NOT_FOUND);
        }
        Board newBoard = new Board(id, board.getTitle(), board.getContent(), oldBoard.getCreatedAt(), board.getCreatedAt());
        boards.put(id, newBoard);
        return HttpResponse.ofJson(newBoard);
    }

    @Blocking
    @Delete("/boards/:id")
    @ExceptionHandler(BadRequestExceptionHandler.class)
    public HttpResponse deleteBoard(@Param long id) {
        Board removed = boards.remove(id);
        if (removed == null) {
            throw new IllegalArgumentException("The board not exist. id: " + id);
        }
        return HttpResponse.of(HttpStatus.NO_CONTENT);
    }

}

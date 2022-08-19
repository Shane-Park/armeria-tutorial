package com.tistory.shanepark.service;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.RequestConverter;
import com.tistory.shanepark.domain.Board;
import com.tistory.shanepark.utils.BoardPostRequestConverter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BoardService {
    private final Map<Long, Board> boards = new ConcurrentHashMap<>();

    @Post("/boards")
    @RequestConverter(BoardPostRequestConverter.class)
    public HttpResponse createBoard(Board board) {
        boards.put(board.getId(), board);
        return HttpResponse.ofJson(board);
    }

}

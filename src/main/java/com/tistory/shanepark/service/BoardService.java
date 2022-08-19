package com.tistory.shanepark.service;

import com.linecorp.armeria.server.annotation.Post;
import com.tistory.shanepark.domain.Board;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BoardService {
    private final Map<Integer, Board> boards = new ConcurrentHashMap<>();

    @Post("/blogs")
    public void createBoard(Board board) {

    }

}

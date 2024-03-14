package com.elice.agora.board.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elice.agora.board.dto.GetAllBoardsDto;
import com.elice.agora.board.dto.GetOneBoardDto;
import com.elice.agora.board.dto.InsertBoardDto;
import com.elice.agora.board.dto.UpdateRequestBoardDto;
import com.elice.agora.board.dto.UpdateResponseBoardDto;
import com.elice.agora.board.service.BoardService;
import com.elice.agora.token.JwtUtil;
import com.elice.agora.user.entity.UserEntity;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    
    private final BoardService boardService;
    private final JwtUtil jwtUtil;

    @Autowired
    public BoardController(BoardService boardService, JwtUtil jwtUtil) {
        this.boardService = boardService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping()
    public ResponseEntity<String> insertBoard(@RequestBody InsertBoardDto insertBoardDto, HttpServletRequest request) {
        try {
            System.out.println(insertBoardDto.isAnonymous);
            UserEntity user = jwtUtil.getUserFromToken(request);
            boardService.insertBoard(insertBoardDto, user);

            return ResponseEntity.ok("게시글 등록 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneBoard(@PathVariable("id") Long id, HttpServletRequest request) {
        try {
            UserEntity user = jwtUtil.getUserFromToken(request);

            GetOneBoardDto result = boardService.getOneBoardById(id, user);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request: " + e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<Object> getAllBoards(@RequestParam(required = false, defaultValue = "0", name="page") Integer page, 
            @RequestParam(required = false, defaultValue = "20", name="size") Integer size, 
            @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime date,
            HttpServletRequest request) {
        try {
            UserEntity user = jwtUtil.getUserFromToken(request);

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            List<GetAllBoardsDto> list = boardService.getManyBoards(date, pageable, user);
            
            return ResponseEntity.ok(list);        
        } catch (Exception e) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBoard(@PathVariable("id") Long id, @RequestBody UpdateRequestBoardDto updateRequestBoardDto,
            HttpServletRequest request) {
        try {
            UserEntity user = jwtUtil.getUserFromToken(request);

            UpdateResponseBoardDto board = boardService.updateBoard(id, updateRequestBoardDto, user);

            return ResponseEntity.ok(board);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable("id") Long id, HttpServletRequest request) {
        try {
            UserEntity user = jwtUtil.getUserFromToken(request);

            boardService.deleteBoard(id, user);
            return ResponseEntity.ok("게시글 삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request: " + e.getMessage());
        }
    }
}

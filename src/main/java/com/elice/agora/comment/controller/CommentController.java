package com.elice.agora.comment.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elice.agora.comment.dto.GetCommentDTO;
import com.elice.agora.board.repository.BoardRepository;
import com.elice.agora.comment.dto.GetCommentAllDTO;
import com.elice.agora.comment.dto.InsertCommentDTO;
import com.elice.agora.comment.dto.PutCommentsDTO;
import com.elice.agora.comment.entity.CommentEntity;
import com.elice.agora.comment.service.CommentService;
import com.elice.agora.token.JwtUtil;
import com.elice.agora.user.entity.UserEntity;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/boards/{boardId}/comment")
public class CommentController {
    
    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @Autowired
    public CommentController(CommentService commentService, JwtUtil jwtUtil) {
        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping()
    public ResponseEntity<String> insertComment(@PathVariable("boardId") Long boardId, @RequestBody InsertCommentDTO insertCommentDTO, HttpServletRequest request) {
        try {
            UserEntity user = jwtUtil.getUserFromToken(request);
            commentService.insertComment(boardId, insertCommentDTO, request, user);

            return ResponseEntity.ok("댓글 작성 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request: " + e.getMessage());
        }
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<GetCommentDTO> getComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId, HttpServletRequest request) {
        try {
            UserEntity user = jwtUtil.getUserFromToken(request);
            GetCommentDTO result = commentService.getComment(boardId, commentId, user);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request: " + e.getMessage());
            return null;
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Object> updateComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId, @RequestBody PutCommentsDTO putCommentsDTO, HttpServletRequest request) {
        try {
            UserEntity user = jwtUtil.getUserFromToken(request);

            GetCommentDTO comment = commentService.updateComment(boardId, commentId, putCommentsDTO, user);
            return ResponseEntity.ok(comment);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request: " + e.getMessage());
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId, HttpServletRequest request) {
        try {
            UserEntity user = jwtUtil.getUserFromToken(request);
            
            commentService.deleteComment(boardId, commentId, user);
            return ResponseEntity.ok("댓글 삭제 성공");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request: " + e.getMessage());
        }
    }

    // @GetMapping()
    // public ArrayList<CommentEntity> getCommentAll(@PathVariable("boardId") Long boardId, HttpServletRequest request) {
    //     try {
    //         UserEntity user = jwtUtil.getUserFromToken(request);
    // //         // ArrayList<CommentEntity> getCommentAll =commentService.getAllComments(boardId, request);
    //         // return getCommentAll;
    //         return null;
    //     } catch (Exception e) {
    //         return null;
            
    //     }
    // //     return null;
    // }
}

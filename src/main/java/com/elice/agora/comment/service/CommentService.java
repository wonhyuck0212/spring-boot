package com.elice.agora.comment.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elice.agora.board.entity.BoardEntity;
import com.elice.agora.board.repository.BoardRepository;
import com.elice.agora.board.service.BoardCheck;
import com.elice.agora.comment.dto.GetCommentAllDTO;
import com.elice.agora.comment.dto.GetCommentDTO;
import com.elice.agora.comment.dto.InsertCommentDTO;
import com.elice.agora.comment.dto.PutCommentsDTO;
import com.elice.agora.comment.entity.CommentEntity;
import com.elice.agora.comment.repository.CommentRepository;
import com.elice.agora.token.JwtUtil;
import com.elice.agora.user.entity.UserEntity;
import com.elice.agora.user.service.UserService;
import com.elice.agora.utils.CheckEmpty;
import com.elice.agora.utils.enums.DeletedYn;
import com.elice.agora.utils.enums.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserService userService;
    
    @Autowired
    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
        this.userService = userService;
    }

    public void insertComment(Long boardId, InsertCommentDTO insertCommentDTO, HttpServletRequest request, UserEntity user) throws Exception {

        CheckEmpty.checkNull(insertCommentDTO.content, "400 번대 에러 - Null임");
        if (insertCommentDTO.content.length() > 100) {
            throw new Exception("400 번대 에러 - 내용 글자수 제한 초과");
        }
        if (!BoardCheck.checkSwearWords(insertCommentDTO.content)) {
            throw new Exception("400 번대 에러 - 욕설 포함");
        }

        BoardEntity boardEntity = boardRepository.findById(boardId).orElse(null);

        CommentEntity commentEntity = insertCommentDTO.toEntity(); // 작성하기

        commentEntity.setBoardId(boardEntity);
        commentEntity.setCreatedBy(user);
        commentEntity.setUpdatedBy(user);

        commentRepository.save(commentEntity);
        commentRepository.flush();
    }
    
    public GetCommentDTO getComment(Long boardId, Long commentId, UserEntity user) throws Exception  {

        // 댓글이 있는지
        CommentEntity comment = commentRepository.findById(commentId).orElse(null);
        if (user.getRole() == Role.MEMBER && comment.getDeletedYn() == DeletedYn.Y) {
            throw new Exception("이미 삭제된 게시물입니다.");
        }
        
        return GetCommentDTO.getFromCommentEntity(comment); // 날짜 형식 유의
    }

    public GetCommentDTO updateComment(Long boardId, long commentId, PutCommentsDTO putCommentsDTO, UserEntity user) throws Exception {

        CheckEmpty.checkNull(putCommentsDTO.content, "400 번대 에러 - Null임");
        if (putCommentsDTO.content.length() > 100) {
            throw new Exception("400 번대 에러 - 내용 글자수 제한 초과");
        }
        if (!BoardCheck.checkSwearWords(putCommentsDTO.content)) {
            throw new Exception("400 번대 에러 - 욕설 포함");
        }
        
        // 댓글이 있는지 
        CommentEntity comment = commentRepository.findById(commentId).orElse(null);

        if (user.getRole() == Role.MEMBER) {
            if (!comment.getUpdatedBy().equals(user)) {
                throw new Exception("권한이 없습니다.");
            }
            if (comment.getDeletedYn() == DeletedYn.Y) {
                throw new Exception("이미 삭제된 게시물입니다.");
            }
        }

        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);
        commentRepository.flush();
        return GetCommentDTO.getFromCommentEntity(comment);
    }

    public void deleteComment(Long boardId, long commentId, UserEntity user) throws Exception {

        CommentEntity comment = commentRepository.findById(commentId).orElse(null);
        if (user.getRole() == Role.MEMBER) {
            if (!comment.getUpdatedBy().equals(user)) {
                throw new Exception("권한이 없습니다.");
            }
            if (comment.getDeletedYn() == DeletedYn.Y) {
                throw new Exception("이미 삭제된 게시물입니다.");
            }
        }

        comment.setDeletedYn(DeletedYn.Y);

        commentRepository.save(comment);
        commentRepository.flush();
    }

    // public GetCommentAllDTO getAllComments( Long boardId, HttpServletRequest request, Long page, Long size) throws Exception  {

    //     // // 토큰 유효하지 않을 경우 400번대 에러 
    //     // UserEntity userEntity = jwtUtil.getUserFromToken(request).orElseThrow(
    //     //     () -> new CustomException(NOT_FOUND_USER)
    //     // );
        
    //     // BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(
    //     //     () -> new CustomException(NOT_FOUND_BOARD)
    //     // );


    //     // List<CommentCommentDto> commentList = new ArrayList<>();
    //     // for (CommentEntity comment : boardEntity.getCommentList()) {
    //     //     commentList.add(new CommentResponseDto(comment));
    //     // }

    //     // // 커멘트 가져오는 코드 작성
    //     // GetCommentAllDTO(boardId, commentList) ;

    //     // return commentList;

    //     return null;
    // }
}

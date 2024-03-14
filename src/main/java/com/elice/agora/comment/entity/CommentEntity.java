package com.elice.agora.comment.entity;

import java.time.LocalDateTime;

import com.elice.agora.board.entity.BoardEntity;
import com.elice.agora.user.entity.UserEntity;
import com.elice.agora.utils.enums.AnonymousYn;
import com.elice.agora.utils.enums.DeletedYn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "Comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name="created_by")
    private UserEntity createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private UserEntity updatedBy;

    @Column(name = "deleted_yn")
    @Enumerated(EnumType.STRING)
    private DeletedYn deletedYn;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private BoardEntity boardId;

    @Builder
    public CommentEntity(Long commentId, String content, LocalDateTime createdAt,UserEntity createdBy, LocalDateTime updatedAt, UserEntity updatedBy, DeletedYn deletedYn, BoardEntity boardId) {
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.deletedYn = deletedYn;
        this.createdBy = createdBy;
        this.boardId = boardId;
    }
}

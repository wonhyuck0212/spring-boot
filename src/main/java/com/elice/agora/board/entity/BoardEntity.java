package com.elice.agora.board.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.elice.agora.user.entity.UserEntity;
import com.elice.agora.utils.enums.AnonymousYn;
import com.elice.agora.utils.enums.DeletedYn;
import com.elice.agora.utils.enums.VoteYn;
import com.elice.agora.comment.entity.CommentEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Board")
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    private String title;

    private String content;

    @Column(name = "anonymous_yn")
    @Enumerated(EnumType.STRING)
    private AnonymousYn anonymousYn;

    @Column(name = "vote_yn")
    @Enumerated(EnumType.STRING)
    private VoteYn voteYn;

    @Column(name = "vote_start_at")
    private LocalDateTime voteStartAt;

    @Column(name = "vote_end_at")
    private LocalDateTime voteEndAt;

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

    @OneToMany(mappedBy = "boardId")
    private List<CommentEntity> comments;

    // @Builder
    // public BoardEntity(Long boardId, String title, String content, UserEntity updatedBy, AnonymousYn anonymousYn, LocalDateTime createdAt,  LocalDateTime updatedAt, DeletedYn deletedYn, UserEntity createdBy) {
    //     this.boardId = boardId;
    //     this.title = title;
    //     this.content = content;
    //     this.anonymousYn = anonymousYn;
    //     this.createdAt = createdAt;
    //     this.updatedAt = updatedAt;
    //     this.updatedBy = updatedBy;
    //     this.deletedYn = deletedYn;
    //     this.createdBy = createdBy;
    // }
}

package com.elice.agora.vote.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.elice.agora.board.entity.BoardEntity;
import com.elice.agora.user.entity.UserEntity;
import com.elice.agora.utils.enums.AnonymousYn;
import com.elice.agora.utils.enums.DeletedYn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Vote")
public class VoteEntity {
    @Id
    @Column(name = "vote_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private BoardEntity boardId;

    @Column(name = "option")
    private String option;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private UserEntity updatedBy;

    @OneToMany(mappedBy = "voteId")
    private List<UserVoteEntity> userVotes;

    // @Builder
    // public VoteEntity(BoardEntity board, String title, String content, UserEntity updatedBy, AnonymousYn anonymousYn, LocalDateTime createdAt,  LocalDateTime updatedAt, UserEntity createdBy) {
    //     this.boardId = board;
    //     this.createdAt = createdAt;
    //     this.updatedAt = updatedAt;
    //     this.updatedBy = updatedBy;
    //     this.createdBy = createdBy;
    // }
}

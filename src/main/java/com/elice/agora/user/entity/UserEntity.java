package com.elice.agora.user.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.elice.agora.board.entity.BoardEntity;
import com.elice.agora.utils.enums.Role;
import com.elice.agora.vote.entity.UserVoteEntity;
import com.elice.agora.vote.entity.VoteEntity;
import com.elice.agora.comment.entity.CommentEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "Users")
public class UserEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role; // MEMBER / ADMIN

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "createdBy")
    private List<BoardEntity> createdBoards;

    @OneToMany(mappedBy = "updatedBy")
    private List<BoardEntity> updatedBoards;

    @OneToMany(mappedBy = "createdBy")
    private List<CommentEntity> createdComments;

    @OneToMany(mappedBy = "updatedBy")
    private List<CommentEntity> updatedComments;

    @OneToMany(mappedBy = "createdBy")
    private List<VoteEntity> createdVotes;

    @OneToMany(mappedBy = "updatedBy")
    private List<VoteEntity> updatedVotes;

    @OneToMany(mappedBy = "createdBy")
    private List<UserVoteEntity> createdUserVotes;

    @Builder
    public UserEntity(Long userId, String email, String password, String nickname, Role role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}

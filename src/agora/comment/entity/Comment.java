package com.elice.agora.comment.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "comment_id")
    private long commentId;
    @Column(name = "board_id")
    private long boardId;
    private String content;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "created_by")
    private long createdBy;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name = "updated_by")
    private long updatedBy;
    @Column(name = "deleted_yn")
    private String deletedYn;
}

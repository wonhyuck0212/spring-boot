package com.elice.agora.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elice.agora.comment.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long>{
    
}

package com.elice.agora.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elice.agora.vote.entity.VoteEntity;

public interface VoteRepository extends JpaRepository<VoteEntity, Long> {

}

package com.elice.agora.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elice.agora.vote.entity.UserVoteEntity;

public interface UserVoteRepository extends JpaRepository<UserVoteEntity, Long> {

}

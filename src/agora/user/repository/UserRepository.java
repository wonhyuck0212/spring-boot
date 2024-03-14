package com.elice.agora.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elice.agora.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    UserEntity findByEmail(String email);

    // 리프레시 토큰 내용 추가
    UserEntity findByRefreshToken(String refreshToke);
}

package com.elice.agora.user.dto;

import java.time.LocalDateTime;

import com.elice.agora.user.entity.UserEntity;
import com.elice.agora.utils.enums.Role;

public class SignUpDTO {

    public String email; 
    
    public String nickname;
    
    public String password;
    
    public String password2;
    
    public UserEntity toEntity(){
        return UserEntity.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                // role, createdAt, updateAt 추가
                .role(Role.MEMBER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}

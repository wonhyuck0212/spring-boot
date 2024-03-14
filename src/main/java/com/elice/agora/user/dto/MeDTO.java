package com.elice.agora.user.dto;

import javax.management.relation.Role;

import com.elice.agora.user.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class MeDTO {
    
    public Long userId;

    public String email;

    public String nickname;

    public String role;

    public static MeDTO getFromUserEntity(UserEntity userEntity) {
        return new MeDTO(
            userEntity.getUserId(),
            userEntity.getEmail(),
            userEntity.getNickname(),
            userEntity.getRole().toString()
        );
    }
}

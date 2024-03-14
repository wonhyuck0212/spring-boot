package com.elice.agora.comment.dto;

import java.time.LocalDateTime;

import com.elice.agora.board.entity.BoardEntity;
import com.elice.agora.comment.entity.CommentEntity;
import com.elice.agora.utils.enums.DeletedYn;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsertCommentDTO {

    public String content;
    
    public CommentEntity toEntity() {
        
        LocalDateTime now = LocalDateTime.now();
        return CommentEntity.builder()
            .content(content)
            .createdAt(now)
            .updatedAt(now)
            .deletedYn(DeletedYn.N)
            .build();
    }
    
}

package com.elice.agora.comment.dto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.elice.agora.board.dto.GetOneBoardDto;
import com.elice.agora.comment.entity.CommentEntity;
import com.elice.agora.board.service.RandomName;
import com.elice.agora.utils.enums.AnonymousYn;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class GetCommentDTO {
    public Long id;
    public String content;
    public Long createdBy;
    public String lastModifiedTime;

    public static GetCommentDTO getFromCommentEntity(CommentEntity commentEntity) {
        

        LocalDate today = LocalDate.now();
        
        DateTimeFormatter formatter;
        if (commentEntity.getUpdatedAt().toLocalDate().equals(today)) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        } else {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }

        String formattedDateTime = commentEntity.getUpdatedAt().format(formatter);

        return new GetCommentDTO(
            commentEntity.getCommentId(),
            commentEntity.getContent(),
            commentEntity.getCreatedBy().getUserId(),
            formattedDateTime
           
        );
    }
}


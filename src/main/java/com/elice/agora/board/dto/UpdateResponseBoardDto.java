package com.elice.agora.board.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.elice.agora.board.entity.BoardEntity;
import com.elice.agora.board.service.RandomName;
import com.elice.agora.utils.enums.AnonymousYn;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResponseBoardDto {
    public Long id;
    public String title;
    public String content;
    // public String author;
    public String lastModifiedTime; // "2024-02-13 17:06:22",
    public String isModified; // "N"

     public static UpdateResponseBoardDto getFromBoardEntity(BoardEntity boardEntity) {
        LocalDate today = LocalDate.now();
        
        DateTimeFormatter formatter;
        if (boardEntity.getUpdatedAt().toLocalDate().equals(today)) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        } else {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }

        String formattedDateTime = boardEntity.getUpdatedAt().format(formatter);

        return new UpdateResponseBoardDto(
            boardEntity.getBoardId(),
            boardEntity.getTitle(),
            boardEntity.getContent(),
            // boardEntity.getAnonymousYn().equals(AnonymousYn.N) 
            //     ? boardEntity.getCreatedBy().getNickname() 
            //     : RandomName.getRandomName(boardEntity.getBoardId()),
            formattedDateTime,
            boardEntity.getCreatedAt().equals(boardEntity.getUpdatedAt())? "N" : "Y"
        );
    }
}

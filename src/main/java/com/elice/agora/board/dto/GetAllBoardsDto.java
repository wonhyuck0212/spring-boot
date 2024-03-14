package com.elice.agora.board.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.elice.agora.board.entity.BoardEntity;
import com.elice.agora.board.service.RandomName;
import com.elice.agora.utils.enums.AnonymousYn;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class GetAllBoardsDto {
    public Long id;
    public String title;
    public String author;
    public String createdTime;
    public boolean deleted;

    public static GetAllBoardsDto getFromBoardEntity(BoardEntity boardEntity) {
        LocalDate today = LocalDate.now();
        
        DateTimeFormatter formatter;
        if (boardEntity.getUpdatedAt().toLocalDate().equals(today)) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        } else {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }

        String formattedDateTime = boardEntity.getUpdatedAt().format(formatter);

        return new GetAllBoardsDto(
            boardEntity.getBoardId(),
            boardEntity.getTitle(),
            boardEntity.getUpdatedBy().getNickname(),
            formattedDateTime,
            boardEntity.getCreatedAt().equals(boardEntity.getUpdatedAt()));
    }
}

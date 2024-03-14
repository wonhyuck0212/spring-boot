package com.elice.agora.board.service;

import com.elice.agora.board.dto.GetAllBoardsDto;
import com.elice.agora.board.entity.BoardEntity;
import com.elice.agora.utils.enums.DeletedYn;

public class BoardEntityMapper {
    public static GetAllBoardsDto entityToDto(BoardEntity boardEntity) {
        return new GetAllBoardsDto(boardEntity.getBoardId(), boardEntity.getTitle(), boardEntity.getUpdatedBy().getNickname(), boardEntity.getUpdatedAt().toString(), boardEntity.getDeletedYn() == DeletedYn.Y);
    }
}

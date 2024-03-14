package com.elice.agora.board.dto;

import java.time.LocalDateTime;

import com.elice.agora.board.entity.BoardEntity;
import com.elice.agora.utils.enums.AnonymousYn;

public class InsertBoardDto {
    public String title;
    public String content;
    public Boolean isAnonymous;

    /* Quest 6 */
    public Boolean isVote;
    public String voteStartAt;
    public String voteEndAt;
    
    public BoardEntity toEntity() {
        LocalDateTime now = LocalDateTime.now();

        return BoardEntity.builder()
            .title(title)
            .content(content)
            .anonymousYn(isAnonymous ? AnonymousYn.Y : AnonymousYn.N)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }
} 

package com.elice.agora.board.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.elice.agora.board.dto.GetAllBoardsDto;
import com.elice.agora.board.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    // @Query("SELECT b.board_id, b.title, b.createdBy, b.updatedAt AS createdTime, b.deleted_yn AS deleted FROM Board b WHERE b.updated_at >= :date AND b.deleteYn = 'N' LIMIT :size OFFSET :offset ORDER BY b.updated_at DESC")
    // List<BoardEntity> findManyBoards(LocalDateTime date, Integer size, Integer offset);


    // Page<BoardEntity> findByUpdatedAtAfterOrderByUpdatedAtAndBoardIdDesc(LocalDateTime date, Pageable pageable);

    // Page<BoardEntity> findByDeletedYNisN(LocalDateTime date, Pageable pageable);



}

package com.elice.agora.board.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elice.agora.board.dto.GetAllBoardsDto;
import com.elice.agora.board.dto.GetOneBoardDto;
import com.elice.agora.board.dto.InsertBoardDto;
import com.elice.agora.board.dto.UpdateRequestBoardDto;
import com.elice.agora.board.dto.UpdateResponseBoardDto;
import com.elice.agora.board.entity.BoardEntity;
import com.elice.agora.board.repository.BoardRepository;
import com.elice.agora.user.entity.UserEntity;
import com.elice.agora.utils.CheckEmpty;
import com.elice.agora.utils.enums.DeletedYn;
import com.elice.agora.utils.enums.Role;
import com.elice.agora.utils.enums.VoteYn;
import com.elice.agora.vote.entity.VoteEntity;
import com.elice.agora.vote.repository.VoteRepository;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final VoteRepository voteRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, VoteRepository voteRepository) { 
        this.boardRepository = boardRepository;
        this.voteRepository = voteRepository;
    }

    public void insertBoard(InsertBoardDto insertBoardDto, UserEntity user) throws Exception {

        CheckEmpty.checkNull(insertBoardDto.isAnonymous, "400 번대 에러 - isAnonymous Null");
        CheckEmpty.checkNull(insertBoardDto.title, "400 번대 에러 - title Null");
        CheckEmpty.checkNull(insertBoardDto.content, "400 번대 에러 - content Null");

        if (!BoardCheck.checkSwearWords(insertBoardDto.title) || !BoardCheck.checkSwearWords(insertBoardDto.content)) {
            throw new Exception("400 번대 에러 - 욕설 포함");
        }
        if (insertBoardDto.title.length() > 200) {
            throw new Exception("400 번대 에러 - 제목 글자수 제한 초과");
        }

        BoardEntity boardEntity = insertBoardDto.toEntity();
        boardEntity.setCreatedBy(user);
        boardEntity.setUpdatedBy(user);
        boardEntity.setDeletedYn(DeletedYn.N);

        /* Quest 6 */
        CheckEmpty.checkNull(insertBoardDto.isVote, "400 번대 에러 - isVote Null");

        if (insertBoardDto.isVote) {
            CheckEmpty.checkNull(insertBoardDto.voteStartAt, "400 번대 에러 - voteStartAt Null");
            CheckEmpty.checkNull(insertBoardDto.voteEndAt, "400 번대 에러 - voteStartAt Null");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date now = new Date();
            Date parsedStartDate = dateFormat.parse(insertBoardDto.voteStartAt);
            Date parsedEndDate = dateFormat.parse(insertBoardDto.voteEndAt);
    
            if (parsedStartDate.compareTo(now) > 0) {
                throw new Exception("voteStartAt은 현재와 동일하거나 과거여야 하며, 그렇지 않다면 400번대 에러를 발생시킨다.");
            }
            if (parsedEndDate.compareTo(now) < 0) {
                throw new Exception("voteEndAt은 현재와 동일하거나 미래여야 하며, 그렇지 않다면 400번대 에러를 발생시킨다.");
            }

            boardEntity.setVoteYn(VoteYn.Y);
            boardEntity.setVoteStartAt(parsedStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            boardEntity.setVoteEndAt(parsedEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

            boardRepository.save(boardEntity);
            VoteEntity voteEntity = VoteEntity.builder()
                    .boardId(boardEntity)
                    .createdAt(now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .createdBy(user)
                    .updatedAt(now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .updatedBy(user)
                    .build();
            voteRepository.save(voteEntity);
            
        } else {

            boardEntity.setVoteYn(VoteYn.N);
            boardRepository.save(boardEntity);
        }
    }

    public GetOneBoardDto getOneBoardById(Long id, UserEntity user) throws Exception  {

        Optional<BoardEntity> boardOptional = boardRepository.findById(id);
        if (!boardOptional.isPresent()) {
            throw new Exception("해당 ID를 가진 게시글이 존재하지 않습니다.");
        }

        BoardEntity board = boardOptional.get();
        if (user.getRole() == Role.MEMBER && board.getDeletedYn() == DeletedYn.Y) {
            throw new Exception("이미 삭제된 게시물입니다.");
        }
        return GetOneBoardDto.getFromBoardEntity(board);
    }

    public List<GetAllBoardsDto> getManyBoards(LocalDateTime date, Pageable pageable, UserEntity user) throws Exception  {
        // Page<BoardEntity> boardEntities = boardRepository.findByUpdatedAtAfter(date, pageable);
        Page<BoardEntity> boardEntities;
        // if (user.getRole().equals(Role.ADMIN)) {
            // boardEntities = boardRepository.findByUpdatedAtAfterOrderByUpdatedAtAndBoardIdDesc(date, pageable);
        // } else {
        //     boardEntities = boardRepository.findByDeletedYNisN(date, pageable);
        // }

        boardEntities = boardRepository.findAll(pageable);

        List<GetAllBoardsDto> dtos = new ArrayList<>();

        for (BoardEntity entity : boardEntities.getContent()) {
            GetAllBoardsDto dto = GetAllBoardsDto.getFromBoardEntity(entity); // 엔티티를 DTO로 변환
            dtos.add(dto);
        }

        return dtos;
    }

    @Transactional
    public UpdateResponseBoardDto updateBoard(Long id, UpdateRequestBoardDto updateRequestBoardDto, UserEntity user) throws Exception  {
        CheckEmpty.checkNull(updateRequestBoardDto.title, "400 번대 에러 - Null임");
        CheckEmpty.checkNull(updateRequestBoardDto.content, "400 번대 에러 - Null임");
        if (!BoardCheck.checkSwearWords(updateRequestBoardDto.title) || !BoardCheck.checkSwearWords(updateRequestBoardDto.content)) {
            throw new Exception("400 번대 에러 - 욕설 포함");
        }
        if (updateRequestBoardDto.title.length() > 200) {
            throw new Exception("400 번대 에러 - 제목 글자수 제한 초과");
        }

        BoardEntity boardEntity = boardRepository.findById(id).orElse(null);
   
        if (boardEntity == null) {
            throw new Exception("엔티티가 없습니다.");
        }
        if (user.getRole() == Role.MEMBER) {
            if (!boardEntity.getUpdatedBy().equals(user)) {
                throw new Exception("권한이 없습니다.");
            }
            if (boardEntity.getDeletedYn() == DeletedYn.Y) {
                throw new Exception("이미 삭제된 게시물입니다.");
            }
        }
        
        boardEntity.setTitle(updateRequestBoardDto.title);
        boardEntity.setContent(updateRequestBoardDto.content);
        boardEntity.setUpdatedAt(LocalDateTime.now());
        boardEntity.setUpdatedBy(user);
        boardRepository.save(boardEntity);
        boardRepository.flush();


        UpdateResponseBoardDto result = UpdateResponseBoardDto.getFromBoardEntity(boardEntity);
        return result;
    }

    public void deleteBoard(Long id, UserEntity user) throws Exception {
        BoardEntity boardEntity = boardRepository.findById(id).orElse(null);
        if (boardEntity == null) {
            throw new Exception("게시물이 없습니다.");
        }
        if (user.getRole() == Role.MEMBER) {
            if (!boardEntity.getUpdatedBy().equals(user)) {
                throw new Exception("권한이 없습니다.");
            }
            if (boardEntity.getDeletedYn() == DeletedYn.Y) {
                throw new Exception("이미 삭제된 게시물입니다.");
            }
        }
        
        boardEntity.setDeletedYn(DeletedYn.Y);
        boardRepository.save(boardEntity);
    }
}

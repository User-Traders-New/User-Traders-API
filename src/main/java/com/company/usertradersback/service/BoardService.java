package com.company.usertradersback.service;

import com.company.usertradersback.dto.BoardCategoryDto;
import com.company.usertradersback.dto.BoardDto;
import com.company.usertradersback.dto.BoardSubCategoryDto;
import com.company.usertradersback.entity.BoardCategoryEntity;
import com.company.usertradersback.entity.BoardEntity;
import com.company.usertradersback.entity.BoardSubCategoryEntity;
import com.company.usertradersback.entity.UserEntity;
import com.company.usertradersback.exception.user.ApiIllegalArgumentException;
import com.company.usertradersback.exception.user.ApiNullPointerException;
import com.company.usertradersback.repository.BoardCategoryRepository;
import com.company.usertradersback.repository.BoardRepository;
import com.company.usertradersback.repository.BoardSubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardCategoryRepository boardCategoryRepository;

    @Autowired
    private BoardSubCategoryRepository boardSubCategoryRepository;

    @Autowired
    private UserService userService;

    //게시물 전체 조회
    @Transactional
    public List<BoardDto> listAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDto> results = boardEntityList.stream().map(boardEntity -> {
            BoardDto boardDto = BoardDto.builder()
                    .build().convertEntityToDto(boardEntity);
            return boardDto;
        }).collect(Collectors.toList());
        return results;
    }

    // 무한 스크롤 페이지 네이션
    @Transactional
    public List<BoardDto> listAllInfinite(Integer limit) {
        //전체 게시물을 desc 순으로 (날짜)큰것 -> (날짜)작은것 순으로 3개씩 나눈다. page는 0부터 시작.
        //defaultValue가 1이기 때문에 , page는 limit - 1
        Page<BoardEntity> page = boardRepository.findAll(PageRequest.of(limit - 1, 3, Sort.by(Sort.Direction.DESC, "createAt")));
        List<BoardDto> results = page.stream().map(boardEntity -> {
            BoardDto boardDto = BoardDto.builder()
                    .build().convertEntityToDto(boardEntity);
            return boardDto;
        }).collect(Collectors.toList());
        return results;
    }

    //게시물 id를 통해 게시물 목록 1개 조회
    @Transactional
    public BoardDto listDetail(Integer id) {
        BoardEntity boardEntity = boardRepository.findById(id)
                .orElseThrow(() -> new ApiIllegalArgumentException("해당되는 게시물 번호 " + id + " 값의 상세정보가 없습니다."));
        BoardDto board = BoardDto.builder().build().convertEntityToDto(boardEntity);
        return board;
    }

    //검색 keyword를 통해 게시물 title에 keword가 속한 게시물 목록 조회
    @Transactional
    public List<BoardDto> searchTitle(String keyword) {
        List<BoardEntity> boardEntityList = boardRepository.findByTitleContaining(keyword);
        List<BoardDto> results = boardEntityList.stream().map(boardEntity -> {
            BoardDto boardDto = BoardDto.builder()
                    .build().convertEntityToDto(boardEntity);
            return boardDto;
        }).collect(Collectors.toList());
        return results;
    }
    //대분류 카테고리 + 서브카테고리를 통해 해당 게시물 전체 목록 조회
    @Transactional
    public List<BoardDto> searchCategory(Integer categoryId,Integer subCategoryId) {
        List<BoardEntity> boardEntityList = boardRepository.selectAll(categoryId,subCategoryId);
        List<BoardDto> results = boardEntityList.stream().map(boardEntity -> {
            BoardDto boardDto = BoardDto.builder()
                    .build().convertEntityToDto(boardEntity);
            return boardDto;
        }).collect(Collectors.toList());

        return results;
    }
    //회원 정보를 이용하여 해당 회원의 게시물 전체 목록 조회
    @Transactional
    public List<BoardDto> listMyBoards(UserEntity userEntity){
        if (userEntity == null) {
            throw new ApiNullPointerException("유저정보가 없습니다.");
        }
        List<BoardEntity> userBoardList = boardRepository.findAllByUserId_Id(userEntity.getId());
        List<BoardDto> results = userBoardList.stream().map(boardEntity -> {
            BoardDto boardDto = BoardDto.builder()
                    .build().convertEntityToDto(boardEntity);
            return boardDto;
        }).collect(Collectors.toList());
        return results;
    }

    //해당 userId를 통해서 해당 회원의 게시물 전체 목록 조회
    @Transactional
    public List<BoardDto> findAllByUserId(Integer userId) {

        List<BoardEntity> userBoardList = boardRepository.findAllByUserId_Id(userId);
        List<BoardDto> results = userBoardList.stream().map(boardEntity -> {
            BoardDto boardDto = BoardDto.builder()
                    .build().convertEntityToDto(boardEntity);
            return boardDto;
        }).collect(Collectors.toList());
        return results;
    }
    //대분류 카테고리 조회
    @Transactional
    public List<BoardSubCategoryDto> listSubCategoryId(){
        List<BoardSubCategoryEntity> categoryEntityList = boardSubCategoryRepository.findAll();

        List<BoardSubCategoryDto> results = categoryEntityList.stream().map(categoryEntity -> {
            BoardSubCategoryDto categoryDto = BoardSubCategoryDto.builder()
                    .id(categoryEntity.getId())
                    .name(categoryEntity.getName())
                    .build();
            return categoryDto;
        }).collect(Collectors.toList());

        return results;
    }

    //서브 카테고리 조회
    @Transactional
    public List<BoardCategoryDto> listCategoryId(Integer subCategoryId){
        List<BoardCategoryEntity> categoryEntityList = boardCategoryRepository.findAllBySubCategoryId_Id(subCategoryId);

        List<BoardCategoryDto> results = categoryEntityList.stream().map(categoryEntity -> {
            BoardCategoryDto categoryDto = BoardCategoryDto.builder()
                    .id(categoryEntity.getId())
                    .subCategoryId(categoryEntity.getSubCategoryId())
                    .name(categoryEntity.getName())
                    .build();
            return categoryDto;
        }).collect(Collectors.toList());

        return results;
    }


//    @Transactional
//    public Integer save(BoardDto boardDto, List<MultipartFile> files, UserEntity user) { // 한 객체를 보드 테이블에 저장, 파일까지 저장
//        String baseDir = "\\home\\ec2-user\\apps\\IC-Capstone-User-Traders\\UserTraders-frontend\\src\\assets\\images\\";
//        String[] fileName = new String[3];
//        if (files != null) {
//            try {
//                for (int i = 0; i < files.size(); i++) {
//                    fileName[i] = files.get(i).getOriginalFilename();
//                    //transferTo() 를 사용하면 파일데이터를 지정한 파일로 저장.
//                    files.get(i).transferTo(new File(baseDir + files.get(i).getOriginalFilename()));
//                }
//            } catch (IllegalStateException | IOException e) {
//                e.printStackTrace();
//            }
//        }
//        boardDto.setImageurl1(fileName[0]);
//        boardDto.setImageurl2(fileName[1]);
//        boardDto.setImageurl3(fileName[2]);
//        boardDto.setUser(user);
//        boardDto.setStatus(true);
//
//        BoardEntity boardEntity = boardDto.convertDtoToEntity();
//        return boardRepository.save(boardEntity).getId();
//    }
//
//    @Transactional
//    public Integer save(BoardDto boardDto) { // 한 객체를 보드 테이블에 저장
//        BoardEntity boardEntity = boardDto.convertDtoToEntity();
//        return boardRepository.save(boardEntity).getId();
//    }
//
//
//
//    @Transactional
//    public Integer updateById(BoardDto boardDto, Integer id) {
//        BoardDto board = this.findById(id);
//
//        // 요청 받은 수정할 객체 정보를 건내받고 , 그 객체의 아이디를 뽑아서  수정전 정보를 wrapper에 담고
//        // 수정 할 객체 정보를 수정전 객체 정보에 저장 ,  수정 끝
//        //Optinal 클래스의 ifPresent 함수의 사용: 수정값에 null이 있는지 확인하는 if문을 줄이기 위함
//        Optional<BoardEntity> boardEntityWrapper = boardRepository.findById(boardDto.getId());
//        boardEntityWrapper.ifPresent(boardEntity -> {
//            boardEntity = BoardEntity.builder()
//                    .id(boardDto.getId())
//                    .title(boardDto.getTitle())
//                    .content(boardDto.getContent())
//                    .price(boardDto.getPrice())
//                    .imageurl1(board.getImageurl1())
//                    .imageurl2(board.getImageurl2())
//                    .imageurl3(board.getImageurl3())
//                    .category(board.getCategory())
//                    .user(board.getUser())
//                    .status(board.getStatus()).build();
//            boardRepository.save(boardEntity);
//        });
//        return boardEntityWrapper.get().getId();
//    }

    @Transactional
    public void deleteById(Integer id) {
        if (id == null) {
            throw new ApiNullPointerException("삭제하려는 게시물" + id + "아이디가 없습니다.");
        }
        boardRepository.deleteById(id);
    }

}

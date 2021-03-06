package com.example.spring.service;

import com.example.spring.advice.exception.NotOwnerException;
import com.example.spring.advice.exception.ResourceNotExistException;
import com.example.spring.advice.exception.UserNotFoundException;
import com.example.spring.common.CacheKey;
import com.example.spring.entity.Board;
import com.example.spring.entity.Post;
import com.example.spring.entity.User;
import com.example.spring.model.board.ParamPost;
import com.example.spring.repository.BoardRepository;
import com.example.spring.repository.PostRepository;
import com.example.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CacheService cacheService;

    // 게시판 이름으로 게시판을 조회 없을 경우 ResourceNotExistException
    @Cacheable(value = CacheKey.BOARD, key = "#boardName", unless = "#result== null") // 캐시가 존재하면 메서드를 실행하지 않고 캐시된 값이 반환
    public Board findBoard(String boardName){                                         // 캐시가 존재하지 않으면 메서드가 실행되고 리턴되는 데이터가 캐시에 저장
        return Optional.ofNullable(boardRepository.findByName(boardName)).orElseThrow(ResourceNotExistException::new);
    }

    // 게시판 이름으로 게시물 리스트 조회
    @Cacheable(value = CacheKey.POSTS, key = "#boardName", unless = "#result== null")
    public List<Post> findPosts(String boardName){
        return postRepository.findByBoard(findBoard((boardName)));
    }

    // 게시물 ID로 게시물 단건 조회, 없을 경우 ResourceNotExistException
    public Post getPost(Long postId){
        return postRepository.findById(postId).orElseThrow(ResourceNotExistException::new);
    }

    //  게시물을 등록. 게시물의 회원 Uid가 조회되지 않으면 UserNotFountException 처리
    // 게시글 등록 후 캐시 삭제
    @CacheEvict(value = CacheKey.POSTS,key = "#boardName") // 게시글 1건을 등록할 경우 게시글 리스트 캐시를 초기화해야하므로 캐시를 삭제
    public Post writePost(String uid, String boardname, ParamPost paramPost){
        Board board = findBoard(boardname);
        Post post = new Post(userRepository.findByUid(uid).orElseThrow(UserNotFoundException::new),board,paramPost.getAuthor(), paramPost.getTitle(), paramPost.getContent());
        return postRepository.save(post);
    }

    // 게시물을 수정, 게시물 등록자와 로그인 회원정보가 틀리면 NotOwnerException을 처리
    // @CachePut(value = CacheKey.POST,key = "#postId") 갱신된 정보만 캐시할 경우에만 사용!
    public Post updatePost(Long postId,String uid,ParamPost paramPost){
        Post post = getPost(postId);
        User user = post.getUser();
        if(!uid.equals(user.getUid()))
            throw new NotOwnerException();

        // 영속성 컨텍스트의 변경 감지(dirty checking) 기능에 의해 조회한 Post내용을 변경만 해도 Update쿼리가 실행
        post.setUpdate(paramPost.getAuthor(),paramPost.getTitle(),paramPost.getContent());

        cacheService.deleteBoardCache(post.getPostId(), post.getBoard().getName());

        return post;
    }

    // 게시물을 삭제 , 게시물 등록자와 로그인 회원정보가 틀리면 NotOwnerException 처리
    public boolean deletePost(Long postId, String uid){
        Post post = getPost(postId);
        User user = post.getUser();
        if(!uid.equals(user.getUid()))
            throw new NotOwnerException();
        postRepository.delete(post);
        cacheService.deleteBoardCache(post.getPostId(), post.getBoard().getName());
        return true;
    }

}

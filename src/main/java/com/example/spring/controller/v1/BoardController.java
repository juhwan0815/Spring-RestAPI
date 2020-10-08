package com.example.spring.controller.v1;

import com.example.spring.entity.Board;
import com.example.spring.entity.Post;
import com.example.spring.model.board.ParamPost;
import com.example.spring.model.response.CommonResult;
import com.example.spring.model.response.ListResult;
import com.example.spring.model.response.SingleResult;
import com.example.spring.service.BoardService;
import com.example.spring.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"3, Board"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/board")
public class BoardController {

    private final BoardService boardService;
    private final ResponseService responseService;

    @ApiOperation(value = "게시판 정보 조회",notes = "게시판 정보를 조회한다.")
    @GetMapping("/{boardName}")
    public SingleResult<Board> boardInfo(@PathVariable String boardName){
        return responseService.getSingleResult(boardService.findBoard(boardName));
    }

    @ApiOperation(value = "게시판 글 리스트",notes = "게시판 게시글 리스트를 조회한다.")
    @GetMapping("/{boardName}/posts")
    public ListResult<Post> posts(@PathVariable String boardName){
        return responseService.getListResult(boardService.findPosts(boardName));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공 후 access_token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value = "게시판 글 작성",notes = "게시판에 글을 작성한다.")
    @PostMapping("/{boardName}")
    public SingleResult<Post> post(@PathVariable String boardName, @Valid @ModelAttribute ParamPost post){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        return responseService.getSingleResult(boardService.writePost(uid,boardName,post));
    }

    @ApiOperation(value = "게시판 글 상세",notes = "게시판 글 상세정보를 조회한다")
    @GetMapping("/post/{postId}")
    public SingleResult<Post> post(@PathVariable Long postId){
        return responseService.getSingleResult(boardService.getPost(postId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공 후 access_token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value = "게시판 글 수정",notes = "게시판의 글을 수정한다")
    @PutMapping("/post/{postId}")
    public SingleResult<Post> post(@PathVariable Long postId, @Valid @ModelAttribute ParamPost post){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid= authentication.getName();
        return responseService.getSingleResult(boardService.updatePost(postId,uid,post));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공 후 access_token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value = "게시판 글 삭제",notes = "게시판의 글을 삭제")
    @DeleteMapping("/post/{postId}")
    public CommonResult deletePost(@PathVariable long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid =authentication.getName();
        boardService.deletePost(postId,uid);
        return responseService.getSuccessResult();
    }
}

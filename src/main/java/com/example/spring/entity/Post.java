package com.example.spring.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false,length = 50)
    private String author;

    @Column(nullable = false,length = 100)
    private String title;

    @Column(length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    // Join 테이블이 Json결과에 표시되지 않도록 처리
    protected Board getBoard(){
        return board;
    }

    // 생성자
    public Post(User user, Board board, String author, String title,String content){
        this.user = user;
        this.board = board;
        this.author = author;
        this.title = title;
        this.content = content;
    }

    // 수정 시 데이터 처리
    public Post setUpdate(String author,String title,String content){
        this.author = author;
        this.title = title;
        this.content = content;
        return this;
    }


}

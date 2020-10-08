package com.example.spring.repository;

import com.example.spring.entity.Board;
import com.example.spring.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByBoard(Board board);
}

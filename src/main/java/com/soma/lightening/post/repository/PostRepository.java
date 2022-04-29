package com.soma.lightening.post.repository;

import com.soma.lightening.post.domain.Post;
import com.soma.lightening.post.domain.PostTag;
import com.soma.lightening.post.domain.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long>{
    public Optional<Post> findById(Long id);

    public Page<Post> findAll(Pageable pageable);
    public Page<Post> findAllByPostTag(PostTag postTag, Pageable pageable);
    public Page<Post> findAllByPostType(PostType postType, Pageable pageable);
    public Page<Post> findAllByPostTagAndPostType(PostTag postTag, PostType postType, Pageable pageable);
}

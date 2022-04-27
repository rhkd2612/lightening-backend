package com.soma.lightening.post.domain;

import com.soma.lightening.common.entity.OAuth2Account;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name="like")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="like_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="account_id")
    private OAuth2Account account;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date date = new Date();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(name = "like_type")
    private LikeType likeType; // PARTICIPATE, WILLING

    public static Like newLike(OAuth2Account account, Post post, LikeType likeType) {
        Like like = new Like();
        like.setAccount(account);
        like.setLikeType(likeType);
        like.setPost(post);
        return like;
    }
}

package com.soma.lightening.post.domain;

import com.soma.lightening.common.entity.OAuth2Account;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 원래 @Setter는 지양
@Entity(name = "post")
@Getter
@Setter
public class Post {
    @Id
    @Column(name="post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주인 엔티티 - Post
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="account_id")
    private OAuth2Account account;

    // 주인 엔티티 - Like
    @OneToMany(mappedBy="post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Like> likeList = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(name = "post_type")
    private PostType postType; // RECRUIT, COMPLETED

    @Enumerated(value = EnumType.STRING)
    @Column(name = "post_tag")
    private PostTag postTag; // MEAL, COFFEE, ALCOHOL, GAME, ETC, // 밥 커피 술 게임 기타

    @Column(name = "max_count")
    private Integer maxCount;

    @Column(name = "post_content")
    private String postContent;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate // 시간생성
    private Date date = new Date(); // 게시 시간

    @Column(name = "recruit_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recruitEndDate; // 모집 마감시간

    @Column(name = "appointment_date")
    private String appointmentDate; // 약속 시간, 스트링임

    public static Post newPost(OAuth2Account account, String appointmentDate, PostTag postTag, Date recruitEndDate, String postContent, int maxCount){
        Post post = new Post();
        post.setAccount(account);
        post.setAppointmentDate(appointmentDate);
        post.setPostTag(postTag);
        post.setRecruitEndDate(recruitEndDate);
        post.setMaxCount(maxCount);
        post.setPostContent(postContent);

        post.setPostType(PostType.RECRUIT);

        return post;
    }
}
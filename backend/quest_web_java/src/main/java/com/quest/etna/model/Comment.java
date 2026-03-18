package com.quest.etna.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="comment_id")
    private Integer id;

    @Column(nullable=false, length = 255)
    private String message;

    @Column(columnDefinition = "DATETIME(0)")
    private LocalDateTime createdDate;

    @Column(columnDefinition = "DATETIME(0)")
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    @ManyToOne
    @JoinColumn(name = "snippet_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "snippet")
    private Snippet snippet;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}

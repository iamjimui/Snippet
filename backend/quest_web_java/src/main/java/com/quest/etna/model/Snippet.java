package com.quest.etna.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name="snippet")
public class Snippet {

    @Id
    @Column(name = "snippet_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonProperty("user")
    private User user;


    @Column(unique = true, nullable=false, length = 255)
    private String title;

    private Boolean visible;

    @OneToMany(mappedBy = "snippet", cascade = CascadeType.ALL)
    private List<Sheet> sheets;

    @OneToMany(mappedBy = "snippet", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "snippet", cascade = CascadeType.ALL)
    private List<Favorite> favorites;

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

    @ManyToMany
    @JoinTable( name = "snippet_tag",
            joinColumns = @JoinColumn( name = "snippet_id" ),
            inverseJoinColumns = @JoinColumn( name = "tag_id" ) )
            private List<Tag> tags = new ArrayList<Tag>();

}

package com.quest.etna.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "language_id")
    private Integer id;

    @Column(name = "name", unique = true, nullable=false, length = 255)
    private String name;

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
}

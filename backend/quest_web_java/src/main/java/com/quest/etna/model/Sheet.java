package com.quest.etna.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Sheet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sheet_id")
    private Integer id;

    @Column(nullable=false)
    private String content;

    @Column(nullable=false)
    private String name;

    @ManyToOne
    @JoinColumn(name="language_id")
    @JsonProperty("language")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "snippet_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "snippet")
    private Snippet snippet;

}

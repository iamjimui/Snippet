package com.quest.etna.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name="user")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="user_id")
	private Integer id;
	
	@Column(name = "username", unique = true, nullable=false, length = 255)
	private String username;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "password", nullable=false, length = 255)
	private String password;
	
	@Enumerated(EnumType.STRING)
    private UserRole role;
	
    @Column(columnDefinition = "DATETIME(0)")
	private LocalDateTime createdDate;
	
    @Column(columnDefinition = "DATETIME(0)")
	private LocalDateTime updatedDate;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private List<Snippet> snippetList = new ArrayList<>();

	@PrePersist
	protected void onCreate() {
		this.createdDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	}
	
	@Enumerated(EnumType.STRING)
	public UserRole getRole() {
		return role;
	}

}

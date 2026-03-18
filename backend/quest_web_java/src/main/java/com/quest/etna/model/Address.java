package com.quest.etna.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity(name="address")
public class Address {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable=false, length = 100)
	private String street;
	
	@Column(nullable=false, length = 30)
	private String postalCode;
	
	@Column(nullable=false, length = 50)
    private String city;
	
	@Column(nullable=false, length = 50)
    private String country;
	
	@ManyToOne()
	@JoinColumn(nullable=false, name="user_id", referencedColumnName = "user_id")
    private User user;
	
    @Column(columnDefinition = "DATETIME(0)")
	private LocalDateTime creationDate;
	
    @Column(columnDefinition = "DATETIME(0)")
	private LocalDateTime updatedDate;
	
	public Address() {}
	
	
	
	public Address(Integer id, String street, String postalCode, String city, String country, User user,
			LocalDateTime creationDate, LocalDateTime updatedDate) {
		super();
		this.id = id;
		this.street = street;
		this.postalCode = postalCode;
		this.city = city;
		this.country = country;
		this.user = user;
		this.creationDate = creationDate;
		this.updatedDate = updatedDate;
	}



	@PrePersist
	protected void onCreate() {
		this.creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	
}

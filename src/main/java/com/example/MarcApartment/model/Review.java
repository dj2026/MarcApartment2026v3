package com.example.MarcApartment.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDate;

@Entity
@Table(name = "reviews")
public class Review {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String comment;
    private int rating;
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "reviewer_id") private Reviewer reviewer;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "apartment_id") @JsonBackReference private Apartment apartment;

    public Review() {}

    public Review(String title, String comment, int rating, LocalDate date) {this.title = title;this.comment = comment;this.rating = rating;this.date = date;}

    public Long getId() {return id;} public void setId(Long id) {this.id = id;}
    public String getTitle() {return title;} public void setTitle(String title) {this.title = title;}
    public String getComment() {return comment;} public void setComment(String comment) {this.comment = comment;}
    public int getRating() {return rating;} public void setRating(int rating) {this.rating = rating;}
    public LocalDate getDate() {return date;} public void setDate(LocalDate date) {this.date = date;}
    public Reviewer getReviewer() {return reviewer;} public void setReviewer(Reviewer reviewer) {this.reviewer = reviewer;}
    public Apartment getApartment() {return apartment;} public void setApartment(Apartment apartment) {this.apartment = apartment;}
}
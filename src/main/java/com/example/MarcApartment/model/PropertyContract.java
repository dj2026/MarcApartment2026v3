package com.example.MarcApartment.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "property_contracts")
public class PropertyContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH}) 
    @JoinColumn(name = "owner_id")  
    @JsonBackReference 
    private Owner owner;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH}) 
    @JoinColumn(name = "apartment_id") 
    private Apartment apartment;

    private String contractDetails; 
    private LocalDate contractDate; 
    private Double finalPrice;

    public PropertyContract(){}

    public PropertyContract(Owner owner, Apartment apartment, String contractDetails, LocalDate contractDate, Double finalPrice){
        this.owner = owner; this.apartment = apartment; this.contractDetails = contractDetails; this.contractDate = contractDate; this.finalPrice = finalPrice;
    }

    public Long getId(){return id;} public void setId(Long id){this.id = id;}
    public Owner getOwner(){return owner;} public void setOwner(Owner owner){this.owner = owner;}
    public Apartment getApartment(){return apartment;} public void setApartment(Apartment apartment){this.apartment = apartment;}
    public String getContractDetails(){return contractDetails;} public void setContractDetails(String details){this.contractDetails = details;}
    public LocalDate getContractDate(){return contractDate;} public void setContractDate(LocalDate date){this.contractDate = date;}
    public Double getFinalPrice(){return finalPrice;} public void setFinalPrice(Double price){this.finalPrice = price;}
}
package com.example.MarcApartment.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("OWNER")
public class Owner extends Person {
    
    private Integer age; private Boolean isActive; private Boolean isBusiness; private String idLegalOwner; private LocalDate registrationDate; private Integer qtyDaysAsOwner;

    @OneToMany(mappedBy="owner", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
    @JsonManagedReference
    private List<PropertyContract> contracts = new ArrayList<>();

    public Owner() {super();}

    public Owner(String name, String email, Integer age, Boolean isActive, Boolean isBusiness, String idLegal, LocalDate regDate, Integer days) {
        this.setName(name); this.setEmail(email);this.age = age;this.isActive = isActive;this.isBusiness = isBusiness;this.idLegalOwner = idLegal;
        this.registrationDate = regDate;this.qtyDaysAsOwner = days;
    }

    public void addContract(PropertyContract contract) {this.contracts.add(contract); contract.setOwner(this);}
    public List<PropertyContract> getContracts() {return contracts;} public void setContracts(List<PropertyContract> contracts) {this.contracts = contracts;}
    public Integer getAge() {return age;}  public void setAge(Integer age) {this.age = age;}
    public Boolean getIsActive() {return isActive;} public void setIsActive(Boolean isActive) {this.isActive = isActive;}
    public Boolean getIsBusiness() {return isBusiness;} public void setIsBusiness(Boolean isBusiness) {this.isBusiness = isBusiness;}
    public String getIdLegalOwner() {return idLegalOwner;} public void setIdLegalOwner(String idLegalOwner) {this.idLegalOwner = idLegalOwner;}
    public LocalDate getRegistrationDate() {return registrationDate;} public void setRegistrationDate(LocalDate registrationDate) {this.registrationDate = registrationDate;}
    public Integer getQtyDaysAsOwner() {return qtyDaysAsOwner;} public void setQtyDaysAsOwner(Integer qtyDaysAsOwner) {this.qtyDaysAsOwner = qtyDaysAsOwner;}

    @Override 
    public String toString() {return "Owner{id=" + getId() + ", name='" + getName() + "', email='" + getEmail() + "', age=" + age + "}";}
}
package com.example.MarcApartment.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "schools")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; private String name; private String schoolType; private String address; private String distance; private String educationLevel; boolean isPublic;

   @ManyToMany(mappedBy = "schools") @JsonIgnore private List<Apartment> apartments = new ArrayList<>();

    public School() {}

    public School(Long id, String name, String schoolType, String address, String distance, String educationLevel, boolean isPublic) {this.id = id; this.name = name; this.schoolType = schoolType; this.address = address; this.distance = distance; this.educationLevel = educationLevel;this.isPublic = isPublic;}

    public Long getId() {return id;} public void setId(Long id) {this.id = id;}
    public String getName() {return name;} public void setName(String name) {this.name = name;}
    public String getSchoolType() {return schoolType;} public void setSchoolType(String type) {this.schoolType = type;}
    public String getAddress() {return address;} public void setAddress(String addr) {this.address = addr;}
    public String getDistance() {return distance;} public void setDistance(String distance) {this.distance = distance;}
    public String getEducationLevel() {return educationLevel;} public void setEducationLevel(String level) {this.educationLevel = level;}
    public boolean isPublic() {return isPublic;} public void setPublic(boolean isPub) {this.isPublic = isPub;}    public List<Apartment> getApartments() {return apartments;} public void setApartments(List<Apartment> apartments) {this.apartments = apartments;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        School school = (School) o;
        return Objects.equals(id, school.id);
    }

    @Override public int hashCode() {return Objects.hash(id);}
}
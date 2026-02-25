package com.example.MarcApartment.model;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="apartments")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="apartment_type")
@DiscriminatorValue("APARTMENT")
@JsonPropertyOrder({"id","propertyType","price","area","bedrooms","bathrooms","stories","mainroad","guestroom","basement","hotwaterheating","airconditioning","terrace","parking","prefarea","furnishingstatus","description","staticReview","reviewCount","reviews","schools"})
public class Apartment {

    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; private String propertyType; private Long price; private int area; private int bedrooms; private int bathrooms; private int stories; private int parking; private String mainroad; private String guestroom;
    private String basement;private String hotwaterheating; private String airconditioning;  private String terrace; private String prefarea; private String furnishingstatus;
    
    @Column(length=1000) 
    private String description; private String staticReview;

    @OneToMany(mappedBy="apartment", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
    @JsonManagedReference
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy="apartment", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonIgnore 
    private List<PropertyContract> propertyContracts = new ArrayList<>();

    @ManyToMany(cascade={CascadeType.MERGE}) 
    @JoinTable(name="apartment_school",joinColumns=@JoinColumn(name="apartment_id"), inverseJoinColumns=@JoinColumn(name="school_id")
    )
    @JsonIgnoreProperties("apartments")
    private List<School> schools = new ArrayList<>();

    public Apartment(){}

    public Apartment(Long id, String propertyType, Long price, int area, int bedrooms, int bathrooms, int stories, String mainroad, String guestroom, String basement, String hotwater, String aircon, 
        String terrace, int parking, String prefarea, String furnish, String description, String staticReview){
        this.id = id;this.propertyType = propertyType;this.price = price;this.area = area;this.bedrooms = bedrooms;this.bathrooms = bathrooms;this.stories = stories;this.mainroad = mainroad;this.guestroom = guestroom;
        this.basement = basement;this.hotwaterheating = hotwater;this.airconditioning = aircon;this.terrace = terrace;this.parking = parking;this.prefarea = prefarea;this.furnishingstatus = furnish;
        this.description = description;this.staticReview = staticReview;this.reviews = new ArrayList<>(); 
        if (this.price == null || this.price == 0L) {this.calcularPreuAutomatic();}
    }

    @JsonProperty("reviewCount") public int getReviewCount() {return (this.reviews != null) ? this.reviews.size() : 0;}

    @JsonIgnore public String getTitle() {return this.propertyType;}

    public void calcularPreuAutomatic() {
        long c = (long) this.area * 2000L;
        c += (this.bedrooms * 1500L);
        c += (this.bathrooms * 1000L);
        if ("yes".equalsIgnoreCase(this.airconditioning)) { c += 5000L; }
        if ("yes".equalsIgnoreCase(this.terrace)) { c += 8000L; }
        if (this.parking > 0) { c += (this.parking * 5000L); }
        if ("furnished".equalsIgnoreCase(this.furnishingstatus)) { c += 2000L; }
        else if ("semi-furnished".equalsIgnoreCase(this.furnishingstatus)) { c += 1000L; }
        this.price = c;
    }

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getPropertyType(){return propertyType;} public void setPropertyType(String pt){this.propertyType=pt;}
    public Long getPrice(){return price;} public void setPrice(Long price){this.price=price;}
    public int getArea(){return area;} public void setArea(int area){this.area=area;}
    public int getBedrooms(){return bedrooms;} public void setBedrooms(int b){this.bedrooms=b;}
    public int getBathrooms(){return bathrooms;} public void setBathrooms(int b){this.bathrooms=b;}
    public int getStories(){return stories;} public void setStories(int s){this.stories=s;}
    public int getParking(){return parking;} public void setParking(int p){this.parking=p;}
    public String getMainroad(){return mainroad;} public void setMainroad(String m){this.mainroad=m;}
    public String getGuestroom(){return guestroom;} public void setGuestroom(String g){this.guestroom=g;}
    public String getBasement(){return basement;} public void setBasement(String b){this.basement=b;}
    public String getHotwaterheating(){return hotwaterheating;} public void setHotwaterheating(String h){this.hotwaterheating=h;}
    public String getAirconditioning(){return airconditioning;} public void setAirconditioning(String a){this.airconditioning=a;}
    public String getTerrace(){return terrace;} public void setTerrace(String t){this.terrace=t;}
    public String getPrefarea(){return prefarea;} public void setPrefarea(String p){this.prefarea=p;}
    public String getFurnishingstatus(){return furnishingstatus;} public void setFurnishingstatus(String f){this.furnishingstatus=f;}
    public String getDescription(){return description;} public void setDescription(String d){this.description=d;}
    public String getStaticReview(){return staticReview;} public void setStaticReview(String sr){this.staticReview=sr;}
    public List<Review> getReviews(){return reviews;} public void setReviews(List<Review> r){this.reviews=r;}
    public List<School> getSchools(){return schools;} public void setSchools(List<School> s){this.schools=s;}
    public void addSchool(School s){this.schools.add(s);}
    public List<PropertyContract> getPropertyContracts(){return propertyContracts;} public void setPropertyContracts(List<PropertyContract> pc){this.propertyContracts=pc;}
}
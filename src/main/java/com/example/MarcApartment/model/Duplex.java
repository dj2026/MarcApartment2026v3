package com.example.MarcApartment.model;
import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("DUPLEX")
public class Duplex extends Apartment {
    private String balcony; private String elevator;
    public Duplex() {super();}

    public Duplex(Long id, String propertyType, Long price, int area, int bedrooms, int bathrooms, int stories, String mainroad, String guestroom, String basement, String hotwater, String aircon, String terrace, int parking, String prefarea, String furnishingstatus, String description,String balcony, String elevator, String staticReview) 
        {super(id, propertyType, price, area, bedrooms, bathrooms, stories, mainroad, guestroom,basement, hotwater, aircon, terrace, parking, prefarea, furnishingstatus, description, staticReview); this.balcony = balcony; this.elevator = elevator; if (this.getPrice() == null || this.getPrice() == 0L) {this.calcularPreuAutomatic();}}

    @Override
    public void calcularPreuAutomatic() {
        super.calcularPreuAutomatic();
        long p = (this.getPrice() == null) ? 0L : this.getPrice();
        if ("yes".equalsIgnoreCase(elevator)) {p += 15000L;}
        if (balcony != null && balcony.toLowerCase().contains("large")) {p += 12000L;}
        this.setPrice(p);
    }

    public String getBalcony() {return balcony;} public void setBalcony(String balcony) {this.balcony = balcony;}
    public String getElevator() {return elevator;} public void setElevator(String elevator) {this.elevator = elevator;}
}
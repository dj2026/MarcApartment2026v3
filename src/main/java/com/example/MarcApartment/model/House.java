package com.example.MarcApartment.model;
import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("HOUSE")
public class House extends Apartment {
    private int yardSize; private String pool;
    public House() {super();}
    public House(Long id, String propertyType, Long price, int area, int bedrooms, int bathrooms, int stories, String mainroad, String guestroom, String basement, String hotwater, String aircon,String terrace, int parking, String prefarea, String furnishingstatus, String description,int yardSize, String pool, String staticReview) {
        super(id, propertyType, price, area, bedrooms, bathrooms, stories, mainroad, guestroom, basement, hotwater, aircon, terrace, parking, prefarea, furnishingstatus, description, staticReview); this.yardSize = yardSize;  this.pool = pool;
        if (this.getPrice() == null || this.getPrice() == 0L) {this.calcularPreuAutomatic();}
    }

    @Override
    public void calcularPreuAutomatic() {
        super.calcularPreuAutomatic();
        long p = (this.getPrice() == null) ? 0L : this.getPrice();
        if ("yes".equalsIgnoreCase(pool)) { p += 30000L;} p += (yardSize * 100L);
        this.setPrice(p);
    }

    public int getYardSize() {return yardSize;} public void setYardSize(int yardSize) {this.yardSize = yardSize;}
    public String getPool() {return pool;} public void setPool(String pool) {this.pool = pool;}
}
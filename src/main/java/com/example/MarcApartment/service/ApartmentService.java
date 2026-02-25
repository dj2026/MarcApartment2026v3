package com.example.MarcApartment.service;

import com.example.MarcApartment.model.*;
import com.example.MarcApartment.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Service
public class ApartmentService {

    @Autowired private ApartmentRepository aptRepo;
    @Autowired private ReviewRepository reviewRepo;
    @Autowired private ReviewerRepository reviewerRepo;
    @Autowired private OwnerRepository ownerRepo;
    @Autowired private SchoolRepository schoolRepo;
    @Autowired private PropertyContractRepository contractRepo;
    @Autowired private EntityManager em;

    public List<Apartment> findAll() { return aptRepo.findAll(); }

    @Transactional 
    public Apartment createApartment(Apartment apt) {
        if (apt.getPrice() == null || apt.getPrice() == 0) { apt.calcularPreuAutomatic(); } 
        return aptRepo.save(apt);
    }

    @Transactional
    public Apartment updateApartment(Long id, Apartment dadesNoves) {
        return aptRepo.findById(id).map(apt -> {
            apt.setPropertyType(dadesNoves.getPropertyType()); 
            apt.setDescription(dadesNoves.getDescription());
            apt.setArea(dadesNoves.getArea()); 
            apt.setBedrooms(dadesNoves.getBedrooms());
            apt.setBathrooms(dadesNoves.getBathrooms()); 
            apt.setStories(dadesNoves.getStories());
            apt.setParking(dadesNoves.getParking()); 
            apt.setMainroad(dadesNoves.getMainroad());
            apt.setGuestroom(dadesNoves.getGuestroom());
            apt.setBasement(dadesNoves.getBasement());
            apt.setHotwaterheating(dadesNoves.getHotwaterheating());
            apt.setAirconditioning(dadesNoves.getAirconditioning());
            apt.setTerrace(dadesNoves.getTerrace()); 
            apt.setPrefarea(dadesNoves.getPrefarea());
            apt.setFurnishingstatus(dadesNoves.getFurnishingstatus()); 
            apt.setStaticReview(dadesNoves.getStaticReview());

            if (apt instanceof House && dadesNoves instanceof House) {((House) apt).setYardSize(((House) dadesNoves).getYardSize()); ((House) apt).setPool(((House) dadesNoves).getPool());} 
            else if (apt instanceof Duplex && dadesNoves instanceof Duplex) {((Duplex) apt).setBalcony(((Duplex) dadesNoves).getBalcony()); ((Duplex) apt).setElevator(((Duplex) dadesNoves).getElevator());}

            if (dadesNoves.getPrice() != null && dadesNoves.getPrice() > 0) {apt.setPrice(dadesNoves.getPrice()); } else { apt.calcularPreuAutomatic();}

            return aptRepo.saveAndFlush(apt);
        }).orElseThrow(() -> new RuntimeException("No he trobat l'ID: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        Apartment a = aptRepo.findById(id).orElseThrow(() -> new RuntimeException("L'ID " + id + " no existeix."));
        em.createNativeQuery("DELETE FROM reviews WHERE apartment_id = ?1").setParameter(1, id).executeUpdate();
        contractRepo.deleteByApartmentId(id);
        a.getSchools().clear();
        aptRepo.save(a);
        aptRepo.delete(a);
    }

    @Transactional
    public void executeFullReset() {
        em.createNativeQuery("DELETE FROM reviews").executeUpdate();
        em.createNativeQuery("DELETE FROM property_contracts").executeUpdate();
        em.createNativeQuery("DELETE FROM apartment_school").executeUpdate();
        em.createNativeQuery("DELETE FROM apartments").executeUpdate();
        em.createNativeQuery("DELETE FROM schools").executeUpdate();
        em.createNativeQuery("DELETE FROM persons").executeUpdate();

        try {
            em.createNativeQuery("ALTER TABLE apartments ALTER COLUMN id RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER TABLE schools ALTER COLUMN id RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER TABLE persons ALTER COLUMN id RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER TABLE reviews ALTER COLUMN id RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER TABLE property_contracts ALTER COLUMN id RESTART WITH 1").executeUpdate();
        } catch (Exception e) {System.out.println("Avís reset IDs: " + e.getMessage());}

        em.flush();
        em.clear();

        School s1 = schoolRepo.save(new School(null, "Escola Gravi", "Pública", "C/Jerico, 5", "150m", "Infantil i Primària", true));
        School s2 = schoolRepo.save(new School(null, "Escola Palcam", "Privada", "C/Castillejos, 361", "450m", "ESO i Batxillerat", false));
        School s3 = schoolRepo.save(new School(null, "Escola Paideia", "Privada", "C/Montnegre, 36", "900m", "Educació Especial", false));
        
        Owner marc = ownerRepo.save(new Owner("Marc", "marc@apartments.com", 30, true, false, "12345678X", LocalDate.now(), 100));
        Reviewer tomas = reviewerRepo.save(new Reviewer("Tomas", "tomas@pro.com", "Expert en luxe", 5));

        Apartment apt1 = new Apartment(null, "Apartment", 0L, 1100, 2, 2, 1, "yes", "no", "no", "no", "no", "yes", 2, "yes", "furnished", "Aquest apartament presenta un saló de luxe d'estètica contemporània i minimalista, que comparteix la vista panoràmica de la ciutat al capvespre però amb un disseny més lluminós i suau que l'anterior", "Increïble apartament al centre de la ciutat ⭐⭐⭐⭐⭐");
        apt1.addSchool(s1); apt1.addSchool(s2); apt1.addSchool(s3);
        guardarTot(apt1, marc, tomas, "Apartment de luxe");

        Duplex dup1 = new Duplex(null, "Duplex", 0L, 2000, 4, 2, 2, "yes", "yes", "no", "no", "yes", "no", 2, "no", "semi-furnished", "Duplex amb saló de luxe d'estil industrial-modern situat en un àtic amb unes vistes espectaculars. L'espai destaca per la seva gran amplitud, sostres de doble alçada i una connexió total amb l'exterior.", "large", "yes", "Dúplex amb vistes al mar i molta llum.⭐⭐⭐⭐⭐");
        dup1.addSchool(s2);
        guardarTot(dup1, marc, tomas, "Duplex amb vistes");

        House casa1 = new House(null, "House", 0L, 1000, 2, 2, 1, "yes", "no", "yes", "no", "yes", "yes", 1, "no", "unfurnished", "Casa que connecta l'interior amb un jardí i piscina, destacant pel seu sostre de fusta amb línies LED i parets de pedra natural.", 500, "yes", "Casa rústica ideal per a famílies ⭐⭐⭐⭐");
        casa1.addSchool(s3);
        guardarTot(casa1, marc, tomas, "Casa Rustica");
        
        System.out.println("🔄 DB Reset completada correctament.");
    }

    private void guardarTot(Apartment apt, Owner o, Reviewer r, String titol) {
        apt.calcularPreuAutomatic();
        Owner ownerManaged = em.merge(o);
        Reviewer reviewerManaged = em.merge(r);
        Apartment guardat = aptRepo.saveAndFlush(apt); 
        PropertyContract contracte = new PropertyContract(ownerManaged, guardat, "Contracte " + guardat.getPropertyType(), LocalDate.now(), (double)guardat.getPrice());
        contractRepo.saveAndFlush(contracte); 
        Review rev = new Review(titol, guardat.getStaticReview(), 5, LocalDate.now());
        rev.setApartment(guardat);
        rev.setReviewer(reviewerManaged);
        reviewRepo.saveAndFlush(rev);
    }
}
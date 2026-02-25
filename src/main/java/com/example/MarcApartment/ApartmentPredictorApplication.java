package com.example.MarcApartment;
import com.example.MarcApartment.model.*;
import com.example.MarcApartment.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class ApartmentPredictorApplication implements CommandLineRunner {

    @Autowired private ApartmentRepository aptRepo; 
    @Autowired private OwnerRepository ownerRepo;
    @Autowired private SchoolRepository schoolRepo;
    @Autowired private ReviewerRepository reviewerRepo;
    @Autowired private ReviewRepository reviewRepo; 
    @Autowired private PropertyContractRepository contractRepo;

    public static void main(String[] args) {SpringApplication.run(ApartmentPredictorApplication.class, args);}

    @Override
    public void run(String... args) throws Exception {carregarDadesInicials();}

    @Transactional
    public void carregarDadesInicials() {
        reviewRepo.deleteAll(); 
        contractRepo.deleteAll(); 
        aptRepo.deleteAll(); 
        schoolRepo.deleteAll();
        ownerRepo.deleteAll();
        reviewerRepo.deleteAll();

        School s1 = schoolRepo.save(new School(null, "Escola Gravi", "Pública", "C/Jerico, 5", "750m", "Infantil / Primària / ESO", true));
        School s2 = schoolRepo.save(new School(null, "Escola Palcam", "Privada", "C/Castillejos, 361", "450m", "ESO i Batxillerat", false));
        School s3 = schoolRepo.save(new School(null, "Escola Paideia", "Privada", "C/Montnegre, 36", "950m", "Educació Especial", false));
        
        Reviewer tomas = reviewerRepo.save(new Reviewer("Tomas", "tomas@critics.com", "Expert en luxe", 5));

        Owner marc = ownerRepo.save(new Owner("Marc", "marc@apartments.com", 29, true, false, "12345678X", LocalDate.now(), 100));
        Owner sonia = ownerRepo.save(new Owner("Sònia Agent", "sonia@luxury.com", 34, true, true, "99887766Z", LocalDate.now(), 95));
        Owner albert = ownerRepo.save(new Owner("Albert Propietari", "albert@propietats.com", 45, true, false, "55443322W", LocalDate.now(), 80));
        Owner laura = ownerRepo.save(new Owner("Laura Immobles", "laura@agencia.com", 31, true, true, "11223344K", LocalDate.now(), 92));

        List<Owner> llistaOwners = List.of(marc, sonia, albert, laura);

        for (int i = 1; i <= 5; i++) {

            Apartment apt = new Apartment(null, "Apartment" + i, 0L, 1000 + (i * 10), 2, 1, 1, "yes", "no", "no", "no", "no", "no", 1, "no", "furnished", "Aquest apartament presenta un saló de luxe d'estètica contemporània i minimalista, que comparteix la vista panoràmica de la ciutat al capvespre però amb un disseny més lluminós i suau que l'anterior.", "Increïble apartament al centre de la ciutat ⭐⭐⭐⭐⭐");
            prepararISalvar(apt, s1, llistaOwners.get((i-1) % 4), tomas, "Review Apt " + i);
            
            Duplex dup = new Duplex(null, "Duplex" + i, 0L, 1800 + (i * 20), 3, 2, 2, "yes", "yes", "no", "no", "yes", "yes", 2, "no", "semi-furnished", "Duplex amb saló de luxe d'estil industrial-modern situat en un àtic amb unes vistes espectaculars. L'espai destaca per la seva gran amplitud, sostres de doble alçada i una connexió total amb l'exterior. " + i, "Terrassa gran", "yes", "Dúplex amb vistes al mar i molta llum.⭐⭐⭐⭐⭐");
            prepararISalvar(dup, s2, llistaOwners.get(i % 4), tomas, "Review Dup " + i);
            
            House hou = new House(null, "House" + i, 0L, 1500 + (i * 30), 4, 3, 2, "yes", "no", "yes", "no", "yes", "no", 1, "no", "unfurnished", "Casa que connecta l'interior amb un jardí i piscina, destacant pel seu sostre de fusta amb línies LED i parets de pedra natural. " + i, 150 + (i * 10), "yes", "Casa rústica ideal per a famílies ⭐⭐⭐⭐");
            prepararISalvar(hou, s3, llistaOwners.get((i+1) % 4), tomas, "Review House " + i);
        }
        imprimirResumFinal();
    }
        private void prepararISalvar(Apartment apt, School school, Owner owner, Reviewer rev, String titolReview) {
        apt.addSchool(school);
        apt.calcularPreuAutomatic();
        aptRepo.save(apt); 

        PropertyContract contracte = new PropertyContract(owner, apt, "Contracte " + apt.getPropertyType(), LocalDate.now(), (double)apt.getPrice());
        contractRepo.save(contracte);

        Review r = new Review(titolReview, apt.getStaticReview(), 5, LocalDate.now());
        r.setApartment(apt); 
        r.setReviewer(rev); 
        reviewRepo.save(r);
    }

    private void imprimirResumFinal() {
        System.out.println("\n============================================================== ");
        System.out.println("                            PRA02                                ");
        System.out.println("==============================================================   ");
        System.out.println(" - 1. Modelatge / Herència                                       ");
        System.out.println(" - 2. Implementació JPA + Repositories                           ");
        System.out.println(" - 3. Population de Dades                                        ");
        System.out.println(" - 4. REST Controllers i CRUD                                    ");
        System.out.println("==============================================================   ");
        System.out.println("                           COMPLET                               ");
        System.out.println("==============================================================\n ");
    }
}
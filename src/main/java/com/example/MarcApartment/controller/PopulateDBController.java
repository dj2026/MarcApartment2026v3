package com.example.MarcApartment.controller;

import com.example.MarcApartment.model.Apartment;
import com.example.MarcApartment.model.Owner;
import com.example.MarcApartment.service.ApartmentService;
import com.example.MarcApartment.repository.OwnerRepository;
import com.example.MarcApartment.utils.PopulateDB;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/populate")
public class PopulateDBController {

    @Autowired private PopulateDB populateDB;
    @Autowired private ApartmentService apartmentService;
    @Autowired private OwnerRepository ownerRepo;

    // 1. GET all owners
    @GetMapping("/owners")
    public List<Owner> getAllOwners() {
        return ownerRepo.findAll();
    }

    // 2. GET one owner (per ID)
    @GetMapping("/owners/{id}")
    public Owner getOneOwner(@PathVariable Long id) {
        return ownerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Propietari no trobat"));
    }

    // 3. GET apartments format list
    @GetMapping("/json")
    public List<Apartment> getAllApartmentsJson() {
        return apartmentService.findAll();
    }

    // 4. POST new apartment
    @PostMapping("/create")
    public Apartment createApartment(@RequestBody Apartment apt) {
        return apartmentService.createApartment(apt);
    }

    // 5. PUT update
    @PutMapping("/update/{id}")
    public Apartment updateApartment(@PathVariable Long id, @RequestBody Apartment apt) {
        return apartmentService.updateApartment(id, apt);
    }

    // 6. DELETE
    @DeleteMapping("/delete/{id}")
    public String deleteApartment(@PathVariable Long id) {
        apartmentService.deleteById(id);
        return "✅ L'immoble amb ID " + id + " ha estat eliminat correctament.";
    }

    // Vista principal: http://localhost:8080/api/populate/run

    @GetMapping("/run")
    public String runPopulate() {
        try {List<Apartment> apts = apartmentService.findAll(); String html = apts.stream().map(this::renderCard).collect(Collectors.joining());
            return String.format("""
                <!DOCTYPE html><html><head><link rel='icon' type='image/webp' href='/images/logo.webp'><meta charset='UTF-8'><title>Apartments | PINT APART®</title>
                <link href='https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;600;800&display=swap' rel='stylesheet'>
                <link rel='stylesheet' href='/css/populateapartments.css'></head>
                <body><div class='logo-fixed'><img src='/images/logo.webp'/></div>
                <div class='main-container'><div class='glass-card'>
                <div class='header'><h1>ESTAT DEL SISTEMA</h1><div class='unit-badge'>TOTAL : %d</div></div>
                <div class='btn-group'>
                    <a href='/api/populate/create_one' class='btn'>➕ Afegir</a>
                    <a href='/api/populate/reset' class='btn btn-danger'>🔄 Revertir</a>
                    <a href='/api/contracte/run' class='btn'>📑 Contractes</a>
                </div>
                <div class='list-container'>%s</div></div>
                <p class='footer-brand'>🏠 <span>PINT APART</span> 2026 🏠</p></div></body></html>""", 
                apts.size(), html.isEmpty() ? "<div class='empty-msg'>Base de dades buida...</div>" : html);
        } catch (Exception e) { return "<h2>Error: " + e.getMessage() + "</h2>"; }
    }

    private String renderCard(Apartment a) {
        // Lògica per marcar els 3 originals (ID 1, 2 i 3)
        boolean isOriginal = a.getId() != null && a.getId() <= 3;
        
        // Icona personalitzada per al teu Dúplex i la teva Casa
        String type = (a.getPropertyType() != null) ? a.getPropertyType().toLowerCase() : "";
        String icona = type.contains("duplex") ? "🏘️" : (type.contains("house") ? "🏡" : "🏠");
        
        String subtitol = isOriginal ? a.getPropertyType() + " emmagatzemat" : "Nou registre detectat";
        String statusLabel = isOriginal ? "ACTIU" : "PROVA";

        return String.format("""
            <div class='item-card %s'>
                <div class='item-main-content'>
                    <div class='id-badge'>ID %d</div>
                    <div class='item-text-wrapper'>
                        <div class='item-title'>%s %s</div>
                        <div class='item-subtitle'>%s</div>
                    </div>
                </div>
                <div class='status-container'>
                    <span class='status-text'>%s</span>
                    <div class='status-dot'></div>
                </div>
            </div>""", 
            isOriginal ? "is-active" : "is-inactive", a.getId(), icona, a.getPropertyType(), subtitol, statusLabel);
    }

    @GetMapping("/create_one") public void createOne(HttpServletResponse response) throws IOException {populateDB.populateAll(1); response.sendRedirect("/api/populate/run");}
    @GetMapping("/reset") public void resetAll(HttpServletResponse response) throws IOException { apartmentService.executeFullReset(); response.sendRedirect("/api/populate/run");}
}
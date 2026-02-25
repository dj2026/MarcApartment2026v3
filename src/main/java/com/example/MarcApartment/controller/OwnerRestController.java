package com.example.MarcApartment.controller;

import com.example.MarcApartment.model.Owner;
import com.example.MarcApartment.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/owner")
@CrossOrigin(origins = "*")
public class OwnerRestController {

    @Autowired private OwnerRepository ownerRepo;
    @Autowired private JdbcTemplate jdbcTemplate;

    // http://localhost:8080/api/owner/run

    @GetMapping("/run")
    public String llistarPropietaris() {
        try {
            List<Owner> owners = ownerRepo.findAll(); 
            String items = owners.stream().map(this::renderOwnerCard).collect(Collectors.joining());
            return buildOwnerPage(owners.size(), items);
        } catch (Exception e) {
            return "<h2>Error: " + e.getMessage() + "</h2>";
        }
    }

    private String renderOwnerCard(Owner o) {
        boolean active = o.getIsActive() != null && o.getIsActive();
        String subtitle = active ? "Propietari al sistema" : "No està en el sistema";
        return String.format("""
            <div class='item-card %s'>
                <div class='item-main-content'><div class='id-badge'>ID %d</div><div class='item-text-wrapper'><div class='item-title'>👤 %s</div><div class='item-subtitle'>%s</div></div></div>
                <div class='status-container'><span class='status-text'>%s</span><div class='status-dot'></div></div>
            </div>""", active ? "is-active" : "is-inactive", o.getId(), o.getName(), subtitle, active ? "ACTIU" : "INACTIU");
    }

    private String buildOwnerPage(int size, String items) {
        return String.format("""
            <!DOCTYPE html><html><head><meta charset='UTF-8'><link rel='icon' type='image/webp' href='/images/logo.webp'><title>OwnerRC | PINT APART®</title>
            <link href="https://fonts.googleapis.com/css2?family=Inter:wght@bolder&display=swap" rel="stylesheet"><link rel='stylesheet' href='/css/owners.css'></head>
            <body><div class='logo-fixed'><img src='/images/logo.webp'/></div><div class='main-container'><div class='glass-card'><div class='header'><h1>OWNERS RC</h1><div class='unit-badge'>TOTAL : %d</div></div><div class='btn-group'><a href='/api/owner/crear' class='btn'>➕ Afegir</a><a href='/api/owner/revertir' class='btn'>🔄 Revertir</a></div><div class='list-container'>%s</div></div><p class='footer-brand'>👤 <span>PINT APART</span> 2026 👤</p></div>
            </body></html>""", size, items.isEmpty() ? "<div class='empty-msg'>No hi ha propietaris al sistema...</div>" : items);
    }

    @GetMapping("/crear")
    public String crear() {
        Owner o = new Owner(); 
        o.setName("Propietari fake" + (ownerRepo.count() + 1));  
        o.setEmail("contacte@pintapart.com");
        o.setIsActive(false); 
        ownerRepo.save(o); 
        return llistarPropietaris();
    }

    @GetMapping("/revertir")
    public String revertir() {
        List<Owner> tots = ownerRepo.findAll(); 
        for (Owner o : tots) { if (o.getId() > 2) { ownerRepo.delete(o); } }
        try { jdbcTemplate.execute("ALTER TABLE PERSONS ALTER COLUMN ID RESTART WITH 3"); } 
        catch (Exception e) { System.out.println("Error restart ID: " + e.getMessage()); }
        return llistarPropietaris();
    }

    @GetMapping("/all") public ResponseEntity<List<Owner>> getAllJson() {return new ResponseEntity<>(ownerRepo.findAll(), HttpStatus.OK);}
    @GetMapping("/{id}") public ResponseEntity<Owner> getById(@PathVariable Long id) {return ownerRepo.findById(id).map(o -> new ResponseEntity<>(o, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));}
    @PostMapping("/crear") public ResponseEntity<?> crearOwner(@RequestBody Owner nouOwner) {try {Owner guardat = ownerRepo.save(nouOwner);return new ResponseEntity<>(guardat, HttpStatus.CREATED);} catch (Exception e) { return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en el format: " + e.getMessage());}}
    @PutMapping("/actualitzar/{id}") public ResponseEntity<Owner> modificarOwner(@PathVariable Long id, @RequestBody Owner dadesNoves) {
        return ownerRepo.findById(id).map(p -> {
            p.setName(dadesNoves.getName());  p.setEmail(dadesNoves.getEmail());  p.setIsActive(dadesNoves.getIsActive()); p.setAge(dadesNoves.getAge()); p.setIsBusiness(dadesNoves.getIsBusiness());
            p.setIdLegalOwner(dadesNoves.getIdLegalOwner()); p.setRegistrationDate(dadesNoves.getRegistrationDate()); p.setQtyDaysAsOwner(dadesNoves.getQtyDaysAsOwner());
            Owner actualitzat = ownerRepo.save(p);
            return new ResponseEntity<>(actualitzat, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/eliminar/{id}") public ResponseEntity<String> eliminar(@PathVariable Long id) {if (ownerRepo.existsById(id)) {ownerRepo.deleteById(id);return new ResponseEntity<>("Propietari amb ID " + id + " eliminat.", HttpStatus.OK);}return new ResponseEntity<>("No s'ha trobat el propietari.", HttpStatus.NOT_FOUND);}
}
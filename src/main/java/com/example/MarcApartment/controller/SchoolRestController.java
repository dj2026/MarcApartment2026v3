package com.example.MarcApartment.controller;

import com.example.MarcApartment.model.School;
import com.example.MarcApartment.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/escola")
@CrossOrigin(origins = "*")
public class SchoolRestController {

    @Autowired private SchoolRepository escolaRepo;

    // http://localhost:8080/api/escola/run
    
    @GetMapping("/run")
    public String renderSchools() {
        try {
            List<School> escoles = escolaRepo.findAll();
            String html = escoles.stream().map(this::renderSchoolCard).collect(Collectors.joining());
            return String.format("""
                <!DOCTYPE html><html><head><meta charset='UTF-8'><title>Escoles | PINT APART®</title><link rel='icon' type='image/webp' href='/images/logo.webp'>
                <link href='https://fonts.googleapis.com/css2?family=Inter:wght@bolder&display=swap' rel='stylesheet'><link rel='stylesheet' href='/css/school.css'></head>
                <body>
                    <div class='logo-fixed'><img src='/images/logo.webp'/></div>
                    <div class='main-container'><div class='glass-card'><div class='header'><h1>ESCOLES ZONA</h1><div class='unit-badge'>CENTRES : %d</div></div><div class='list-container'>%s</div></div>
                    <p class='footer-brand'>🎓 <span>PINT APART</span> 2026 🎓</p></div>
                </body></html>""", escoles.size(), html.isEmpty() ? "<div class='empty'>No hi ha escoles registrades</div>" : html);
        } catch (Exception e) { return "Error: " + e.getMessage(); }
    }

    private String renderSchoolCard(School s) {
        String tipus = s.isPublic() ? "Pública" : "Privada/Concertada";
        String icona = s.isPublic() ? "🏫" : "🏛️";
        return String.format("""
            <div class='item-card'>
                <div class='item-main-content'>
                    <div class='id-badge'>ID %d</div><div class='item-text-wrapper'><div class='item-title'>%s %s</div><div class='details-box'><div>📍 <b>Adreça:</b> %s</div><div>📊 <b>Nivell:</b> %s</div><div>🚶 <b>Distància:</b> %s</div></div>
                </div></div>
                <div class='status-container'><span class='status-text'>%s</span></div>
            </div>""", s.getId(), icona, s.getName(), s.getAddress(), s.getEducationLevel(), s.getDistance(), tipus);
    }

    @GetMapping("/llistar") public List<School> llistarEscoles() { return escolaRepo.findAll(); }
    @GetMapping("/detalls/{id}") public ResponseEntity<School> buscarEscola(@PathVariable Long id) { return escolaRepo.findById(id).map(escola -> new ResponseEntity<>(escola, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); }
    @PostMapping("/afegir") public ResponseEntity<School> novaEscola(@RequestBody School s) { try { School guardada = escolaRepo.save(s); return new ResponseEntity<>(guardada, HttpStatus.CREATED); } catch (Exception e) { return new ResponseEntity<>(HttpStatus.BAD_REQUEST); } }
    @PutMapping("/modificar/{id}") public ResponseEntity<School> modificarEscola(@PathVariable Long id, @RequestBody School dades) { return escolaRepo.findById(id).map(actual -> { actual.setName(dades.getName()); actual.setSchoolType(dades.getSchoolType()); actual.setAddress(dades.getAddress()); actual.setDistance(dades.getDistance()); actual.setEducationLevel(dades.getEducationLevel()); actual.setPublic(dades.isPublic()); return new ResponseEntity<>(escolaRepo.save(actual), HttpStatus.OK); }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); }
    @DeleteMapping("/esborrar/{id}") public ResponseEntity<Void> eliminar(@PathVariable Long id) { try { if (escolaRepo.existsById(id)) { escolaRepo.deleteById(id); return new ResponseEntity<>(HttpStatus.NO_CONTENT); } return new ResponseEntity<>(HttpStatus.NOT_FOUND); } catch (Exception e) { return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); } }
}
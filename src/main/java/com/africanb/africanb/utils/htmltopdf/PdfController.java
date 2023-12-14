package com.africanb.africanb.utils.htmltopdf;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @GetMapping("/generate-ticket")
    public String generateTicket() {
        try {
            Map<String, String> staticData = new HashMap<>();
            staticData.put("Compagnie transport", "MaCompagnie");
            staticData.put("Gare compagnie", "Gare Centrale");
            staticData.put("Nom", "Dupont");
            staticData.put("Prenoms", "Jean");
            staticData.put("Ville depart", "Ville A");
            staticData.put("Ville destination", "Ville B");
            staticData.put("Heure depart", "10:00");
            staticData.put("Heure destination", "14:00");
            staticData.put("Date generation", "01-01-2023 12:30:00");
            staticData.put("Prix", "50.00");
            staticData.put("Nombre places", "2");
            pdfService.generateTicket(staticData);
            return "Le billet a été généré avec succès.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Une erreur s'est produite lors de la génération du billet.";
        }
    }
}

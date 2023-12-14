package com.africanb.africanb.utils.htmltopdf;

import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Component
public class PdfService {

    public void generateTicket(Map<String, String> dynamicFields) throws Exception {
        // Date de génération
        String dateGeneration = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());

        // HTML pour le billet avec des champs dynamiques et référence au fichier CSS externe
        String htmlContent =    "<html><head>" +
                "<style>" +
                "@page { size: 13cm 15cm; margin: 0; } " +
                "body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background-image: url('classpath:chemin/vers/votre/quadrillage.png'); background-repeat: repeat; width: 13cm; height: 15cm; transform: rotate(-90deg); transform-origin: left top; } " +
                ".ticket { position: relative; width: 100%; height: 100%; box-sizing: border-box; } " +
                ".border { position: absolute; top: 0; left: 0; width: 100%; height: 100%; border: 2px solid #333; box-sizing: border-box; } " +
                ".content { position: absolute; top: 15mm; left: 10mm; right: 10mm; bottom: 10mm; display: flex; flex-direction: column; } " +
                ".row { display: flex; flex-direction: row; justify-content: space-between; margin-bottom: 8px; } " +
                ".column { flex: 1; margin-right: 10px; } " +
                ".watermark { color: #ccc; font-size: 2em; opacity: 0.5; position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%) rotate(-45deg); } " +
                "p { margin: 10px; line-height: 1.5; color: #555; font-size: 14px; } " +
                ".field { font-weight: bold; font-size: 16px; } " + // Ajout de la classe field pour styliser les champs
                "</style>" +
                "</head><body>" +
                "<div class=\"ticket\">" +
                "<center><i><h4><u>BILLET TRANSPORT DELIVRE PAR AFRICANBUS</u></h4></i></center>" + // Titre avec le style italic
                "<div class=\"border\"></div>" +
                "<div class=\"content\">" +
                "<p><strong>Compagnie de transport:</strong> " + dynamicFields.get("Compagnie transport") + "</p>" +
                "<p><strong>Gare de compagnie:</strong> " + dynamicFields.get("Gare compagnie") + "</p>" +
                "<p><strong>Nom:</strong> " + dynamicFields.get("Nom") + "</p>" +
                "<p><strong>Prénoms:</strong> " + dynamicFields.get("Prenoms") + "</p>" +
                "<p><strong>Lieu de départ:</strong> " + dynamicFields.get("Ville depart") + "</p>" +
                "<p><strong>Lieu de destination:</strong> " + dynamicFields.get("Ville destination") + "</p>" +
                "<p><strong>Heure de départ:</strong> " + dynamicFields.get("Heure depart") + "</p>" +
                "<p><strong>Heure de destination:</strong> " + dynamicFields.get("Heure destination") + "</p>" +
                "<p><strong>Date de génération:</strong> " + dateGeneration + "</p>" +
                "<p><strong>Prix:</strong> " + dynamicFields.get("Prix") + "</p>" +
                "<p><strong>Nombre de places:</strong> " + dynamicFields.get("Nombre places") + "</p>" +
                "</div></div></body></html>";

        // Chemin du fichier PDF généré
        String pdfPath = "billet.pdf";
        generatePdf(htmlContent, pdfPath);
    }

    private void generatePdf(String htmlContent, String pdfPath) throws Exception {
        try (OutputStream os = new FileOutputStream(pdfPath)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(os, false);
            renderer.finishPDF();
        }
    }

}

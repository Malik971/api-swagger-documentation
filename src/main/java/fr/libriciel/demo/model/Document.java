package fr.libriciel.demo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Représente un document dans le système de dématérialisation.
 * Ce modèle simule les flux documentaires typiques de Libriciel/Pastell.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    
    private String id;
    
    @NotBlank(message = "Le titre du document est obligatoire")
    private String titre;
    
    @NotBlank(message = "Le type de document est obligatoire")
    private String type; // Ex: "ACTE", "DELIBERATION", "MARCHE_PUBLIC", etc.
    
    @NotNull(message = "Le statut est obligatoire")
    private DocumentStatus statut;
    
    private String contenu; // Contenu base64 ou référence
    
    private String editeurPartenaire; // Identifiant de l'éditeur partenaire
    
    private String destinataire; // Collectivité ou service destinataire
    
    private LocalDateTime dateCreation;
    
    private LocalDateTime dateModification;
    
    private Metadata metadata;
    
    /**
     * Métadonnées additionnelles du document
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metadata {
        private String referenceInterne;
        private String classification;
        private Boolean confidentiel;
        private String[] tags;
    }
}

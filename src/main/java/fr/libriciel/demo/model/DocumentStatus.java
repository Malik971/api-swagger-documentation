package fr.libriciel.demo.model;

/**
 * Représente les différents statuts d'un document dans le workflow.
 * Ces statuts simulent le cycle de vie d'un document dans Pastell.
 */
public enum DocumentStatus {
    BROUILLON("Document en cours de création"),
    EN_ATTENTE_SIGNATURE("Document en attente de signature électronique"),
    SIGNE("Document signé"),
    EN_COURS_TRANSMISSION("Document en cours de transmission"),
    TRANSMIS("Document transmis avec succès"),
    REJETE("Document rejeté"),
    ARCHIVE("Document archivé");
    
    private final String description;
    
    DocumentStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

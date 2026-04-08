package fr.libriciel.demo.controller;

import fr.libriciel.demo.model.Document;
import fr.libriciel.demo.model.DocumentStatus;
import fr.libriciel.demo.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller REST pour la gestion des documents.
 * Simule l'API Pastell de Libriciel pour les opérations documentaires.
 */
@RestController
@RequestMapping("/api/v1/documents")
@Tag(name = "Documents", description = "Gestion des documents dans le flux de dématérialisation")
public class DocumentController {
    
    @Autowired
    private DocumentService documentService;
    
    /**
     * Créer un nouveau document
     */
    @PostMapping
    @Operation(
        summary = "Créer un nouveau document",
        description = "Crée un nouveau document dans le système. Le document est créé avec le statut BROUILLON par défaut."
    )
    public ResponseEntity<Document> createDocument(
            @Valid @RequestBody Document document,
            Authentication authentication) {
        
        // Récupérer le client_id depuis le token
        String clientId = authentication.getName();
        document.setEmetteur(clientId);
        
        Document created = documentService.createDocument(document);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * Récupérer un document par son ID
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Récupérer un document",
        description = "Récupère un document spécifique par son identifiant"
    )
    public ResponseEntity<Document> getDocument(
            @Parameter(description = "ID du document")
            @PathVariable String id) {
        
        return documentService.getDocument(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Lister tous les documents
     */
    @GetMapping
    @Operation(
        summary = "Lister tous les documents",
        description = "Récupère la liste de tous les documents. Peut être filtré par statut."
    )
    public ResponseEntity<List<Document>> getAllDocuments(
            @Parameter(description = "Filtre par statut (optionnel)")
            @RequestParam(required = false) DocumentStatus statut) {
        
        List<Document> documents;
        
        if (statut != null) {
            documents = documentService.getDocumentsByStatus(statut);
        } else {
            documents = documentService.getAllDocuments();
        }
        
        return ResponseEntity.ok(documents);
    }
    
    /**
     * Lister les documents de l'émetteur connecté
     */
    @GetMapping("/mes-documents")
    @Operation(
        summary = "Lister mes documents",
        description = "Récupère tous les documents créés par le partenaire authentifié"
    )
    public ResponseEntity<List<Document>> getMyDocuments(Authentication authentication) {
        String clientId = authentication.getName();
        List<Document> documents = documentService.getDocumentsByEmetteur(clientId);
        return ResponseEntity.ok(documents);
    }
    
    /**
     * Mettre à jour un document
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Mettre à jour un document",
        description = "Met à jour les informations d'un document existant"
    )
    public ResponseEntity<Document> updateDocument(
            @PathVariable String id,
            @Valid @RequestBody Document document) {
        
        return documentService.updateDocument(id, document)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Changer le statut d'un document (workflow)
     */
    @PatchMapping("/{id}/statut")
    @Operation(
        summary = "Changer le statut d'un document",
        description = "Change le statut d'un document dans le workflow (ex: BROUILLON -> EN_ATTENTE_SIGNATURE)"
    )
    public ResponseEntity<Document> changeStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> payload) {
        
        String newStatusStr = payload.get("statut");
        if (newStatusStr == null) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            DocumentStatus newStatus = DocumentStatus.valueOf(newStatusStr);
            return documentService.changeStatus(id, newStatus)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Supprimer un document
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Supprimer un document",
        description = "Supprime définitivement un document du système"
    )
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        boolean deleted = documentService.deleteDocument(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    /**
     * Statistiques des documents
     */
    @GetMapping("/stats")
    @Operation(
        summary = "Statistiques des documents",
        description = "Retourne des statistiques sur les documents (nombre total, par statut, etc.)"
    )
    public ResponseEntity<Map<String, Object>> getStats() {
        long totalDocuments = documentService.getDocumentCount();
        
        Map<String, Long> byStatus = Map.of(
            "BROUILLON", (long) documentService.getDocumentsByStatus(DocumentStatus.BROUILLON).size(),
            "EN_ATTENTE_SIGNATURE", (long) documentService.getDocumentsByStatus(DocumentStatus.EN_ATTENTE_SIGNATURE).size(),
            "SIGNE", (long) documentService.getDocumentsByStatus(DocumentStatus.SIGNE).size(),
            "TRANSMIS", (long) documentService.getDocumentsByStatus(DocumentStatus.TRANSMIS).size()
        );
        
        Map<String, Object> stats = Map.of(
            "total", totalDocuments,
            "parStatut", byStatus
        );
        
        return ResponseEntity.ok(stats);
    }
}

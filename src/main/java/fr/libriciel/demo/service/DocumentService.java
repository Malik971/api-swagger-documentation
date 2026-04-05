package fr.libriciel.demo.service;

import fr.libriciel.demo.model.Document;
import fr.libriciel.demo.model.DocumentStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service de gestion des documents.
 * Simule les opérations CRUD sur les documents dans Pastell.
 */
@Service
public class DocumentService {
    
    // Stockage en mémoire pour la démo (en production : base de données)
    private final Map<String, Document> documents = new ConcurrentHashMap<>();
    
    /**
     * Crée un nouveau document
     */
    public Document createDocument(Document document) {
        String id = UUID.randomUUID().toString();
        document.setId(id);
        document.setDateCreation(LocalDateTime.now());
        document.setDateModification(LocalDateTime.now());
        
        // Si pas de statut défini, on met BROUILLON par défaut
        if (document.getStatut() == null) {
            document.setStatut(DocumentStatus.BROUILLON);
        }
        
        documents.put(id, document);
        return document;
    }
    
    /**
     * Récupère un document par son ID
     */
    public Optional<Document> getDocument(String id) {
        return Optional.ofNullable(documents.get(id));
    }
    
    /**
     * Liste tous les documents
     */
    public List<Document> getAllDocuments() {
        return new ArrayList<>(documents.values());
    }
    
    /**
     * Liste les documents par statut
     */
    public List<Document> getDocumentsByStatus(DocumentStatus status) {
        return documents.values().stream()
                .filter(doc -> doc.getStatut() == status)
                .toList();
    }
    
    /**
     * Liste les documents d'un émetteur spécifique
     */
    public List<Document> getDocumentsByEmetteur(String emetteur) {
        return documents.values().stream()
                .filter(doc -> emetteur.equals(doc.getEmetteur()))
                .toList();
    }
    
    /**
     * Met à jour un document existant
     */
    public Optional<Document> updateDocument(String id, Document updatedDocument) {
        Document existingDocument = documents.get(id);
        if (existingDocument == null) {
            return Optional.empty();
        }
        
        // Mise à jour des champs modifiables
        if (updatedDocument.getTitre() != null) {
            existingDocument.setTitre(updatedDocument.getTitre());
        }
        if (updatedDocument.getContenu() != null) {
            existingDocument.setContenu(updatedDocument.getContenu());
        }
        if (updatedDocument.getMetadata() != null) {
            existingDocument.setMetadata(updatedDocument.getMetadata());
        }
        
        existingDocument.setDateModification(LocalDateTime.now());
        
        return Optional.of(existingDocument);
    }
    
    /**
     * Change le statut d'un document (transition dans le workflow)
     */
    public Optional<Document> changeStatus(String id, DocumentStatus newStatus) {
        Document document = documents.get(id);
        if (document == null) {
            return Optional.empty();
        }
        
        // Validation des transitions possibles (simplifié pour la démo)
        if (isValidTransition(document.getStatut(), newStatus)) {
            document.setStatut(newStatus);
            document.setDateModification(LocalDateTime.now());
            return Optional.of(document);
        }
        
        return Optional.empty();
    }
    
    /**
     * Supprime un document
     */
    public boolean deleteDocument(String id) {
        return documents.remove(id) != null;
    }
    
    /**
     * Valide si une transition de statut est possible
     * (Logique simplifiée pour la démo)
     */
    private boolean isValidTransition(DocumentStatus currentStatus, DocumentStatus newStatus) {
        // Pour la démo, on autorise toutes les transitions
        // En production, on aurait une matrice de transitions autorisées
        return true;
    }
    
    /**
     * Retourne le nombre total de documents
     */
    public long getDocumentCount() {
        return documents.size();
    }
}

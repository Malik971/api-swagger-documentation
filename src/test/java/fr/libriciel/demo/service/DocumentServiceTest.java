package fr.libriciel.demo.service;

import fr.libriciel.demo.model.Document;
import fr.libriciel.demo.model.DocumentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour DocumentService
 */
class DocumentServiceTest {
    
    private DocumentService documentService;
    
    @BeforeEach
    void setUp() {
        documentService = new DocumentService();
    }
    
    @Test
    @DisplayName("Devrait créer un document avec succès")
    void testCreateDocument() {
        // Arrange
        Document document = new Document();
        document.setTitre("Test Document");
        document.setType("DELIBERATION");
        document.setStatut(DocumentStatus.BROUILLON);
        
        // Act
        Document created = documentService.createDocument(document);
        
        // Assert
        assertNotNull(created.getId());
        assertEquals("Test Document", created.getTitre());
        assertEquals(DocumentStatus.BROUILLON, created.getStatut());
        assertNotNull(created.getDateCreation());
    }
    
    @Test
    @DisplayName("Devrait créer un document avec statut BROUILLON par défaut")
    void testCreateDocumentWithDefaultStatus() {
        // Arrange
        Document document = new Document();
        document.setTitre("Test Document");
        document.setType("DELIBERATION");
        // Pas de statut défini
        
        // Act
        Document created = documentService.createDocument(document);
        
        // Assert
        assertEquals(DocumentStatus.BROUILLON, created.getStatut());
    }
    
    @Test
    @DisplayName("Devrait récupérer un document par ID")
    void testGetDocument() {
        // Arrange
        Document document = new Document();
        document.setTitre("Test Document");
        Document created = documentService.createDocument(document);
        
        // Act
        Optional<Document> retrieved = documentService.getDocument(created.getId());
        
        // Assert
        assertTrue(retrieved.isPresent());
        assertEquals(created.getId(), retrieved.get().getId());
    }
    
    @Test
    @DisplayName("Devrait retourner Optional.empty pour un ID inexistant")
    void testGetDocumentNotFound() {
        // Act
        Optional<Document> retrieved = documentService.getDocument("inexistant-id");
        
        // Assert
        assertTrue(retrieved.isEmpty());
    }
    
    @Test
    @DisplayName("Devrait lister tous les documents")
    void testGetAllDocuments() {
        // Arrange
        Document doc1 = new Document();
        doc1.setTitre("Document 1");
        documentService.createDocument(doc1);
        
        Document doc2 = new Document();
        doc2.setTitre("Document 2");
        documentService.createDocument(doc2);
        
        // Act
        List<Document> documents = documentService.getAllDocuments();
        
        // Assert
        assertEquals(2, documents.size());
    }
    
    @Test
    @DisplayName("Devrait filtrer les documents par statut")
    void testGetDocumentsByStatus() {
        // Arrange
        Document doc1 = new Document();
        doc1.setTitre("Document 1");
        doc1.setStatut(DocumentStatus.BROUILLON);
        documentService.createDocument(doc1);
        
        Document doc2 = new Document();
        doc2.setTitre("Document 2");
        doc2.setStatut(DocumentStatus.SIGNE);
        documentService.createDocument(doc2);
        
        // Act
        List<Document> brouillons = documentService.getDocumentsByStatus(DocumentStatus.BROUILLON);
        List<Document> signes = documentService.getDocumentsByStatus(DocumentStatus.SIGNE);
        
        // Assert
        assertEquals(1, brouillons.size());
        assertEquals(1, signes.size());
        assertEquals(DocumentStatus.BROUILLON, brouillons.get(0).getStatut());
    }
    
    @Test
    @DisplayName("Devrait filtrer les documents par émetteur")
    void testGetDocumentsByEmetteur() {
        // Arrange
        Document doc1 = new Document();
        doc1.setTitre("Document 1");
        doc1.setEmetteur("partner-1");
        documentService.createDocument(doc1);
        
        Document doc2 = new Document();
        doc2.setTitre("Document 2");
        doc2.setEmetteur("partner-2");
        documentService.createDocument(doc2);
        
        // Act
        List<Document> partner1Docs = documentService.getDocumentsByEmetteur("partner-1");
        
        // Assert
        assertEquals(1, partner1Docs.size());
        assertEquals("partner-1", partner1Docs.get(0).getEmetteur());
    }
    
    @Test
    @DisplayName("Devrait mettre à jour un document")
    void testUpdateDocument() {
        // Arrange
        Document document = new Document();
        document.setTitre("Titre Original");
        Document created = documentService.createDocument(document);
        
        Document updates = new Document();
        updates.setTitre("Titre Modifié");
        
        // Act
        Optional<Document> updated = documentService.updateDocument(created.getId(), updates);
        
        // Assert
        assertTrue(updated.isPresent());
        assertEquals("Titre Modifié", updated.get().getTitre());
        assertNotNull(updated.get().getDateModification());
    }
    
    @Test
    @DisplayName("Devrait changer le statut d'un document")
    void testChangeStatus() {
        // Arrange
        Document document = new Document();
        document.setTitre("Test Document");
        document.setStatut(DocumentStatus.BROUILLON);
        Document created = documentService.createDocument(document);
        
        // Act
        Optional<Document> updated = documentService.changeStatus(created.getId(), DocumentStatus.SIGNE);
        
        // Assert
        assertTrue(updated.isPresent());
        assertEquals(DocumentStatus.SIGNE, updated.get().getStatut());
    }
    
    @Test
    @DisplayName("Devrait supprimer un document")
    void testDeleteDocument() {
        // Arrange
        Document document = new Document();
        document.setTitre("Document à supprimer");
        Document created = documentService.createDocument(document);
        
        // Act
        boolean deleted = documentService.deleteDocument(created.getId());
        
        // Assert
        assertTrue(deleted);
        Optional<Document> retrieved = documentService.getDocument(created.getId());
        assertTrue(retrieved.isEmpty());
    }
    
    @Test
    @DisplayName("Devrait retourner false pour suppression d'un document inexistant")
    void testDeleteNonExistentDocument() {
        // Act
        boolean deleted = documentService.deleteDocument("inexistant-id");
        
        // Assert
        assertFalse(deleted);
    }
    
    @Test
    @DisplayName("Devrait compter correctement le nombre de documents")
    void testGetDocumentCount() {
        // Arrange
        Document doc1 = new Document();
        doc1.setTitre("Document 1");
        documentService.createDocument(doc1);
        
        Document doc2 = new Document();
        doc2.setTitre("Document 2");
        documentService.createDocument(doc2);
        
        // Act
        long count = documentService.getDocumentCount();
        
        // Assert
        assertEquals(2, count);
    }
}

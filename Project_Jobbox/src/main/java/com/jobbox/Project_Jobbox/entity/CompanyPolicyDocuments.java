package com.jobbox.Project_Jobbox.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class CompanyPolicyDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int documentId;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    private String documentTitle;

    @Lob
    @Column(name = "document_file", columnDefinition = "LONGBLOB")
    private byte[] documentFile;

    // Constructors
    public CompanyPolicyDocuments() {
        // Default constructor
    }

    public CompanyPolicyDocuments(Company company, String documentTitle, byte[] documentFile) {
        this.company = company;
        this.documentTitle = documentTitle;
        this.documentFile = documentFile;
    }

    // Getters and Setters
    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public byte[] getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(byte[] documentFile) {
        this.documentFile = documentFile;
    }
}

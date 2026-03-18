package com.InventoryManagmentUniandes.InventoryManagmentUniandes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Clase que representa un reporte DIAN (Dirección de Impuestos y Aduanas Nacionales)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DianReport {
    private String reportId;
    private String reportType;
    
    // Información del período del reporte
    private Date startDate;
    private Date endDate;
    
    // Información del reportante (quien envía el reporte)
    private String reporterDocumentType;
    private String reporterDocumentNumber;
    private String reporterVerificationDigit;
    private String reporterName;
    private String reporterAddress;
    private String reporterCity;
    private String reporterDepartment;
    private String reporterCountry;
    private String reporterPhone;
    private String reporterEmail;
    
    // Información tributaria
    private String taxpayerType;
    private String taxRegime;
    
    private long createdDate;

    /**
     * Constructor simplificado
     */
    public DianReport(String reportType, Date startDate, Date endDate) {
        this.reportType = reportType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdDate = System.currentTimeMillis();
    }

    /**
     * Valida que el reporte tenga información completa
     */
    public boolean isValid() {
        return reportType != null && !reportType.isEmpty() &&
               startDate != null && endDate != null &&
               reporterDocumentNumber != null && !reporterDocumentNumber.isEmpty() &&
               reporterName != null && !reporterName.isEmpty();
    }

    /**
     * Representación en string del reporte
     */
    @Override
    public String toString() {
        return "DianReport{" +
                "reportId='" + reportId + '\'' +
                ", reportType='" + reportType + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", reporterName='" + reporterName + '\'' +
                ", reporterDocumentNumber='" + reporterDocumentNumber + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}

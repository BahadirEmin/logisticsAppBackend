package com.baem.logisticapp.service;

import com.baem.logisticapp.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DocumentService {

    private static final String TEMPLATE_PATH = "templates/driver_information_form.docx";

    /**
     * Order bilgilerini kullanarak Word template'ini doldurur
     * 
     * @param order Sipariş bilgileri
     * @return Doldurulmuş Word dosyasının byte array'i
     */
    public byte[] generateDriverInformationDocument(Order order) {
        try {
            log.info("Starting document generation for order: {}", order.getId());

            // Template dosyasını yükle
            Resource templateResource = new ClassPathResource(TEMPLATE_PATH);
            log.info("Template path: {}", TEMPLATE_PATH);

            if (!templateResource.exists()) {
                log.error("Template dosyası bulunamadı: {}", TEMPLATE_PATH);
                throw new RuntimeException("Template dosyası bulunamadı: " + TEMPLATE_PATH);
            }

            log.info("Template dosyası bulundu, boyut: {} bytes", templateResource.contentLength());

            // Word document'i aç
            try (InputStream templateStream = templateResource.getInputStream();
                    XWPFDocument document = new XWPFDocument(templateStream)) {

                log.info("Word document açıldı, paragraf sayısı: {}", document.getParagraphs().size());

                // Placeholder'ları değiştir
                replacePlaceholders(document, order);
                log.info("Placeholder'lar değiştirildi");

                // Byte array'e çevir
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    document.write(outputStream);
                    byte[] result = outputStream.toByteArray();
                    log.info("Document başarıyla oluşturuldu, boyut: {} bytes", result.length);
                    return result;
                }
            }

        } catch (Exception e) {
            log.error("Word document oluşturulurken hata oluştu: {}", e.getMessage(), e);
            throw new RuntimeException("Word document oluşturulamadı: " + e.getMessage(), e);
        }
    }

    /**
     * Document'teki placeholder'ları gerçek değerlerle değiştirir
     */
    private void replacePlaceholders(XWPFDocument document, Order order) {
        // Tüm paragrafları işle
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            replacePlaceholdersInParagraph(paragraph, order);
        }

        // Tablolardaki paragrafları da işle
        document.getTables().forEach(table -> table.getRows().forEach(row -> row.getTableCells().forEach(
                cell -> cell.getParagraphs().forEach(paragraph -> replacePlaceholdersInParagraph(paragraph, order)))));
    }

    /**
     * Bir paragraftaki placeholder'ları değiştirir
     */
    private void replacePlaceholdersInParagraph(XWPFParagraph paragraph, Order order) {
        String paragraphText = paragraph.getText();

        if (paragraphText.contains("{{SURUCU}}")) {
            // Sürücü ismini al
            String driverName = getDriverName(order);

            // Placeholder'ı değiştir
            String newText = paragraphText.replace("{{SURUCU}}", driverName);

            // Mevcut runs'ları kaldır ve yeni metni ekle
            // getRuns() unmodifiable olduğu için önce listeye çevir
            int runCount = paragraph.getRuns().size();
            for (int i = runCount - 1; i >= 0; i--) {
                paragraph.removeRun(i);
            }

            // Yeni run oluştur ve metni ekle
            XWPFRun run = paragraph.createRun();
            run.setText(newText);
        }
    }

    /**
     * Order'dan sürücü ismini alır
     */
    private String getDriverName(Order order) {
        if (order.getAssignedDriver() != null) {
            return order.getAssignedDriver().getFullName();
        }
        return "Sürücü Atanmamış";
    }

    /**
     * Gelecekte kullanım için: Birden fazla placeholder'ı aynı anda değiştirmek
     */
    private void replaceMultiplePlaceholders(XWPFDocument document, Map<String, String> replacements) {
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            String paragraphText = paragraph.getText();
            String newText = paragraphText;

            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                newText = newText.replace(entry.getKey(), entry.getValue());
            }

            if (!newText.equals(paragraphText)) {
                // Mevcut runs'ları kaldır ve yeni metni ekle
                int runCount = paragraph.getRuns().size();
                for (int i = runCount - 1; i >= 0; i--) {
                    paragraph.removeRun(i);
                }

                // Yeni run oluştur ve metni ekle
                XWPFRun run = paragraph.createRun();
                run.setText(newText);
            }
        }
    }
}

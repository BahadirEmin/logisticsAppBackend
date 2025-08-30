package com.baem.logisticapp.entity;

public enum TripStatus {
    TEKLIF_ASAMASI("Teklif Aşaması"),
    ONAYLANAN_TEKLIF("Onaylanan Teklif"),
    IPTAL_EDILDI("İptal Edildi"),
    REDDEDILDI("Reddedildi"),
    YOLA_CIKTI("Yola Çıktı"),
    TESLIM_EDILDI("Teslim Edildi");

    private final String displayName;

    TripStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
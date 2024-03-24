package com.attijari.vocalbanking.Carte;

import lombok.RequiredArgsConstructor;

public enum CodeOffre {
    CODE_006("006"), // westart generalisee
    CODE_007("007"), // wetrust generalisee
    CODE_003("003"); // carte technologique

    private final String code;

    CodeOffre(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
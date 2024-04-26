package com.attijari.vocalbanking.Virement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirementRequest {
    private Long clientId;
    private String RIB;
    private float montant;
    private String motif;
}

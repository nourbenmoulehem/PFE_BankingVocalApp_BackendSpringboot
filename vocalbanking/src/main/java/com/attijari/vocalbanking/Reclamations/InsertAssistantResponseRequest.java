package com.attijari.vocalbanking.Reclamations;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsertAssistantResponseRequest {

    private long reclamationId;

    private String objet;

    private String descriptionClient;

    private String descriptionAssistant;

    private long clientId;
}

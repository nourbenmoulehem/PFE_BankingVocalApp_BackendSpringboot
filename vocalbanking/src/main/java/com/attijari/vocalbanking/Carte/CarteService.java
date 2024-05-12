package com.attijari.vocalbanking.Carte;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarteService {

    private final CarteRepository carteRepository;

    public Carte updateCarteStatus(int id_produit, String newStatus) {
        Carte carte = carteRepository.findByIdProduit(id_produit);
        if (carte == null) {
            throw new IllegalArgumentException("Carte Id invalide:" + id_produit);
        }
        carte.setStatus(newStatus);
        return carteRepository.save(carte);
    }
}

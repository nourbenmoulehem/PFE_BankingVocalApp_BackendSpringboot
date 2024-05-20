package com.attijari.vocalbanking.Carte;

import com.attijari.vocalbanking.CompteBancaire.CompteBancaire;
import com.attijari.vocalbanking.CompteBancaire.CompteBancaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarteService {

    private final CarteRepository carteRepository;
    private final CompteBancaireRepository compteBancaireRepository;


    public Carte updateCarteStatus(Long id_produit, String newStatus) {
        System.out.println("id_produit = " + id_produit);
        CompteBancaire compteBancaire1 = compteBancaireRepository.findByClientID(id_produit);
        Carte carte = compteBancaire1.getCarte();

        if (carte == null) {
            throw new IllegalArgumentException("Carte Id invalide:" + id_produit);
        }
        carte.setStatus(newStatus);
        return carteRepository.save(carte);
    }
}

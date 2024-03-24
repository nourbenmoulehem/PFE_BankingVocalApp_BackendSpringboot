package com.attijari.vocalbanking.CompteBancaire;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CompteBancaireService {

    CompteBancaireRepository compteBancaireRepository;

    public String generateUniqueRIB() {
        String rib;
        do {
            long min = (long) Math.pow(10, 22);
            long max = (long) Math.pow(10, 23) - 1;
            long randomNum = ThreadLocalRandom.current().nextLong(min, max);
            rib = String.valueOf(randomNum);
        } while(compteBancaireRepository.existsByRIB(rib));
        return rib;
    }
}

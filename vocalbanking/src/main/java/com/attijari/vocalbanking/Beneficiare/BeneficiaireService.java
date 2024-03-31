package com.attijari.vocalbanking.Beneficiare;

import com.attijari.vocalbanking.Client.Client;
import com.attijari.vocalbanking.Client.ClientRepository;
import com.attijari.vocalbanking.CompteBancaire.CompteBancaire;
import com.attijari.vocalbanking.Transaction.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BeneficiaireService {

    private final BeneficiareRepository beneficiaireRepository;
    private final ClientRepository clientRepository;

    public Client saveBeneficiaires(List<Beneficiaire> beneficiaires, Long idClient) {
        Optional<Client> clientOptional = clientRepository.findById(idClient);

        if(clientOptional.isPresent()) {
            Client client = clientOptional.get();
            for (Beneficiaire beneficiaire : beneficiaires) {
                beneficiaire.setClient(client);

                beneficiaireRepository.save(beneficiaire);
            }
            // insert ALL* benef to client
            client.setBeneficiairesList(beneficiaires);
            clientRepository.save(client);
            return client;
        } else {
            throw new RuntimeException("Compte bancaire not found");
        }
    }

    public List<Beneficiaire> getBeneficiairesByClient(Long idClient) {
        return beneficiaireRepository.findByClientClientId(idClient);
    }
}

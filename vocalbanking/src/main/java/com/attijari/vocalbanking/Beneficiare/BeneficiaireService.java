package com.attijari.vocalbanking.Beneficiare;

import com.attijari.vocalbanking.Client.Client;
import com.attijari.vocalbanking.Client.ClientRepository;
import lombok.RequiredArgsConstructor;
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

    public List<String> getNomsByClientId(Long idClient) {
        return beneficiaireRepository.findNomsByClientId(idClient);
    }

    public void saveBeneficiaire(Beneficiaire beneficiaire, Long idClient) {
        Optional<Client> clientOptional = clientRepository.findById(idClient);

        if(clientOptional.isPresent()) {
            Client client = clientOptional.get();
            beneficiaire.setClient(client);
//            client.setBeneficiairesList(List.of(beneficiaire));
            clientRepository.save(client);
            beneficiaireRepository.save(beneficiaire);
        } else {
            throw new RuntimeException("Erreur lors de l'insertion du beneficiaire");
        }
    }


    public void deleteBeneficiaire(Beneficiaire beneficiaire, Long idClient) {
        Optional<Client> clientOptional = clientRepository.findById(idClient);

        if(clientOptional.isPresent()) {
            Client client = clientOptional.get();
            beneficiaire.setClient(client);
            beneficiaireRepository.delete(beneficiaire);
        } else {
            throw new RuntimeException("Erreur lors de la suppression du beneficiaire");
        }
    }

    public void updateBeneficiaire(Beneficiaire beneficiaire, Long idClient) {
        Optional<Client> clientOptional = clientRepository.findById(idClient);

        if(clientOptional.isPresent()) {
            Client client = clientOptional.get();
            beneficiaire.setClient(client);
            beneficiaireRepository.save(beneficiaire);
        } else {
            throw new RuntimeException("Erreur lors de la mise à jour du beneficiaire");
        }
    }
}

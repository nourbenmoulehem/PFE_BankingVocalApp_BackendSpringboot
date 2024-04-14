package com.attijari.vocalbanking.Beneficiare;

import com.attijari.vocalbanking.Client.Client;
import com.attijari.vocalbanking.Client.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
        try {
            Optional<Client> clientOptional = clientRepository.findById(idClient);

            if(clientOptional.isPresent()) {
                Client client = clientOptional.get();

                // benefeciaire jdid nziidouh lel client
                beneficiaire.setClient(client);


                // ne5dhou lista mtaa beneficiaire mtaa client w nziidouh leha le beneficiaire jdid
                List<Beneficiaire> clientBenefeciaires =  client.getBeneficiairesList();
                // check if rib exists
                for (Beneficiaire b : clientBenefeciaires) {
                    if (b.getRib().equals(beneficiaire.getRib())) {
                        throw new RuntimeException("RIB existe déjà.");
                    }
                }
                clientBenefeciaires.add(beneficiaire);
                client.setBeneficiairesList(clientBenefeciaires);


                // saving
                clientRepository.save(client);
                beneficiaireRepository.save(beneficiaire);
            }
        }
        catch (Exception e) {
            System.out.println("Error Server: " + e);
            throw new RuntimeException("RIB existe déjà.");
        }

    }


    public void deleteBeneficiaire(Beneficiaire beneficiaire, Long idClient) {
        Optional<Client> clientOptional = clientRepository.findById(idClient);

        if(clientOptional.isPresent()) {
            Client client = clientOptional.get();
            beneficiaireRepository.deleteById((long) beneficiaire.getId());
        } else {
            throw new RuntimeException("Erreur lors de la suppression du beneficiaire");
        }
    }

    public void updateBeneficiaire(Beneficiaire updatedBeneficiaire, Long idClient) {
        Optional<Client> clientOptional = clientRepository.findById(idClient);

        if(clientOptional.isPresent()) {
            Client client = clientOptional.get();
            updatedBeneficiaire.setClient(client);
            Optional<Beneficiaire> optionalBeneficiaire = beneficiaireRepository.findById((long) updatedBeneficiaire.getId());
            if(optionalBeneficiaire.isPresent()) {
                Beneficiaire beneficiaire = optionalBeneficiaire.get();
                beneficiaire.setNom(updatedBeneficiaire.getNom());
                beneficiaireRepository.save(beneficiaire);
            } else {
                throw new RuntimeException("Beneficiaire n'existe pas");
            }
        } else {
            throw new RuntimeException("Erreur lors de la mise à jour du beneficiaire");
        }
    }
}

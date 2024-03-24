package com.attijari.vocalbanking.service;

import com.attijari.vocalbanking.Client.Client;
import lombok.*;
import org.springframework.stereotype.Service;
import com.attijari.vocalbanking.Client.ClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client getClientByCin(String cin) {
        return clientRepository.findByCin(cin);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
}

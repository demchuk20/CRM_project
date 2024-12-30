package org.project.crm.service;

import lombok.RequiredArgsConstructor;
import org.project.crm.entity.Client;
import org.project.crm.repository.ClientRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final static String CACHE_NAME = "clients";
    private final CacheService cacheService;
    private final ClientRepository clientRepository;

    @CacheEvict(cacheNames = CACHE_NAME)
    public Client save(Client client) {
        Client saved = clientRepository.save(client);
        cacheService.evictAllCacheValues(CACHE_NAME);
        return saved;
    }

    public Client findById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Cacheable(CACHE_NAME)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @CacheEvict(CACHE_NAME)
    public void delete(Long id) {
        clientRepository.deleteById(id);
        cacheService.evictAllCacheValues(CACHE_NAME);
    }
}

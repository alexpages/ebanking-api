package com.alexpages.ebankingapi.service;

import com.alexpages.ebankingapi.domain.AuthenticateRequest;
import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.error.EbankingManagerException;
import com.alexpages.ebankingapi.mapper.EbankingMapper;
import com.alexpages.ebankingapi.others.AuthenticationResponse;
import com.alexpages.ebankingapi.repository.ClientRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class ClientService {

    private final ClientRepository clientRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
	private final EbankingMapper ebankingMapper;
    
	public Optional<ClientEntity> findClientByName(String name){
        return clientRepository.findClientByName(name);
    }

    public ClientEntity addClient (Client client)
    {
    	try 
    	{ 	    
    	    String encodedPassword = passwordEncoder.encode(client.getPassword());
    	    client.setPassword(encodedPassword);
    	    return clientRepository.saveAndFlush(ebankingMapper.toClientEntity(client));
    	}
    	catch(Exception e)
    	{
    		throw new EbankingManagerException("Error when adding client");
    	}
    }
    
    public AuthenticationResponse authenticateToken(AuthenticateRequest request) 
    {
    	try 
    	{
        	String clientName= request.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));  
            Optional<ClientEntity> clientOptional = clientRepository.findClientByName(clientName);
    	    if (!clientOptional.isPresent())
    	    {
    	    	log.error("ClientService > authenticateToken > Client: " + clientName + ", could not be authenticated");
    	        throw new EbankingManagerException("Client could not be registered");
    	    }  
    	    log.info("ClientService > authenticateToken > Client authenticated successfully: {}", clientOptional);
            return generateToken(clientOptional.get());	
    	}
    	catch (Exception e)
    	{
    		throw new EbankingManagerException("Client could not be registered");
    	}
    }
    
    public AuthenticationResponse generateToken(ClientEntity clientEntity) 
    {
	    AuthenticationResponse response = new AuthenticationResponse();
	    response.setToken(jwtService.generateToken(clientEntity));
	    return response;
    }
}

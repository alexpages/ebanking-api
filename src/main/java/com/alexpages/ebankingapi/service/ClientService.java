package com.alexpages.ebankingapi.service;

import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.error.EbankingManagerException;
import com.alexpages.ebankingapi.mapper.EbankingMapper;
import com.alexpages.ebankingapi.others.AuthenticateRequest;
import com.alexpages.ebankingapi.others.AuthenticationResponse;
import com.alexpages.ebankingapi.repository.ClientRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
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
@Validated
public class ClientService {

    private ClientRepository clientRepository;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
	
    @Autowired
    EbankingMapper ebankingMapper;
    
    public ClientService()
    {
    	
    }
    
    @Autowired
    public ClientService(ClientRepository clientRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) 
    {
		this.clientRepository 	= clientRepository;
		this.jwtService 		= jwtService;
		this.passwordEncoder 	= passwordEncoder;
		this.authenticationManager = authenticationManager;
	}

	public Optional<ClientEntity> findClientByName(String name){
        return clientRepository.findClientByName(name);
    }

    public ClientEntity addClient (Client client)
    {
    	try 
    	{ 	    
    	    String encodedPassword = passwordEncoder.encode(client.getPassword());
    	    client.setPassword(encodedPassword);
    	    ClientEntity clientEntity = ebankingMapper.toClientEntity(client);
    	    return clientRepository.save(clientEntity);
    	}
    	catch(Exception e)
    	{
    		throw new EbankingManagerException("Error when adding client");
    	}
    }
    
    public AuthenticationResponse authenticateToken(AuthenticateRequest request) 
    {
    	String clientName= request.getClientName();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        request.getClientName(),
                        request.getPassword()));
        
        Optional<ClientEntity> clientOptional = clientRepository.findClientByName(clientName);
	    if (clientOptional.isPresent())
	    {
	    	log.error("ClientService > authenticateToken > Client: " + clientName + ", could not be authenticated");
	        throw new EbankingManagerException("Client could not be registered");
	    }  
	    log.info("ClientService > authenticateToken > Client authenticated successfully: {}", clientOptional.get().toString());
        return generateToken(clientOptional.get());
    }
    
    public AuthenticationResponse generateToken(ClientEntity clientEntity) 
    {
	    AuthenticationResponse response = new AuthenticationResponse();
	    response.setToken(jwtService.generateToken(clientEntity));
	    return response;
    }

}

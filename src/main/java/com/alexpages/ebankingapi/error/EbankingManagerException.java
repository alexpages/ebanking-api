package com.alexpages.ebankingapi.error;

import org.springframework.http.HttpStatus;

public class EbankingManagerException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public EbankingManagerException(String message) {
        super(message);
    }
}

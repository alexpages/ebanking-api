//package com.alexpages.ebankingapi.model.client;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;
//
//@AllArgsConstructor
//@Getter
//@Setter
//@Entity
//@Table(name = "accounts")
//public class Account {
//    private String IBAN; //TODO add checks for not having duplicated IBAN
//    private String currency;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "client_id")
//    private Client client;
//}

package com.homeBanking.cardsservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long accountId;
    private String holder;
    private String number;
    private LocalDate expirationDate;
    private String cvv;

    public Card(Long accountId, String holder, String number, LocalDate expirationDate, String cvv) {
        this.accountId = accountId;
        this.holder = holder;
        this.number = number;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
    }
}

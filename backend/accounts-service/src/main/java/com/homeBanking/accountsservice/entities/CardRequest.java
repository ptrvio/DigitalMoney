package com.homeBanking.accountsservice.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class CardRequest {
    @NotBlank(message = "El nombre y apellido es obligatorio")
    private String name;
    @NotBlank(message = "El numero de tarjeta es obligatorio")
    @Size(min = 16, message = "Debe ingresar los 16 numeros de su tarjeta")
    private String number;
    @NotBlank(message = "El mes y el año de expiracion de la tarjeta es obligatorio")
    @Size(min = 4, message = "Debe ingresar los 4 numeros que representan mm/aa")
    private String expiration;
    @NotBlank(message = "El codigo de seguridad de su tarjeta es obligatorio, ver al dorso")
    @Size(min = 3, message = "Debe ingresar los 3 numeros que representan el cvc")
    private String cvc;
}

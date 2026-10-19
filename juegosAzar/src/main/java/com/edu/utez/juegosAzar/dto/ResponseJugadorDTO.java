package com.edu.utez.juegosAzar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseJugadorDTO {
    private Long id;
    private String nombre;
    private double saldo;
    private boolean activo;
}
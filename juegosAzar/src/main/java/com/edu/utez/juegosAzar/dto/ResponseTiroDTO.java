package com.edu.utez.juegosAzar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTiroDTO {
    private Long id;
    private Long partidaId;
    private int valorDado1;
    private int valorDado2;
    private int suma;
    private boolean esGanador;
    private String resultado;
    private String estadoPartida;
}
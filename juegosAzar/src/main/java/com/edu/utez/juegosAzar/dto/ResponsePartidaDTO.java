package com.edu.utez.juegosAzar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.edu.utez.juegosAzar.model.partida.EstadoPartida;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePartidaDTO {
    private Long id;
    private LocalDateTime fecha;
    private EstadoPartida estado;
    private Long jugadorId;
    private double apuesta;
}
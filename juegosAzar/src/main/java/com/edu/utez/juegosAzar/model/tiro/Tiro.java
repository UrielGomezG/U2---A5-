package com.edu.utez.juegosAzar.model.tiro;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.edu.utez.juegosAzar.model.partida.Partida;

@Entity
@Table(name = "tiros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partida_id", nullable = false)
    private Partida partida;

    @Column(nullable = false)
    private int valorDado1;

    @Column(nullable = false)
    private int valorDado2;

    @Column(nullable = false)
    private int suma;

    @Column(nullable = false)
    private boolean esGanador;
}
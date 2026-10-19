package com.edu.utez.juegosAzar.model.jugador;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "jugadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private double saldo;

    @Column(nullable = false)
    private boolean activo;

    public Jugador(String nombre, double saldo, boolean activo) {
        this.nombre = nombre;
        this.saldo = saldo;
        this.activo = activo;
    }
}
package com.edu.utez.juegosAzar.repository.jugador;
import com.edu.utez.juegosAzar.model.jugador.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {
    
}

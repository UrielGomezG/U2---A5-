package com.edu.utez.juegosAzar.repository.tiro;

import com.edu.utez.juegosAzar.model.tiro.Tiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiroRepository extends JpaRepository<Tiro, Long> {
    
}
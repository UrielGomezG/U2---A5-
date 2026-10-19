package com.edu.utez.juegosAzar.controller;

import jakarta.validation.Valid;
import com.edu.utez.juegosAzar.dto.*;
import com.edu.utez.juegosAzar.service.JuegoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/juego")
public class JuegoController {

    private final JuegoService juegoService;

    public JuegoController(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    @PostMapping("/jugadores")
    public ResponseEntity<ResponseJugadorDTO> crearJugador(@Valid @RequestBody RequestCrearJugadorDTO payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(juegoService.crearJugador(payload));
    }


    @PostMapping("/iniciar")
    public ResponseEntity<ResponsePartidaDTO> iniciarPartida(@Valid @RequestBody RequestPartidaDTO payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(juegoService.iniciarPartida(payload));
    }

    @PostMapping("/tiro/{partidaId}")
    public ResponseEntity<ResponseTiroDTO> realizarTiro(@PathVariable Long partidaId) {
        return ResponseEntity.ok(juegoService.realizarTiro(partidaId));
    }

    @PatchMapping("/finalizar/{partidaId}")
    public ResponseEntity<ResponsePartidaDTO> finalizarPartidaManual(@PathVariable Long partidaId) {
        return ResponseEntity.ok(juegoService.finalizarPartidaManual(partidaId));
    }

    @PostMapping("/jugadores/{jugadorId}/recargar")
    public ResponseEntity<ResponseJugadorDTO> recargarSaldo(@PathVariable Long jugadorId, @Valid @RequestBody RequestRecargaDTO payload) {
        return ResponseEntity.ok(juegoService.recargarSaldo(jugadorId, payload));
    }

    @GetMapping("/jugadores/{jugadorId}/historial")
    public ResponseEntity<List<ResponsePartidaDTO>> obtenerHistorialPartida(@PathVariable Long jugadorId) {
        return ResponseEntity.ok(juegoService.obtenerHistorialPartida(jugadorId));
    }
}
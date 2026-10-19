package com.edu.utez.juegosAzar.service;

import com.edu.utez.juegosAzar.dto.*;
import com.edu.utez.juegosAzar.exception.RecursoNoEncontradoException;
import com.edu.utez.juegosAzar.exception.ReglaNegocioException;
import com.edu.utez.juegosAzar.model.jugador.Jugador;
import com.edu.utez.juegosAzar.model.partida.EstadoPartida;
import com.edu.utez.juegosAzar.model.partida.Partida;
import com.edu.utez.juegosAzar.model.tiro.Tiro;
import com.edu.utez.juegosAzar.repository.jugador.JugadorRepository;
import com.edu.utez.juegosAzar.repository.partida.PartidaRepository;
import com.edu.utez.juegosAzar.repository.tiro.TiroRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class JuegoService {

    private final JugadorRepository jugadorRepository;
    private final PartidaRepository partidaRepository;
    private final TiroRepository tiroRepository;
    private final Random random = new Random();

    public JuegoService(JugadorRepository jugadorRepository,
                        PartidaRepository partidaRepository,
                        TiroRepository tiroRepository) {
        this.jugadorRepository = jugadorRepository;
        this.partidaRepository = partidaRepository;
        this.tiroRepository = tiroRepository;
    }

    public ResponseJugadorDTO crearJugador(RequestCrearJugadorDTO payload) {
        Jugador jugador = new Jugador(payload.getNombre(), payload.getSaldo(), true);
        jugador = jugadorRepository.save(jugador);

        ResponseJugadorDTO respuesta = new ResponseJugadorDTO();
        respuesta.setId(jugador.getId());
        respuesta.setNombre(jugador.getNombre());
        respuesta.setSaldo(jugador.getSaldo());
        respuesta.setActivo(jugador.isActivo());

        return respuesta;
    }

    public ResponsePartidaDTO iniciarPartida(RequestPartidaDTO payload) {
        Jugador jugador = jugadorRepository.findById(payload.getJugadorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el jugador con ID " + payload.getJugadorId()));

        if (!jugador.isActivo()) {
            throw new ReglaNegocioException("El jugador se encuentra inactivo");
        }

        if (jugador.getSaldo() < payload.getApuesta()) {
            throw new ReglaNegocioException("Saldo insuficiente para realizar la apuesta");
        }

        // Descontar apuesta del saldo
        jugador.setSaldo(jugador.getSaldo() - payload.getApuesta());
        jugadorRepository.save(jugador);

        Partida partida = new Partida();
        partida.setFecha(LocalDateTime.now());
        partida.setEstado(EstadoPartida.EN_JUEGO);
        partida.setJugador(jugador);
        partida.setApuesta(payload.getApuesta());

        partida = partidaRepository.save(partida);

        ResponsePartidaDTO respuesta = new ResponsePartidaDTO();
        respuesta.setId(partida.getId());
        respuesta.setFecha(partida.getFecha());
        respuesta.setEstado(partida.getEstado());
        respuesta.setJugadorId(jugador.getId());
        respuesta.setApuesta(partida.getApuesta());

        return respuesta;
    }

    public ResponseTiroDTO realizarTiro(Long partidaId) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la partida con ID " + partidaId));

        if (partida.getEstado() != EstadoPartida.EN_JUEGO) {
            throw new ReglaNegocioException("La partida ya se encuentra " + partida.getEstado());
        }

        int dado1 = random.nextInt(6) + 1;
        int dado2 = random.nextInt(6) + 1;
        int suma = dado1 + dado2;

        boolean esGanador = false;
        String resultado = "CONTINÚA";

        if (suma == 7 || suma == 11) {
            esGanador = true;
            resultado = "GANÓ";
            partida.setEstado(EstadoPartida.FINALIZADA);

            Jugador jugador = partida.getJugador();
            double ganancia = partida.getApuesta() * 2;
            jugador.setSaldo(jugador.getSaldo() + ganancia);
            jugadorRepository.save(jugador);
        } else if (suma == 2 || suma == 3 || suma == 12) {
            resultado = "PERDIÓ";
            partida.setEstado(EstadoPartida.FINALIZADA);
        }

        partidaRepository.save(partida);

        Tiro tiro = new Tiro();
        tiro.setPartida(partida);
        tiro.setValorDado1(dado1);
        tiro.setValorDado2(dado2);
        tiro.setSuma(suma);
        tiro.setEsGanador(esGanador);

        tiro = tiroRepository.save(tiro);

        ResponseTiroDTO respuesta = new ResponseTiroDTO();
        respuesta.setId(tiro.getId());
        respuesta.setPartidaId(partida.getId());
        respuesta.setValorDado1(tiro.getValorDado1());
        respuesta.setValorDado2(tiro.getValorDado2());
        respuesta.setSuma(tiro.getSuma());
        respuesta.setEsGanador(tiro.isEsGanador());
        respuesta.setResultado(resultado);
        respuesta.setEstadoPartida(partida.getEstado().name());

        return respuesta;
    }

    public ResponsePartidaDTO finalizarPartidaManual(Long partidaId) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la partida con ID " + partidaId));

        if (partida.getEstado() == EstadoPartida.FINALIZADA) {
            throw new ReglaNegocioException("La partida ya estaba FINALIZADA");
        }

        partida.setEstado(EstadoPartida.FINALIZADA);
        partida = partidaRepository.save(partida);

        ResponsePartidaDTO respuesta = new ResponsePartidaDTO();
        respuesta.setId(partida.getId());
        respuesta.setFecha(partida.getFecha());
        respuesta.setEstado(partida.getEstado());
        respuesta.setJugadorId(partida.getJugador().getId());
        respuesta.setApuesta(partida.getApuesta());

        return respuesta;
    }

    public ResponseJugadorDTO recargarSaldo(Long jugadorId, RequestRecargaDTO payload) {
        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el jugador con ID " + jugadorId));

        jugador.setSaldo(jugador.getSaldo() + payload.getCantidad());
        jugador = jugadorRepository.save(jugador);

        ResponseJugadorDTO respuesta = new ResponseJugadorDTO();
        respuesta.setId(jugador.getId());
        respuesta.setNombre(jugador.getNombre());
        respuesta.setSaldo(jugador.getSaldo());
        respuesta.setActivo(jugador.isActivo());

        return respuesta;
    }

    public List<ResponsePartidaDTO> obtenerHistorialPartida(Long jugadorId) {
        if (!jugadorRepository.existsById(jugadorId)) {
            throw new RecursoNoEncontradoException("No existe el jugador con ID " + jugadorId);
        }

        List<Partida> partidas = partidaRepository.findByJugadorId(jugadorId);
        List<ResponsePartidaDTO> listaRespuesta = new ArrayList<>();

        for (Partida p : partidas) {
            ResponsePartidaDTO respuesta = new ResponsePartidaDTO();
            respuesta.setId(p.getId());
            respuesta.setFecha(p.getFecha());
            respuesta.setEstado(p.getEstado());
            respuesta.setJugadorId(p.getJugador().getId());
            respuesta.setApuesta(p.getApuesta());
            listaRespuesta.add(respuesta);
        }

        return listaRespuesta;
    }
}
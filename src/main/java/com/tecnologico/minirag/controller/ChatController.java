package com.tecnologico.minirag.controller;

import com.tecnologico.minirag.model.Consulta;
import com.tecnologico.minirag.repository.ConsultaRepository;
import com.tecnologico.minirag.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;
    private final ConsultaRepository consultaRepository;

    public ChatController(ChatService chatService, ConsultaRepository consultaRepository) {
        this.chatService = chatService;
        this.consultaRepository = consultaRepository;
    }

    @PostMapping("/chat")
    public Map<String, String> preguntar(@RequestBody Map<String, String> body) {
        String pregunta = body.get("pregunta");

        if (pregunta == null || pregunta.isBlank()) {
            return Map.of("respuesta", "Debe ingresar una pregunta.");
        }

        String respuesta = chatService.preguntar(pregunta);
        return Map.of("respuesta", respuesta);
    }

    @GetMapping("/consultas")
    public List<Consulta> historial() {
        return consultaRepository.findAll();
    }

    @GetMapping("/salud")
    public Map<String, String> salud() {
        return Map.of("estado", "OK", "aplicacion", "MiniRAG");
    }
}

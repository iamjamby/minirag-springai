package com.tecnologico.minirag;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnologico.minirag.model.Consulta;
import com.tecnologico.minirag.repository.ConsultaRepository;
import com.tecnologico.minirag.service.ChatService;
import com.tecnologico.minirag.service.DocumentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MiniragApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmbeddingModel embeddingModel;

    @MockitoBean
    private VectorStore vectorStore;

    @MockitoBean
    private DocumentService documentService;

    @MockitoBean
    private ChatService chatService;

    @MockitoBean
    private ConsultaRepository consultaRepository;

    @Test
    @DisplayName("1. El contexto de Spring Boot carga correctamente")
    void contextLoads() {
        assertNotNull(mockMvc);
    }

    @Test
    @DisplayName("2. GET /api/salud retorna estado OK y aplicación MiniRAG")
    void endpointSaludRetornaOk() throws Exception {
        mockMvc.perform(get("/api/salud"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("OK"))
                .andExpect(jsonPath("$.aplicacion").value("MiniRAG"));
    }

    @Test
    @DisplayName("3. POST /api/chat procesa la pregunta y retorna la respuesta RAG")
    void endpointChatPreguntarRetornaRespuesta() throws Exception {
        String pregunta = "¿Qué es Spring Boot?";
        String respuestaMock = "Spring Boot es un proyecto del ecosistema Spring que facilita...";

        when(chatService.preguntar(pregunta)).thenReturn(respuestaMock);

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("pregunta", pregunta))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.respuesta").value(respuestaMock));
    }

    @Test
    @DisplayName("4. POST /api/chat con pregunta vacía solicita ingresar una pregunta")
    void endpointChatValidaPreguntaVacia() throws Exception {
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("pregunta", ""))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.respuesta").value("Debe ingresar una pregunta."));
    }

    @Test
    @DisplayName("5. GET /api/consultas retorna el historial de consultas")
    void endpointConsultasHistorial() throws Exception {
        Consulta consulta1 = new Consulta("¿Qué es un embedding?", "Un embedding es una representación numérica...");
        when(consultaRepository.findAll()).thenReturn(List.of(consulta1));

        mockMvc.perform(get("/api/consultas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pregunta").value("¿Qué es un embedding?"))
                .andExpect(jsonPath("$[0].respuesta").value("Un embedding es una representación numérica..."));
    }

    @Test
    @DisplayName("6. Entidad Consulta gestiona correctamente sus propiedades y fecha")
    void entidadConsultaPropiedades() {
        Consulta consulta = new Consulta("¿Pregunta?", "Respuesta");
        assertEquals("¿Pregunta?", consulta.getPregunta());
        assertEquals("Respuesta", consulta.getRespuesta());
        assertNotNull(consulta.getFecha());

        consulta.setPregunta("Nueva");
        consulta.setRespuesta("Nueva R");
        assertEquals("Nueva", consulta.getPregunta());
        assertEquals("Nueva R", consulta.getRespuesta());
    }
}

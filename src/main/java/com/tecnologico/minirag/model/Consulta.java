package com.tecnologico.minirag.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String pregunta;

    @Lob
    @Column(nullable = false)
    private String respuesta;

    private LocalDateTime fecha;

    public Consulta() {
    }

    public Consulta(String pregunta, String respuesta) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.fecha = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}

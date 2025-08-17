package com.forohubalura.forohubalura.DTO;

import com.forohubalura.forohubalura.modelo.Topico;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TopicoDTOactualizador(
       @NotNull @Min(1) @NotBlank (message = "El id no puede estar en blanco") Long id,
       @NotBlank(message = "El titulo no puede estar en blanco") String titulo,
       @NotBlank(message = "El mensaje no puede estar en blanco") String mensaje,
       @NotNull(message = "La fecha no puede estar en blanco") LocalDateTime creado,
       @NotBlank(message = "El status no puede estar en blanco") String status,
       @NotBlank(message = "El autor no puede estar en blanco") String autor,
       @NotBlank(message = "El curso no puede estar en blanco") String curso
) {
    public TopicoDTOactualizador(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getCreado(),
                topico.getStatus(),
                topico.getAutor(),
                topico.getCurso()
        );
    }

    public Topico crearTopico(){
        return new Topico(this.id, this.titulo, this.mensaje, this.creado, this.status, this.autor, this.curso);
    }

}

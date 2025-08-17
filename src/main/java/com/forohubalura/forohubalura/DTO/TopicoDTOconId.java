package com.forohubalura.forohubalura.DTO;

import com.forohubalura.forohubalura.modelo.Topico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TopicoDTOconId(
       long id,
       String titulo,
       String mensaje,
       LocalDateTime creado,
       String status,
       String autor,
       String curso
) {
    public TopicoDTOconId(Topico topico) {
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

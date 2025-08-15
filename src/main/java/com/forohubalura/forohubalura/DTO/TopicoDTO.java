package com.forohubalura.forohubalura.DTO;

import com.forohubalura.forohubalura.modelo.Topico;



import java.time.LocalDateTime;

public record TopicoDTO(
        String titulo,
        String mensaje,
        LocalDateTime creado,
        String status,
        String autor,
        String curso
) {
    public TopicoDTO(Topico topico) {
        this(
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getCreado(),
                topico.getStatus(),
                topico.getAutor(),
                topico.getCurso()
        );
    }

    public Topico crearTopico(){
        return new Topico(0, this.titulo, this.mensaje, this.creado, this.status, this.autor, this.curso);
    }

}

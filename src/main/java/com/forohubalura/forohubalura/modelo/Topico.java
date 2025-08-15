package com.forohubalura.forohubalura.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="topico")
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String titulo;
    private String mensaje;
    @Column(name = "fecha_creacion")
    private LocalDateTime creado;
    private String status;
    private String autor;
    private String curso;

}



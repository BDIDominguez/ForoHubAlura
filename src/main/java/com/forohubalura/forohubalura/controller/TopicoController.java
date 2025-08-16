package com.forohubalura.forohubalura.controller;

import com.forohubalura.forohubalura.DTO.TopicoDTO;
import com.forohubalura.forohubalura.modelo.Topico;
import com.forohubalura.forohubalura.service.TopicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topico")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    @PostMapping()
    public ResponseEntity<TopicoDTO> cargarTopico(@Valid @RequestBody TopicoDTO topicoDTO){
        Topico topico = topicoDTO.crearTopico();
        topico = topicoService.crearTopico(topico);
        TopicoDTO respuesta = new TopicoDTO(topico);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @GetMapping()
    public ResponseEntity<List<TopicoDTO>> consultarTodosLosTopicos(){
        List<Topico> lista = topicoService.consultarTodos();
        List<TopicoDTO> listaDTO = lista.stream().map(TopicoDTO::new).toList();
        return ResponseEntity.ok(listaDTO);
    }

}

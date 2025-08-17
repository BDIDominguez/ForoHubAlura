package com.forohubalura.forohubalura.controller;

import com.forohubalura.forohubalura.DTO.TopicoDTO;
import com.forohubalura.forohubalura.DTO.TopicoDTOactualizador;
import com.forohubalura.forohubalura.DTO.TopicoDTOconId;
import com.forohubalura.forohubalura.modelo.Topico;
import com.forohubalura.forohubalura.service.TopicoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topico")
@Validated
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    @PostMapping()
    public ResponseEntity<TopicoDTOconId> cargarTopico(@Valid @RequestBody TopicoDTO topicoDTO){
        Topico topico = topicoDTO.crearTopico();
        topico = topicoService.crearTopico(topico);
        TopicoDTOconId respuesta = new TopicoDTOconId(topico);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @GetMapping()
    public ResponseEntity<List<TopicoDTOconId>> consultarTodosLosTopicos(){
        List<Topico> lista = topicoService.consultarTodos();
        List<TopicoDTOconId> listaDTO = lista.stream().map(TopicoDTOconId::new).toList();
        return ResponseEntity.ok(listaDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicoDTOconId> consultarTopicoId(@PathVariable @NotNull @Min(1) Long id){
        Topico topico = topicoService.buscarPorId(id);
        TopicoDTOconId respuesta = new TopicoDTOconId(topico);
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicoDTOconId> actualizarTopico(@PathVariable @NotNull @Min(1) Long id, @Valid @RequestBody TopicoDTO topicoDTO){
        Topico respuesta = topicoService.buscarPorId(id);
        Topico actualizar = topicoDTO.crearTopico();
        actualizar.setId(id);
        Topico nuevo = topicoService.crearTopico(actualizar);
        return ResponseEntity.ok(new TopicoDTOconId(nuevo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TopicoDTOconId> eliminarTopico(@PathVariable @NotNull @Min(1) Long id){
        Topico existe = topicoService.buscarPorId(id);
        topicoService.eliminarTopicoPorID(id);
        return ResponseEntity.noContent().build();
    }
}

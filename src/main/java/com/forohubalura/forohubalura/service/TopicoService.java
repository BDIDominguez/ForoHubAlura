package com.forohubalura.forohubalura.service;

import com.forohubalura.forohubalura.excepciones.RecursoNoEncontradoException;
import com.forohubalura.forohubalura.modelo.Topico;
import com.forohubalura.forohubalura.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicoService {
    @Autowired
    private TopicoRepository topicoRepository;

    public Topico crearTopico(Topico topico) {
        try{
            return topicoRepository.save(topico);
        } catch (DataIntegrityViolationException e){
            throw new IllegalArgumentException("Duplicado: el titulo y el mensaje ya existen");
        }
    }

    public List<Topico> consultarTodos() {
        return topicoRepository.findAll();
    }

    public Topico buscarPorId(Long id) {
        return   topicoRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("El topico con id " + id + " no existe"));
    }

    public void eliminarTopicoPorID(Long id) {
        try{
            topicoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new RecursoNoEncontradoException("El Topico con el ID " + id + " no existe.");
        }
    }
}

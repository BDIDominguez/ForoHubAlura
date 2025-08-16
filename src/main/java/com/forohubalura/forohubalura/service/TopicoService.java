package com.forohubalura.forohubalura.service;

import com.forohubalura.forohubalura.modelo.Topico;
import com.forohubalura.forohubalura.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
}

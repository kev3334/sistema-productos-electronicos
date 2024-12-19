package com.egg.sistema_electro.servicios;

import com.egg.sistema_electro.entidades.Fabrica;
import com.egg.sistema_electro.excepciones.MiExcepcion;
import com.egg.sistema_electro.repositorios.FabricaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class FabricaServicio {

    @Autowired
    private FabricaRepositorio fabricaRepositorio;

    @Transactional
    public void crearFabrica(String nombre) throws MiExcepcion {
        validar(nombre);
        Fabrica fabrica = new Fabrica();
        fabrica.setNombre(nombre);

        fabricaRepositorio.save(fabrica);

    }

    @Transactional(readOnly = true)
    public List<Fabrica> listarFabricas(){
        List<Fabrica> fabricas = new ArrayList<>();

        fabricas = fabricaRepositorio.findAll();

        return fabricas;
    }

    @Transactional
    public void actualizar(String nombre, String id) throws MiExcepcion {
        validar(nombre);
        Optional<Fabrica> resultado = fabricaRepositorio.findById(id);
        if(resultado.isPresent()){
            Fabrica fabrica = resultado.get();
            fabrica.setNombre(nombre);
            fabricaRepositorio.save(fabrica);
        }
    }

    @Transactional(readOnly = true)
    public Fabrica getOne(String id){
        return fabricaRepositorio.getReferenceById(id);
    }


    public void validar(String nombre) throws MiExcepcion{
        if(nombre.isEmpty() || nombre == null){
            throw new MiExcepcion("El nombre no puede ser nulo o vacío.");
        }
    }

}

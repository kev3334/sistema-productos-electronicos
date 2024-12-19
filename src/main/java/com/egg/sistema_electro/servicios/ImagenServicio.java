package com.egg.sistema_electro.servicios;

import com.egg.sistema_electro.entidades.Imagen;
import com.egg.sistema_electro.excepciones.MiExcepcion;
import com.egg.sistema_electro.repositorios.ImagenRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ImagenServicio {

    @Autowired
    private ImagenRepositorio imagenRepositorio;

    @Transactional
    public Imagen guardar(MultipartFile archivo) throws MiExcepcion {
        if(archivo != null){
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());

                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }
        return null;
    }

    @Transactional
    public Imagen actualizar(MultipartFile archivo, String idImagen){
        if(archivo != null){
            try {
                Imagen imagen = new Imagen();

                if(idImagen != null){
                    Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);
                    if(respuesta.isPresent()){
                        imagen = respuesta.get();
                    }
                }

                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());

                return imagenRepositorio.save(imagen);

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;

    }

    @Transactional(readOnly = true)
    public List<Imagen> listarTodos(){
        return imagenRepositorio.findAll();
    }


}

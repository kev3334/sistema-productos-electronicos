package com.egg.sistema_electro.servicios;

import com.egg.sistema_electro.entidades.Articulo;
import com.egg.sistema_electro.entidades.Fabrica;
import com.egg.sistema_electro.entidades.Imagen;
import com.egg.sistema_electro.excepciones.MiExcepcion;
import com.egg.sistema_electro.repositorios.ArticuloRepositorio;
import com.egg.sistema_electro.repositorios.FabricaRepositorio;
import com.egg.sistema_electro.repositorios.ImagenRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticuloServicio {

    @Autowired
    private ArticuloRepositorio articuloRepositorio;

    @Autowired
    private FabricaRepositorio fabricaRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void crearArticulo(String nombre, String idFabrica, MultipartFile archivo, String descripcion ) throws MiExcepcion{
        validar(nombre, idFabrica, descripcion);

        Articulo articulo = new Articulo();
        Fabrica fabrica = fabricaRepositorio.findById(idFabrica).get();
        Imagen imagen = imagenServicio.guardar(archivo);

        articulo.setNombre(nombre);
        articulo.setFabrica(fabrica);
        articulo.setImagen(imagen);
        articulo.setDescripcion(descripcion);

        articuloRepositorio.save(articulo);
    }

    @Transactional
    public void actualizar(String idArticulo, String nombre, String idFabrica, MultipartFile archivo,  String descripcion) throws MiExcepcion {
        validar(nombre, idFabrica, descripcion);

        Articulo articulo = new Articulo();
        Fabrica fabrica = fabricaRepositorio.findById(idFabrica).get(); //no sería mejor que fabrica tenga su servicio para actualizar? asi como imagen

        Optional<Articulo> respuesta = articuloRepositorio.findById(idArticulo);

        if(respuesta.isPresent()){
            articulo = respuesta.get();
            articulo.setNombre(nombre);
            articulo.setFabrica(fabrica);
            articulo.setDescripcion(descripcion);

            //imagen
            if (archivo != null && !archivo.isEmpty()) {
                String idImagen = null;
                if(articulo.getImagen() != null){
                    idImagen = articulo.getImagen().getId();
                }
                Imagen imagen = imagenServicio.actualizar(archivo,idImagen);

                if(imagen != null){
                    articulo.setImagen(imagen);
                }
            }

//            String idImagen = null;
//            if(articulo.getImagen() != null){
//                idImagen = articulo.getImagen().getId();
//            }
//            Imagen imagen = imagenServicio.actualizar(archivo,idImagen);
//
//            if(imagen != null){
//                articulo.setImagen(imagen);
//            }

            articuloRepositorio.save(articulo);

        }
    }

    @Transactional(readOnly = true)
    public List<Articulo> listarArticulos(){
        List<Articulo> articulos = new ArrayList<>();
        articulos = articuloRepositorio.findAll();
        return articulos;
    }

    @Transactional(readOnly = true)
    public Articulo getOne(String id){
        return articuloRepositorio.getReferenceById(id);
    }

    public void validar(String nombre, String idFabrica, String descripcion) throws MiExcepcion{
        if (nombre == null || nombre.isEmpty()) {
            throw new MiExcepcion("el nombre no puede ser nulo o estar vacío");
        }
        if (idFabrica == null ||  idFabrica.isEmpty() ) {
            throw new MiExcepcion("la fabrica no puede estar vacío");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new MiExcepcion("La descripcion no puede ser nulo o estar vacía");
        }

    }
}

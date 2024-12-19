package com.egg.sistema_electro.controladores;

import com.egg.sistema_electro.entidades.Articulo;
import com.egg.sistema_electro.entidades.Fabrica;
import com.egg.sistema_electro.excepciones.MiExcepcion;
import com.egg.sistema_electro.servicios.ArticuloServicio;
import com.egg.sistema_electro.servicios.FabricaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/articulo")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public class ArticuloControlador {

    @Autowired
    private ArticuloServicio articuloServicio;

    @Autowired
    private FabricaServicio fabricaServicio;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/registrar")
    public String registrar(ModelMap model){
        List<Fabrica> fabricas = new ArrayList<>();
        fabricas = fabricaServicio.listarFabricas();
        model.addAttribute("fabricas", fabricas);

        return "articulo_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) String nombre, MultipartFile archivo, @RequestParam(required = false) String idFabrica,
                           @RequestParam(required = false) String descripcion, RedirectAttributes redirectAttributes){

        try {
            articuloServicio.crearArticulo(nombre, idFabrica, archivo, descripcion);
            redirectAttributes.addFlashAttribute("exito", "El artículo fue registrado correctamente");

        } catch (MiExcepcion e) {
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            return "redirect:/articulo/registrar";
        }

        return "redirect:/articulo/registrar";
    }

    @GetMapping("/lista")
    public String listar(ModelMap model){
        List<Articulo> articulos = articuloServicio.listarArticulos();
        model.addAttribute("articulos", articulos);

        return "articulo_list.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap model){
        Articulo articulo = articuloServicio.getOne(id);
        model.addAttribute("articulo", articulo);

        List<Fabrica> fabricas = new ArrayList<>();
        fabricas = fabricaServicio.listarFabricas();
        model.addAttribute("fabricas", fabricas);

        return "articulo_modificar_form.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id,@RequestParam(required = false) String nombre,
                            @RequestParam(required = false) String idFabrica, @RequestParam(required = false) MultipartFile archivo,
                            @RequestParam(required = false)String descripcion, RedirectAttributes redirectAttributes){

        try {
            articuloServicio.actualizar(id,nombre, idFabrica, archivo, descripcion);
            redirectAttributes.addFlashAttribute("exito", "El artículo fue modificado correctamente");

            return "redirect:/articulo/lista";
        } catch (MiExcepcion e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());

            return "redirect:/articulo/modificar/{id}";
        }
    }

}

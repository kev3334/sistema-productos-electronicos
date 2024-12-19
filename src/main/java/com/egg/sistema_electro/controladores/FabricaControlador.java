package com.egg.sistema_electro.controladores;

import com.egg.sistema_electro.entidades.Fabrica;
import com.egg.sistema_electro.excepciones.MiExcepcion;
import com.egg.sistema_electro.servicios.FabricaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/fabrica")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public class FabricaControlador {

    @Autowired
    private FabricaServicio fabricaServicio;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/registrar")
    public String registrar(){
        return "fabrica_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, RedirectAttributes redirectAttributes){

        try {
            fabricaServicio.crearFabrica(nombre);
            redirectAttributes.addFlashAttribute("exito","La fábrica fue registrada correctamente");
        } catch (MiExcepcion e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/fabrica/registrar";
        }
        return "redirect:/fabrica/registrar";
    }

    @GetMapping("/lista")
    public String listar(ModelMap model){
        List<Fabrica> fabricas = fabricaServicio.listarFabricas();
        model.addAttribute("fabricas", fabricas);
        return "fabrica_list.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap model){
        Fabrica fabrica = fabricaServicio.getOne(id);
        model.addAttribute("fabrica", fabrica);
        return "fabrica_modificar_form.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id,@RequestParam String nombre, RedirectAttributes redirectAttributes){
        try {
            fabricaServicio.actualizar(nombre, id);
            redirectAttributes.addFlashAttribute("exito", "La fábrica fue modificada correctamente");
            return "redirect:/fabrica/lista";

        } catch (MiExcepcion e) {

            redirectAttributes.addFlashAttribute("error", e.getMessage());

            return "redirect:/fabrica/modificar/{id}";
        }

    }


}

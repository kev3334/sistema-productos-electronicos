package com.egg.sistema_electro.controladores;

import com.egg.sistema_electro.excepciones.MiExcepcion;
import com.egg.sistema_electro.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String home(){
        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String hola(String nombre, HttpSession session){
        nombre="ElectroSystem";
        return "index.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model){
        if(error != null){
            model.addAttribute("error", "Usuario y/o Contraseña inválidos!");
        }
        return "login.html";
    }

    @GetMapping("/registrar")
    public String registrar(){

        return "registro_user.html";
    }

    @PostMapping("/registro")
    public String registrar(@RequestParam(required = false) String nombre, @RequestParam(required = false) String apellido, @RequestParam(required = false) String email,
                            @RequestParam(required = false) String password, @RequestParam(required = false) String password2,
                            MultipartFile archivo, ModelMap model){

        try {
            usuarioServicio.registrar(nombre, apellido, email, password,password2,archivo);
            model.addAttribute("exito", "usuario registrado exitosamente");
            return "login.html";
        } catch (MiExcepcion e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("nombre",nombre);
            model.addAttribute("apellido", apellido);
            model.addAttribute("email", email);
            return "registro_user.html";
        }

    }


}

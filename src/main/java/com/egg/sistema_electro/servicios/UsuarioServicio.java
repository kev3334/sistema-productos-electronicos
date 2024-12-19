package com.egg.sistema_electro.servicios;

import com.egg.sistema_electro.entidades.Imagen;
import com.egg.sistema_electro.entidades.Usuario;
import com.egg.sistema_electro.enumeraciones.Rol;
import com.egg.sistema_electro.excepciones.MiExcepcion;
import com.egg.sistema_electro.repositorios.UsuarioRepositorio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();//si tiene mas de un rol ?
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+usuario.getRol().toString());
            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        }else{
            return null;
        }
    }

    @Transactional
    public void registrar(String nombre, String apellido, String email,
                           String password, String password2, MultipartFile archivo) throws MiExcepcion{

        validar(nombre, apellido, email, password, password2);

        Imagen imagen = imagenServicio.guardar(archivo);

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword( new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USER);

        if (archivo != null && !archivo.isEmpty()) {
            if(imagen != null){
                usuario.setImagen(imagen);
            }
        }

        usuarioRepositorio.save(usuario);

    }




    public void validar(String nombre, String apellido, String email, String password, String password2) throws MiExcepcion {

        if (nombre == null || nombre.isEmpty() ) {
            throw new MiExcepcion("el nombre no puede ser nulo o estar vacío");
        }
        if (apellido == null || apellido.isEmpty() ) {
            throw new MiExcepcion("el apellido no puede ser nulo o estar vacío");
        }
        if (email == null || email.isEmpty() ) {
            throw new MiExcepcion("el email no puede ser nulo o estar vacío");
        }
        if (password == null || password.isEmpty()  || password.length() <= 5) {
            throw new MiExcepcion("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
        }
        if (!password.equals(password2)) {
            throw new MiExcepcion("Las contraseñas ingresadas deben ser iguales");
        }
    }



}

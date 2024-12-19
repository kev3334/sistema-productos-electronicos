package com.egg.sistema_electro.repositorios;

import com.egg.sistema_electro.entidades.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticuloRepositorio extends JpaRepository <Articulo , String> {
}

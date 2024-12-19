package com.egg.sistema_electro.repositorios;

import com.egg.sistema_electro.entidades.Fabrica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FabricaRepositorio extends JpaRepository<Fabrica, String> {
}

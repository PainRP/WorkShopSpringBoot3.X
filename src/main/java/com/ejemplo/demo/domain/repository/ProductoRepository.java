package com.ejemplo.demo.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ejemplo.demo.domain.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{

	List<Producto> findByNombreContainingIgnoreCase(String name);
	
	List<Producto> findByPrecioLessThan(int precio);
	
	List<Producto> findByNombreAndPrecioLessThan(String nombre, int precio);
}

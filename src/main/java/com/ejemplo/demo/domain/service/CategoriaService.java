package com.ejemplo.demo.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ejemplo.demo.api.dto.CategoriaRequest;
import com.ejemplo.demo.api.dto.CategoriaResponse;
import com.ejemplo.demo.domain.model.Categoria;
import com.ejemplo.demo.domain.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoriaService {

	private final CategoriaRepository categoriaRepository;

	// Inyección de dependencias por constructor (tu código lo aprueba)
	public CategoriaService(CategoriaRepository categoriaRepository) {
		this.categoriaRepository = categoriaRepository;
	}

	// 1. Listar todas las categorías
	@Transactional(readOnly = true)
	public List<CategoriaResponse> listarCategorias() {
		return categoriaRepository.findAll().stream()
				.map(this::mapearAResponse)
				.collect(Collectors.toList());
	}

	// 2. Obtener por ID
	@Transactional(readOnly = true)
	public CategoriaResponse obtenerCategoriaPorId(Long id) {
		Categoria categoria = categoriaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));
		return mapearAResponse(categoria);
	}

	// 3. Crear Categoría
	@Transactional
	public CategoriaResponse crearCategoria(CategoriaRequest request) {
		Categoria categoria = new Categoria();
		categoria.setNombre(request.getNombre());

		Categoria categoriaGuardada = categoriaRepository.save(categoria);
		return mapearAResponse(categoriaGuardada);
	}

	// 4. Actualizar Categoría
	@Transactional
	public CategoriaResponse actualizarCategoria(Long id, CategoriaRequest request) {
		Categoria categoria = categoriaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));

		categoria.setNombre(request.getNombre());

		Categoria categoriaActualizada = categoriaRepository.save(categoria);
		return mapearAResponse(categoriaActualizada);
	}

	// 5. Eliminar Categoría
	@Transactional
	public void eliminarCategoria(Long id) {
		if (!categoriaRepository.existsById(id)) {
			throw new EntityNotFoundException("Categoría no encontrada con ID: " + id);
		}
		categoriaRepository.deleteById(id);
	}

	// Método ayudante: Convierte la Entidad cruda en el Recibo elegante (Response)
	private CategoriaResponse mapearAResponse(Categoria categoria) {
		CategoriaResponse response = new CategoriaResponse();
		// Asegúrate de que el método en tu entidad se llame getId() o getID() y ajústalo aquí
		response.setId(categoria.getId()); 
		response.setNombre(categoria.getNombre());
		return response;
	}
}

package com.ejemplo.demo.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ejemplo.demo.api.dto.CategoriaRequest;
import com.ejemplo.demo.api.dto.CategoriaResponse;
import com.ejemplo.demo.domain.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorías", description = "API para gestionar categorías")
public class CategoriaController {

	private final CategoriaService categoriaService;

	public CategoriaController(CategoriaService categoriaService) {
		this.categoriaService = categoriaService;
	}

	@GetMapping
	@Operation(summary = "Listar todas las categorías")
	public ResponseEntity<List<CategoriaResponse>> listar() {
		return ResponseEntity.ok(categoriaService.listarCategorias());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtener una categoría por su ID")
	public ResponseEntity<CategoriaResponse> obtenerPorId(@PathVariable Long id) {
		return ResponseEntity.ok(categoriaService.obtenerCategoriaPorId(id));
	}

	@PostMapping
	@Operation(summary = "Crear una nueva categoría")
	public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest request) {
		CategoriaResponse response = categoriaService.crearCategoria(request);
		
		// Buena práctica: devolver 201 Created y la URI del nuevo recurso en el header Location
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(response.getId())
				.toUri();
				
		return ResponseEntity.created(location).body(response);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualizar una categoría existente")
	public ResponseEntity<CategoriaResponse> actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest request) {
		return ResponseEntity.ok(categoriaService.actualizarCategoria(id, request));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar una categoría por su ID")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		categoriaService.eliminarCategoria(id);
		return ResponseEntity.noContent().build(); // 204 No Content
	}
}
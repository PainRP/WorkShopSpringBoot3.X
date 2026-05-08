package com.ejemplo.demo.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ejemplo.demo.api.dto.ProductoRequest;
import com.ejemplo.demo.api.dto.ProductoResponse;
import com.ejemplo.demo.domain.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "API para gestionar productos")
public class ProductoController {

	private final ProductoService productoService;

	public ProductoController(ProductoService productoService) {
		this.productoService = productoService;
	}

	@GetMapping
	@Operation(summary = "Listar todos los productos")
	public ResponseEntity<List<ProductoResponse>> listar() {
		return ResponseEntity.ok(productoService.listarTodos());
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Obtener un producto por su ID")
	public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
		return ResponseEntity.ok(productoService.obtenerPorId(id));
	}

	@PostMapping
	@Operation(summary = "Crear un nuevo producto")
	public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
		ProductoResponse response = productoService.crearProducto(request);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(response.getId())
				.toUri();
				
		return ResponseEntity.created(location).body(response);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualizar un producto existente")
	public ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
		return ResponseEntity.ok(productoService.actualizar(id, request));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar un producto por su ID")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		productoService.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
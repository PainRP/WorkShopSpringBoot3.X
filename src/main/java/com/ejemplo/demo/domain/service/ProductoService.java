package com.ejemplo.demo.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ejemplo.demo.api.dto.ProductoRequest;
import com.ejemplo.demo.api.dto.ProductoResponse;
import com.ejemplo.demo.domain.model.Producto;
import com.ejemplo.demo.domain.repository.CategoriaRepository;
import com.ejemplo.demo.domain.repository.ProductoRepository;
import com.ejemplo.demo.domain.model.Categoria;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductoService{

	private final ProductoRepository productoRepository;
	private final CategoriaRepository categoriaRepository;
	public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
		this.productoRepository = productoRepository;
		this.categoriaRepository = categoriaRepository;
	}

	
	
	
	// CREATE: Crea un producto con validación de categoría [cite: 2, 27, 28]
		@Transactional
		public ProductoResponse crearProducto(ProductoRequest request) {
			Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
					.orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + request.getCategoriaId()));

			Producto producto = new Producto();
			producto.setNombre(request.getNombre());
			producto.setPrecio(request.getPrecio());
			producto.setCategoria(categoria);

			Producto productoGuardado = productoRepository.save(producto);
			return mapearAResponse(productoGuardado);
		}

		// READ: Obtiene un solo producto o lanza 404 [cite: 5, 21]
		@Transactional(readOnly = true)
		public ProductoResponse obtenerPorId(Long id) {
			Producto producto = productoRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
			return mapearAResponse(producto);
		}

		// READ: Lista todos los productos (eficiente con readOnly) [cite: 3, 21, 22]
		@Transactional(readOnly = true)
		public List<ProductoResponse> listarTodos() {
			return productoRepository.findAll().stream()
					.map(this::mapearAResponse)
					.collect(Collectors.toList());
		}

		// UPDATE: Actualiza datos y cambia categoría si es necesario [cite: 3, 52]
		@Transactional
		public ProductoResponse actualizar(Long id, ProductoRequest request) {
			Producto productoExistente = productoRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException("No se puede actualizar, producto no encontrado"));

			Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
					.orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));

			productoExistente.setNombre(request.getNombre());
			productoExistente.setPrecio(request.getPrecio());
			productoExistente.setCategoria(categoria);

			Producto productoActualizado = productoRepository.save(productoExistente);
			return mapearAResponse(productoActualizado);
		}

		// DELETE: Elimina físicamente el registro [cite: 3, 18]
		@Transactional
		public void eliminar(Long id) {
			if (!productoRepository.existsById(id)) {
				throw new EntityNotFoundException("No se puede eliminar, producto no encontrado");
			}
			productoRepository.deleteById(id);
		}

		// Método privado para no repetir código de conversión (mapeo) 
		private ProductoResponse mapearAResponse(Producto producto) {
		    ProductoResponse response = new ProductoResponse();
		    response.setId(producto.getId()); // Asegúrate de que coincida con la entidad
		    response.setNombre(producto.getNombre());
		    response.setPrecio(producto.getPrecio());
		    response.setCategoriaId(producto.getCategoria().getId());
		    response.setCategoriaNombre(producto.getCategoria().getNombre());
		    return response;
		}
	
	
}

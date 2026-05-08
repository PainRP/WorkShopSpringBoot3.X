package com.ejemplo.demo.api.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoriaRequest {
	@NotBlank(message = "El nombre de la categoría no puede estar vacío")
	private String nombre;
	
	private String descripcion;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}

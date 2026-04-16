package com.ejemplo.demo.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name= "producto")
public class Producto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	
	@Column(nullable = false , length = 100)
	private String nombre;
	
	@Column(nullable= false, length = 20)
	private Double precio;
	
	@ManyToOne(optional=false)
	@JoinColumn(name = "categoria_id", nullable = false)
	private Categoria categoria;

	@Column(name = "creado_en", updatable=false)
	private LocalDateTime creadoEn;
	
	public Long getID() {
		return id;
	}

	public void setID(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
}

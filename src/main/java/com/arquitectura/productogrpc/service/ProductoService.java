package com.arquitectura.productogrpc.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.arquitectura.productogrpc.entity.ProductoEntity;
import com.arquitectura.productogrpc.exception.ProductoNotFoundException;
import com.arquitectura.productogrpc.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<ProductoEntity> listarProductos() {
        return productoRepository.findAll();
    }

    public ProductoEntity obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() ->
                        new ProductoNotFoundException(
                                "Producto no encontrado con id: " + id
                        )
                );
    }

    public ProductoEntity crearProducto(ProductoEntity producto) {
        producto.setId(null);
        return productoRepository.save(producto);
    }

    public ProductoEntity actualizarProducto(Long id, ProductoEntity datosProducto) {

        ProductoEntity producto = obtenerProductoPorId(id);

        producto.setNombre(datosProducto.getNombre());
        producto.setDescripcion(datosProducto.getDescripcion());
        producto.setPrecio(datosProducto.getPrecio());

        return productoRepository.save(producto);
    }

    public ProductoEntity eliminarProducto(Long id) {

        ProductoEntity producto = obtenerProductoPorId(id);

        productoRepository.delete(producto);

        return producto;
    }
}
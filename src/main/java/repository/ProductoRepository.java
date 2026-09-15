package com.arquitectura.productogrpc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arquitectura.productogrpc.entity.ProductoEntity;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {
}
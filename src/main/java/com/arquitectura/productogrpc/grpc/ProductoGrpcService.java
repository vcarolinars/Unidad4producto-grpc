package com.arquitectura.productogrpc.grpc;
import java.util.List;
import org.springframework.grpc.server.service.GrpcService;
import com.arquitectura.productogrpc.entity.ProductoEntity;
import com.arquitectura.productogrpc.exception.ProductoNotFoundException;
import com.arquitectura.productogrpc.proto.Producto;
import com.arquitectura.productogrpc.proto.ProductoCrearRequest;
import com.arquitectura.productogrpc.proto.ProductoId;
import com.arquitectura.productogrpc.proto.ProductoServiceGrpc;
import com.arquitectura.productogrpc.proto.Productos;
import com.arquitectura.productogrpc.service.ProductoService;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

@GrpcService
public class ProductoGrpcService
        extends ProductoServiceGrpc.ProductoServiceImplBase {

    private final ProductoService productoService;

    public ProductoGrpcService(ProductoService productoService) {
        this.productoService = productoService;
    }

    // CREATE
    @Override
    public void create(
            ProductoCrearRequest request,
            StreamObserver<Producto> responseObserver) {

        try {

            if (request.getNombre().isBlank()) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("El nombre del producto es obligatorio")
                                .asRuntimeException()
                );
                return;
            }

            if (request.getPrecio() < 0) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("El precio no puede ser negativo")
                                .asRuntimeException()
                );
                return;
            }

            ProductoEntity entity = new ProductoEntity();

            entity.setNombre(request.getNombre());
            entity.setDescripcion(request.getDescripcion());
            entity.setPrecio(request.getPrecio());

            ProductoEntity creado =
                    productoService.crearProducto(entity);

            responseObserver.onNext(toProto(creado));
            responseObserver.onCompleted();

        } catch (Exception ex) {

            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Error al crear el producto")
                            .withCause(ex)
                            .asRuntimeException()
            );
        }
    }

    // GET
    @Override
    public void get(
            ProductoId request,
            StreamObserver<Producto> responseObserver) {

        try {

            ProductoEntity producto =
                    productoService.obtenerProductoPorId(request.getId());

            responseObserver.onNext(toProto(producto));
            responseObserver.onCompleted();

        } catch (ProductoNotFoundException ex) {

            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(ex.getMessage())
                            .asRuntimeException()
            );

        } catch (Exception ex) {

            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Error al consultar el producto")
                            .withCause(ex)
                            .asRuntimeException()
            );
        }
    }

    // UPDATE
    @Override
    public void update(
            Producto request,
            StreamObserver<Producto> responseObserver) {

        try {

            if (request.getNombre().isBlank()) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("El nombre del producto es obligatorio")
                                .asRuntimeException()
                );
                return;
            }

            ProductoEntity datos = new ProductoEntity();

            datos.setNombre(request.getNombre());
            datos.setDescripcion(request.getDescripcion());
            datos.setPrecio(request.getPrecio());

            ProductoEntity actualizado =
                    productoService.actualizarProducto(
                            request.getId(),
                            datos
                    );

            responseObserver.onNext(toProto(actualizado));
            responseObserver.onCompleted();

        } catch (ProductoNotFoundException ex) {

            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(ex.getMessage())
                            .asRuntimeException()
            );

        } catch (Exception ex) {

            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Error al actualizar el producto")
                            .withCause(ex)
                            .asRuntimeException()
            );
        }
    }

    // DELETE
    @Override
    public void delete(
            ProductoId request,
            StreamObserver<Producto> responseObserver) {

        try {

            ProductoEntity eliminado =
                    productoService.eliminarProducto(request.getId());

            responseObserver.onNext(toProto(eliminado));
            responseObserver.onCompleted();

        } catch (ProductoNotFoundException ex) {

            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(ex.getMessage())
                            .asRuntimeException()
            );

        } catch (Exception ex) {

            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Error al eliminar el producto")
                            .withCause(ex)
                            .asRuntimeException()
            );
        }
    }

    // LIST
    @Override
    public void list(
            Empty request,
            StreamObserver<Productos> responseObserver) {

        try {

            List<Producto> productos =
                    productoService.listarProductos()
                            .stream()
                            .map(this::toProto)
                            .toList();

            Productos response =
                    Productos.newBuilder()
                            .addAllItems(productos)
                            .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception ex) {

            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Error al listar los productos")
                            .withCause(ex)
                            .asRuntimeException()
            );
        }
    }

    // CONVERSIÓN ENTITY -> PROTOBUF
    private Producto toProto(ProductoEntity entity) {

        return Producto.newBuilder()
                .setId(entity.getId())
                .setNombre(entity.getNombre())
                .setDescripcion(
                        entity.getDescripcion() != null
                                ? entity.getDescripcion()
                                : ""
                )
                .setPrecio(entity.getPrecio())
                .build();
    }
}
package co.com.franquicias.api.mapper;

import co.com.franquicias.api.dto.request.ProductoPeticion;
import co.com.franquicias.api.dto.response.ProductoMayorStockPorSucursalRespuesta;
import co.com.franquicias.api.dto.response.ProductoRespuesta;
import co.com.franquicias.model.producto.Producto;
import co.com.franquicias.model.sucursal.Sucursal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sucursalId", ignore = true)
    Producto aDominio(ProductoPeticion peticion);

    ProductoRespuesta aRespuesta(Producto producto);

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.nombre", target = "productoNombre")
    @Mapping(source = "producto.stock", target = "stock")
    @Mapping(source = "sucursal.id", target = "sucursalId")
    @Mapping(source = "sucursal.nombre", target = "sucursalNombre")
    ProductoMayorStockPorSucursalRespuesta aRespuesta(Producto producto, Sucursal sucursal);
}

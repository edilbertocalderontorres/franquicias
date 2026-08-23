package co.com.franquicias.model.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDominio {

    FRANQUICIA_NO_ENCONTRADA("FRANQUICIA_NO_ENCONTRADA", "La franquicia solicitada no existe"),
    FRANQUICIA_YA_EXISTE("FRANQUICIA_YA_EXISTE", "Ya existe una franquicia con ese tipo y número de documento"),
    SUCURSAL_NO_ENCONTRADA("SUCURSAL_NO_ENCONTRADA", "La sucursal solicitada no existe"),
    SUCURSAL_YA_EXISTE("SUCURSAL_YA_EXISTE", "Ya existe una sucursal con ese código en la franquicia"),
    PRODUCTO_NO_ENCONTRADO("PRODUCTO_NO_ENCONTRADO", "El producto solicitado no existe"),
    PRODUCTO_YA_EXISTE("PRODUCTO_YA_EXISTE", "Ya existe un producto con ese código en la sucursal"),
    STOCK_INSUFICIENTE("STOCK_INSUFICIENTE", "El stock resultante no puede ser negativo");

    private final String codigo;
    private final String mensaje;
}

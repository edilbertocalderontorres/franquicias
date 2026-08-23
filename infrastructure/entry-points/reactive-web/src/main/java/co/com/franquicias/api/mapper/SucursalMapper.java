package co.com.franquicias.api.mapper;

import co.com.franquicias.api.dto.request.SucursalPeticion;
import co.com.franquicias.api.dto.response.SucursalRespuesta;
import co.com.franquicias.model.sucursal.Sucursal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SucursalMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "franquiciaId", ignore = true)
    Sucursal aDominio(SucursalPeticion peticion);

    SucursalRespuesta aRespuesta(Sucursal sucursal);
}

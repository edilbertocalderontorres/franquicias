package co.com.franquicias.api.mapper;

import co.com.franquicias.api.dto.request.FranquiciaPeticion;
import co.com.franquicias.api.dto.response.FranquiciaRespuesta;
import co.com.franquicias.model.franquicia.Franquicia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FranquiciaMapper {

    @Mapping(target = "id", ignore = true)
    Franquicia aDominio(FranquiciaPeticion peticion);

    FranquiciaRespuesta aRespuesta(Franquicia franquicia);
}

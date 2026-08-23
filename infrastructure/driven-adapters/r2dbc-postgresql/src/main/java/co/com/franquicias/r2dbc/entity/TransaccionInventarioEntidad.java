package co.com.franquicias.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("transaccion_inventario")
public class TransaccionInventarioEntidad {

    @Id
    private Long id;

    private String idkey;

    @Column("producto_id")
    private Long productoId;

    private Integer delta;

    private String usuario;

    @Column("fecha_creacion")
    private Instant fechaCreacion;
}

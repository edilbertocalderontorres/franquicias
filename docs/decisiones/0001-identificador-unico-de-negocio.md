# Identificador único de negocio (franquicia y sucursal)

nombre es texto libre para mostrar, no para identificar. Un espacio o una mayúscula de más genera duplicados que en la práctica son la misma franquicia o sucursal. Por eso deja de ser UNIQUE en ningún nivel.

**Franquicia:** tipoDocumento + numeroDocumento (NIT, CC o CE). Es una entidad legal real, se identifica por su documento.

**Sucursal:** codigo, único por franquicia. No es una entidad legal independiente (no tiene documento propio), así que uso un código interno de negocio en vez de un identificador legal externo. Descarté la matrícula mercantil: depende de un registro externo que no voy a integrar en esta prueba, y una sucursal nueva podría no tenerlo aún al crearse.

En ambos casos el atributo no se reutiliza: una vez asignado, queda permanente.

Impacto: el DDL agrega tipo_documento/numero_documento a franquicia y codigo a sucursal, cada uno con su propio UNIQUE. Los gateways solo exponen los métodos que el caso de uso correspondiente usa de verdad (ej. buscarPorId aparece cuando hace falta validar el padre, no antes).

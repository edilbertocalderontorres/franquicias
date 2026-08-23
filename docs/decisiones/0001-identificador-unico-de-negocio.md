# Identificador único de negocio (franquicia, sucursal y producto)

nombre es texto libre para mostrar, no para identificar. Un espacio o una mayúscula de más genera duplicados que en la práctica son la misma entidad. Por eso deja de ser UNIQUE en ningún nivel.

**Franquicia:** tipoDocumento + numeroDocumento (NIT, CC o CE). Es una entidad legal real, se identifica por su documento.

**Sucursal:** codigo, único por franquicia. No es una entidad legal independiente (no tiene documento propio), así que uso un código interno de negocio en vez de un identificador legal externo. Descarté la matrícula mercantil: depende de un registro externo que no voy a integrar en esta prueba, y una sucursal nueva podría no tenerlo aún al crearse.

**Producto:** mismo razonamiento que sucursal. codigo, único por sucursal — tampoco existe un identificador legal a nivel de producto, así que es otro código interno (piénsalo como SKU).

En los tres casos el atributo no se reutiliza: una vez asignado, queda permanente.

Impacto: el DDL agrega tipo_documento/numero_documento a franquicia, codigo a sucursal y codigo a producto, cada uno con su propio UNIQUE. Los gateways solo exponen los métodos que el caso de uso correspondiente usa de verdad (ej. buscarPorId aparece cuando hace falta validar el padre, no antes).

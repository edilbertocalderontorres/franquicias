# Borrado lógico de producto

Eliminar un producto no borra la fila: solo pone activo=false. Borrado físico pierde trazabilidad. El código no se libera (mismo criterio que en la 0001). buscarPorId y cualquier lectura de negocio filtran activo=true, así que un producto borrado se ve como "no encontrado" para el resto del sistema.

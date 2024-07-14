# Literalura - Gestión de Libros y Autores

Literalura es un proyecto desarrollado como parte de un challenge de Alura Latam. Esta aplicación Java está diseñada para consumir una API de libros, procesar la información y almacenarla en una base de datos. La aplicación permite buscar libros por título, listar libros y autores registrados, filtrar autores vivos por año, y listar libros por idioma.

## Características

- **Buscar libros por título:** Realiza una búsqueda en la API de libros y muestra los detalles del primer libro encontrado.
- **Listar libros registrados:** Muestra todos los libros almacenados en la base de datos.
- **Listar autores registrados:** Muestra todos los autores almacenados en la base de datos.
- **Listar autores vivos por año:** Filtra y muestra autores vivos en un año específico.
- **Listar libros por idioma:** Filtra y muestra libros almacenados en la base de datos por su idioma.

## Estructura del Proyecto

El proyecto está organizado en los siguientes paquetes:

- `com.alura.literalura.models`: Contiene las clases de modelo como `Autor` y `Libro`.
- `com.alura.literalura.repository`: Contiene la interfaz `AutorRepository` para interactuar con la base de datos.
- `com.alura.literalura.service`: Contiene las clases `ConsumoAPI` y `Conversor` para consumir la API y convertir datos JSON.
- `com.alura.literalura.principal`: Contiene la clase principal `Principal` que gestiona la lógica de la aplicación.

## Menú de Opciones

La aplicación presenta un menú interactivo donde el usuario puede seleccionar diferentes opciones para gestionar libros y autores:
- **Elija la opción a través de su número:**
- **1 - Buscar libros por título**
- **2 - Listar libros registrados**
- **3 - Listar autores registrados**
- **4 - Listar autores vivos en un determinado año**
- **5 - Listar libros por idioma**
- **0 - Salir**


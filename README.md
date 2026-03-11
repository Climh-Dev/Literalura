#  Literalura - Challenge Alura Geek

¡Bienvenido a **Literalura**! Este es un sistema de catálogo de libros interactivo desarrollado en **Java 17** con **Spring Boot**. La aplicación consume datos reales de la API [Gutendex](https://gutendex.com/) para buscar libros, procesar información de autores y persistir todo en una base de datos relacional.



##  Tecnologías Utilizadas

* **Java 17**: Lenguaje principal de desarrollo.
* **Spring Boot 3.x**: Framework para la gestión de dependencias y persistencia.
* **Spring Data JPA**: Para el mapeo objeto-relacional (ORM) y consultas a la DB.
* **PostgreSQL**: Base de datos relacional para el almacenamiento persistente.
* **Jackson**: Biblioteca para el parseo de JSON proveniente de la API.
* **Maven**: Gestor de dependencias.

##  Funcionalidades

El sistema ofrece un menú interactivo por consola con las siguientes capacidades:

1.  **Buscar libro por título**: Consulta la API de Gutendex, muestra los detalles del libro y lo guarda automáticamente en la base de datos (evitando duplicados).
2.  **Listar libros registrados**: Muestra todos los libros almacenados localmente en PostgreSQL.
3.  **Listar autores registrados**: Lista los autores capturados, incluyendo sus años de nacimiento y fallecimiento.
4.  **Listar autores vivos en un determinado año**: Filtro inteligente para consultar qué autores de nuestra base de datos estaban activos en el año que elijas.
5.  **Listar libros por idioma**: Filtra tu biblioteca personal por códigos de idioma (es, en, fr, pt).

##  Demostración

### Menú Principal
*(Inserta aquí tu captura de la consola con el menú)*
![Menú Principal](![Captura de pantalla 2026-03-10 182434](https://github.com/user-attachments/assets/defa1443-0354-40b0-95aa-2f5eab5540fa)
)

### Búsqueda y Registro
*(Inserta aquí la captura de cuando guardas un libro exitosamente)*
![Registro de Libro](![Captura de pantalla 2026-03-10 183206](https://github.com/user-attachments/assets/36de5901-e55e-4945-9ed0-ef045ec160bc)
)



## 📋 Configuración del Proyecto

Para correr este proyecto localmente, asegúrate de configurar tu archivo `src/main/resources/application.properties` con tus credenciales de PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update

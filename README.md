# 📋 Aplicación social de recetas de cocina

## 📖 Descripción
Este proyecto es el resultado del aprendizaje adquirido en el Ciclo Superior de Desarrollo de Aplicaciones Web. Combina varias tecnologías y herramientas avanzadas para ofrecer una aplicación moderna y funcional que permite a los usuarios descubrir, compartir y valorar recetas de cocina.
Se trata pues de un proyecto educativo para demostrar competencias aprendidas, por lo que no se emplea framework o motor de plantillas en el frontend, ya que quedan fuera del ámbito de los estudios realizados.

La aplicación consta de:
- **Backend:** API REST desarrollada con **Spring Boot**.
- **Frontend:** Interfaz creada con **HTML5**, **CSS3** y **JavaScript**.
- **Monitoreo:** Servicios desplegados con **Docker**, integrados con **Grafana** y **Loki** para visualizar métricas como:
  - Usuarios registrados.
  - Recetas subidas.
  - Recetas más visitadas.
  - Información de logs detallados.

## 🎯 Características principales
- Registro y autenticación de usuarios.
- Publicación y gestión de recetas.
- Interacción social: Los usuarios pueden dar "likes" a recetas.
- Sistema de búsqueda por nombre de receta y ordenación por número de likes y fecha
- Diseño responsivo.
- Monitorización y análisis de datos en tiempo real a través de **Grafana**.

## 🛠️ Tecnologías utilizadas
### Backend
- **Spring Boot**: Framework para construir el backend REST.
- **MySQL**: Base de datos relacional para gestionar usuarios, recetas y datos relacionados.
- **Spring Security**: Gestión de autenticación y autorización.
- **JPA/Hibernate**: Acceso y manejo de datos en la base de datos.

### Frontend
- **HTML5**: Estructura semántica de la aplicación.
- **CSS3**: Estilización responsiva y moderna.
- **JavaScript**: Validación en frontend y comunicación con el backend.

### Monitorización y despliegue
- **Docker**: Contenedores para facilitar el despliegue de servicios.
- **Grafana y Loki**: Monitorización de usuarios, recetas y visualización de logs.
- **Promtail**: Recopilación de métricas.

## 🚀 Configuración del proyecto

### Prerrequisitos
1. **Java 17** o superior.
3. **Docker** y **Docker Compose** instalados.

### Instrucciones para desplegar
#### Clonar el repositorio
```bash
git clone https://github.com/EloSdev/recipes-daw-project
cd tu-repo
Ejecutar la aplicación (en Visual Studio Code se recomienda tener instalado la extensión "Extension Pack for Java" )




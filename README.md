# Nombre de proyecto: SRAE - Sistema de Reservas y Gestión de Eventos.

## Descripción:
**Sistema de Reservación y Administración de Eventos (SRAE)** 
es una plataforma web desarrollada en Java bajo la arquitectura 

**Modelo-Vista-Controlador (MVC)** e implementando el patrón **DAO (Data Access Object)** para interactuar con bases de datos relacionales en Oracle Cloud.

El sistema resuelve la complejidad logística de eventos masivos:
* Permite a los **Administradores** la gestión global de usuarios, estados, roles, categorías y espacios/recintos.
* Otorga a los **Organizadores** el control para crear, editar (`editarEvento.jsp`), guardar borradores, publicar eventos y subir imágenes promocionales.
* Brinda a los **Asistentes / Clientes** un panel dinámico para explorar el catálogo de eventos (`eventos.jsp`), ver su ficha técnica detallada (`detalleEvento.jsp`), generar reservaciones con un código de confirmación único (`SRAE-XXXXXX`), gestionar su perfil con foto personalizada (`crearPerfil.jsp`) y administrar su historial personal de boletos (`misReservas.jsp`).


## Integrantes del Equipo

 Nombre Completo | Matrícula | Usuario de GitHub |

 **Armenta Galindo Axel Ernesto** | `20253ds115` | [@axel-ds](https://github.com/axel-ds) |
 **Castañeda Mendoza Emely Darian** | `20253ds093` | [@20253ds093-collab](https://github.com/20253ds093-collab) |
 **Escamilla Carrillo Silvana Lizeth** | `20253DS102` | [@Silvana-es](https://github.com/Silvana-es) |
 **Miranda Diaz Vanessa Alejandra** | `20253ds117` | [@Vanessa-Mira](https://github.com/Vanessa-Mira) |
 **Omaña Silva Alondra Yolotzin** | `20253ds154` | [@yolotzin-12](https://github.com/yolotzin-12) |
 **Pardo Diaz Alexa** | `20253ds109` | [@alexyafk](https://github.com/alexyafk) |

##  Funcionalidades Principales

* **Autenticación y Seguridad por Roles (RBAC):** Control global de peticiones HTTP mediante `FiltroAutenticacion`. Roles diferenciados (Admin, Organizador, Cliente).
* **Gestión de Roles:** Modificación de roles con restricción por regla de negocio (evita degradar la cuenta si el usuario tiene reservas activas).
* **Seguridad:** Restablecimiento seguro de credenciales con hashing **SHA-256** y **Tokens UUID** temporales almacenados en Oracle DB.
* **Sesiones:** Cabeceras anti-caché (`Cache-Control: no-cache, no-store`) en servlets para evitar accesos indebidos tras el cierre de sesión.

##  Tecnologías Utilizadas

* **Lenguaje & Core:** Java 17 / Jakarta EE 10 (Servlets `@WebServlet`, Filters `@WebFilter`, Multipart `@MultipartConfig`).
* **Base de Datos:** Oracle Cloud Autonomous Database (JDBC Thin Driver con Wallet SSL).
* **Seguridad:** Encriptación SHA-256, Tokens UUID temporales y validación con Expresiones Regulares.
* **Vistas:** JSP (JavaServer Pages), JSTL (`jakarta.tags.core`, `fn:functions`), Bootstrap 5, Bootstrap Icons, SweetAlert2.
* **Manejo de JSON & AJAX:** Google Gson (`JsonObject`, `JsonParser`) e integración vía Fetch API.
* **Protocolo de Notificaciones:** JavaMail / SMTP Client (TLS 1.2/1.3).

 **Diseño UI/UX** | **Figma** | Maquetación y prototipado interactivo móvil y desktop |
 **Entorno de Desarrollo** | **IntelliJ IDEA** | IDE principal para la programación del sistema |
 **Base de Datos Cloud** | **Oracle Cloud** | Servidor de base de datos relacional en la nube |
 **Gestión de Datos** | **DataGrip** | Administración y modelado DDL/DML de la base de datos |
 **Control de Versiones** | **GitHub** | Control de repositorio y trabajo colaborativo |
 **Despliegue y Entorno** | **Docker Desktop** | Contenedores virtuales para homologar el entorno de ejecución |

##  Modelo de Base de Datos

La base de datos relacional se encuentra desplegada en **Oracle Cloud** y normalizada en **Tercera Forma Normal (3FN)**. Contempla **9 entidades principales**:

1. **`Usuario`**: Entidad padre que almacena credenciales e identidad.
2. **`Rol`**: Catálogo de permisos (`Administrador`, `Organizador`, `Asistente`).
3. **`Contrasena`**: Almacenamiento independiente de claves encriptadas.
4. **`Token_Recuperacion`**: Gestión de tokens de caducidad para restablecimiento de acceso.
5. **`Administrador` / `Organizador` / `Asistente`**: Especialización de usuarios (subtipos 1:1).
6. **`Categoria`**: Clasificación temática de los eventos.
7. **`Espacio`**: Registro de la infraestructura física (auditorios, salas) y sus límites de capacidad.
8. **`Evento`**: Entidad central que coordina al organizador, categoría, espacio y disponibilidad de aforo.
9. **`Reserva`**: Gestión de boletos con códigos de confirmación únicos y estado del pase.


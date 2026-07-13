
```
minimarket
├─ .mvn
│  └─ wrapper
│     └─ maven-wrapper.properties
├─ mvnw
├─ mvnw.cmd
├─ pom.xml
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ com
   │  │     └─ minimarket
   │  │        ├─ controller
   │  │        │  ├─ CarritoController.java
   │  │        │  ├─ CategoriaController.java
   │  │        │  ├─ DetalleVentaController.java
   │  │        │  ├─ HolaMundoController.java
   │  │        │  ├─ InventarioController.java
   │  │        │  ├─ ProductoController.java
   │  │        │  ├─ UsuarioController.java
   │  │        │  └─ VentaController.java
   │  │        ├─ entity
   │  │        │  ├─ Carrito.java
   │  │        │  ├─ Categoria.java
   │  │        │  ├─ DetalleVenta.java
   │  │        │  ├─ Inventario.java
   │  │        │  ├─ Producto.java
   │  │        │  ├─ Rol.java
   │  │        │  ├─ Usuario.java
   │  │        │  └─ Venta.java
   │  │        ├─ MinimarketApplication.java
   │  │        ├─ repository
   │  │        │  ├─ CarritoRepository.java
   │  │        │  ├─ CategoriaRepository.java
   │  │        │  ├─ DetalleVentaRepository.java
   │  │        │  ├─ InventarioRepository.java
   │  │        │  ├─ ProductoRepository.java
   │  │        │  ├─ RolRepository.java
   │  │        │  ├─ UsuarioRepository.java
   │  │        │  └─ VentaRepository.java
   │  │        ├─ security
   │  │        │  ├─ config
   │  │        │  │  └─ SecurityConfig.java
   │  │        │  ├─ model
   │  │        │  │  ├─ CustomUserDetails.java
   │  │        │  │  └─ LoginRequest.java
   │  │        │  ├─ service
   │  │        │  │  └─ CustomUserDetailsService.java
   │  │        │  └─ util
   │  │        │     └─ JwtUtil.java
   │  │        └─ service
   │  │           ├─ CarritoService.java
   │  │           ├─ CategoriaService.java
   │  │           ├─ DetalleVentaService.java
   │  │           ├─ impl
   │  │           │  ├─ CarritoServiceImpl.java
   │  │           │  ├─ CategoriaServiceImpl.java
   │  │           │  ├─ DetalleVentaServiceImpl.java
   │  │           │  ├─ InventarioServiceImpl.java
   │  │           │  ├─ ProductoServiceImpl.java
   │  │           │  ├─ RolServiceImpl.java
   │  │           │  ├─ UsuarioServiceImpl.java
   │  │           │  └─ VentaServiceImpl.java
   │  │           ├─ InventarioService.java
   │  │           ├─ ProductoService.java
   │  │           ├─ RolService.java
   │  │           ├─ UsuarioService.java
   │  │           └─ VentaService.java
   │  └─ resources
   │     ├─ application.properties
   │     ├─ static
   │     └─ templates
   └─ test
      └─ java
         └─ com
            └─ minimarket
               ├─ MinimarketApplicationTests.java
               └─ UsuarioTest.java

```
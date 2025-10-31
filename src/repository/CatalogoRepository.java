package repository;

import model.*;
import java.util.*;

/**
 * Repositorio de datos del sistema (catálogos de áreas y usuarios)
 */
public class CatalogoRepository {
    private final Map<String, Area> areas;
    private final Map<String, Usuario> usuarios;

    public CatalogoRepository() {
        this.areas = new HashMap<>();
        this.usuarios = new HashMap<>();
    }

    // ===== Gestión de Usuarios =====

    public void agregarUsuario(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    public Usuario obtenerUsuario(String id) {
        return usuarios.get(id);
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return new ArrayList<>(usuarios.values());
    }

    public List<Usuario> obtenerUsuariosPorRol(Rol rol) {
        return usuarios.values().stream()
                .filter(u -> u.getRol() == rol)
                .toList();
    }

    // ===== Gestión de Áreas =====

    public void agregarArea(Area area) {
        areas.put(area.getId(), area);
    }

    public Area obtenerArea(String id) {
        return areas.get(id);
    }

    public List<Area> obtenerTodasLasAreas() {
        return new ArrayList<>(areas.values());
    }

    // ===== Inicialización de datos de demo =====

    public void inicializarDatosDemo() {
        // Crear usuarios
        Usuario despachador = new Usuario("U001", "Carlos Martínez", Rol.DESPACHADOR_OPERADOR);
        Usuario agente1 = new Usuario("U002", "Ana López", Rol.AGENTE_AREA);
        Usuario agente2 = new Usuario("U003", "Pedro Gómez", Rol.AGENTE_AREA);
        Usuario agente3 = new Usuario("U004", "María Rodríguez", Rol.AGENTE_AREA);
        Usuario agente4 = new Usuario("U005", "Luis Fernández", Rol.AGENTE_AREA);
        Usuario admin = new Usuario("U006", "Laura Admin", Rol.ADMINISTRADOR);

        agregarUsuario(despachador);
        agregarUsuario(agente1);
        agregarUsuario(agente2);
        agregarUsuario(agente3);
        agregarUsuario(agente4);
        agregarUsuario(admin);

        // Crear áreas
        Area areaTecnica = new Area("A001", "Área Técnica");
        Area areaComercial = new Area("A002", "Área Comercial");
        Area areaSoporte = new Area("A003", "Área Soporte");
        Area areaCalidad = new Area("A004", "Área Calidad");

        // Asignar agentes a áreas
        areaTecnica.agregarAgente(agente1);
        areaTecnica.agregarAgente(agente2);
        areaComercial.agregarAgente(agente3);
        areaSoporte.agregarAgente(agente4);
        areaCalidad.agregarAgente(agente1);

        agregarArea(areaTecnica);
        agregarArea(areaComercial);
        agregarArea(areaSoporte);
        agregarArea(areaCalidad);

        System.out.println("✅ Catálogo inicializado con " + usuarios.size() +
                         " usuarios y " + areas.size() + " áreas");
    }
}


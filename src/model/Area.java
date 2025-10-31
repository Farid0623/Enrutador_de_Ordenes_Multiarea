package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa un área de trabajo del sistema
 */
public class Area {
    private final String id;
    private final String nombre;
    private final List<Usuario> agentes;

    public Area(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.agentes = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Usuario> getAgentes() {
        return new ArrayList<>(agentes);
    }

    public void agregarAgente(Usuario usuario) {
        if (usuario.getRol() == Rol.AGENTE_AREA && !agentes.contains(usuario)) {
            agentes.add(usuario);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Area area = (Area) o;
        return Objects.equals(id, area.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return nombre;
    }
}


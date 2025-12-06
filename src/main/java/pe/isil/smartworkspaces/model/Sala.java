package pe.isil.smartworkspaces.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sala")
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sale")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "capacidad")
    private int capacidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado;

    public Sala() {
    }

    public Sala(Integer id, String nombre, int capacidad, Estado estado) {
        this.id = id;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombres() {
        return nombre;
    }

    public void setNombres(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}

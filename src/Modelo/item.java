package Modelo;

public class item {

    private int id;
    private String nombre;

    public item(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }

    @Override
    public String toString() { return nombre; }
}
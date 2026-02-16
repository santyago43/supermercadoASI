/**
 * ❌ PROBLEMA: Esta clase tiene conocimiento del impuesto,
 * pero no lo centraliza — cada quien lo recalcula por su cuenta.
 */
public class Producto {

    private String nombre;
    private double precio;
    private String categoria; // "ALIMENTO", "BEBIDA", "LIMPIEZA", "ELECTRONICO"

    public Producto(String nombre, double precio, String categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }
}

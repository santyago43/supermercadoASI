/**
 * ✅ DRY APLICADO: El conocimiento del impuesto vive en Producto.
 *
 * ¿Por qué aquí y no en una clase CalculadoraImpuestos?
 * Porque la tasa depende de la CATEGORÍA del producto.
 * Producto es quien conoce su categoría → Producto calcula su tasa.
 *
 * Esto elimina la duplicación SIN crear acoplamiento artificial.
 * Nadie necesita depender de una clase utilitaria externa.
 *
 * ✅ LOW COUPLING: Ninguna otra clase necesita conocer las tasas.
 * Si cambia la tasa de BEBIDA, se cambia en UN solo lugar.
 */
public class Producto {

    private String nombre;
    private double precio;
    private Categoria categoria;

    // ✅ Usamos enum en lugar de String → el compilador previene errores
    public enum Categoria {
        ALIMENTO, BEBIDA, LIMPIEZA, ELECTRONICO
    }

    public Producto(String nombre, double precio, Categoria categoria) {
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

    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * ✅ SINGLE SOURCE OF TRUTH para la lógica de impuestos.
     * Toda la lógica de qué tasa aplica según categoría vive aquí.
     * Factura, CarritoCompras y ReporteVentas simplemente llaman este método.
     */
    public double getTasaImpuesto() {
        switch (categoria) {
            case ALIMENTO:
                return 0.05;
            case BEBIDA:
                return 0.08;
            case LIMPIEZA:
                return 0.19;
            case ELECTRONICO:
                return 0.19;
            default:
                return 0.19;
        }
    }

    /**
     * ✅ Producto calcula su propio impuesto por unidad.
     * No hay Feature Envy: nadie tiene que meterse en los datos de Producto.
     */
    public double calcularImpuesto(int cantidad) {
        return precio * cantidad * getTasaImpuesto();
    }

    public double calcularSubtotal(int cantidad) {
        return precio * cantidad;
    }
}

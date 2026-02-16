/**
 * ✅ Objeto valor que agrupa un producto con su cantidad.
 * Elimina la necesidad de mantener dos listas paralelas en sincronía.
 * Reduce el acoplamiento interno de Factura y CarritoCompras.
 */
public class LineaFactura {

    private final Producto producto;
    private final int cantidad;

    public LineaFactura(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    // ✅ Delega en Producto → no hay Feature Envy
    public double getSubtotal() {
        return producto.calcularSubtotal(cantidad);
    }

    public double getImpuesto() {
        return producto.calcularImpuesto(cantidad);
    }
}

import java.util.ArrayList;
import java.util.List;

/**
 * ❌ SMELL: Duplicate Code
 * La lógica de descuento aquí es IDÉNTICA a la de Factura.
 * Son el mismo conocimiento de negocio: "cuánto descuento aplica".
 *
 * Si el negocio decide cambiar los umbrales de descuento ($100k → $80k),
 * hay que buscarlo y cambiarlo en DOS clases distintas.
 * Y si hay una tercera clase que también lo calcula... en TRES.
 */
public class CarritoCompras {

    private List<Producto> productos;
    private List<Integer> cantidades;

    public CarritoCompras() {
        this.productos = new ArrayList<>();
        this.cantidades = new ArrayList<>();
    }

    public void agregar(Producto producto, int cantidad) {
        productos.add(producto);
        cantidades.add(cantidad);
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (int i = 0; i < productos.size(); i++) {
            subtotal += productos.get(i).getPrecio() * cantidades.get(i);
        }
        return subtotal;
    }

    // ❌ DUPLICATE CODE: Exactamente la misma lógica que Factura.calcularDescuento()
    // Mismo conocimiento de negocio, dos representaciones
    public double calcularDescuentoPreview() {
        double subtotal = calcularSubtotal();
        if (subtotal > 100000) {
            return subtotal * 0.10;
        }
        if (subtotal > 50000) {
            return subtotal * 0.05;
        }
        return 0;
    }

    // ❌ DUPLICATE CODE: Mismas tasas de impuesto que en Factura y ReporteVentas
    public double estimarImpuestos() {
        double totalImpuesto = 0;
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            double precioLinea = p.getPrecio() * cantidades.get(i);
            double tasa;

            if (p.getCategoria().equals("ALIMENTO")) {
                tasa = 0.05;
            } else if (p.getCategoria().equals("BEBIDA")) {
                tasa = 0.08;
            } else if (p.getCategoria().equals("LIMPIEZA")) {
                tasa = 0.19;
            } else {
                tasa = 0.19;
            }

            totalImpuesto += precioLinea * tasa;
        }
        return totalImpuesto;
    }

    public double calcularTotalEstimado() {
        return calcularSubtotal() + estimarImpuestos() - calcularDescuentoPreview();
    }

    public void mostrarResumen() {
        System.out.println("--- Resumen del Carrito ---");
        System.out.printf("Subtotal:  $%.2f%n", calcularSubtotal());
        System.out.printf("Impuestos: $%.2f%n", estimarImpuestos());
        System.out.printf("Descuento: $%.2f%n", calcularDescuentoPreview());
        System.out.printf("Total est: $%.2f%n", calcularTotalEstimado());
    }
}

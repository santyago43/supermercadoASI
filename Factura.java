import java.util.ArrayList;
import java.util.List;

/**
 * ❌ SMELL: Feature Envy
 * Factura sabe demasiado sobre Producto para calcular cosas
 * que debería calcular el propio Producto.
 *
 * ❌ SMELL: Duplicate Code
 * La lógica de impuestos está DUPLICADA aquí y en ReporteVentas.
 * Si cambia la tasa del IVA hay que buscarlo en 3 lugares.
 */
public class Factura {

    private String numeroFactura;
    private String nombreCliente;
    private List<Producto> productos;
    private List<Integer> cantidades;

    public Factura(String numeroFactura, String nombreCliente) {
        this.numeroFactura = numeroFactura;
        this.nombreCliente = nombreCliente;
        this.productos = new ArrayList<>();
        this.cantidades = new ArrayList<>();
    }

    public void agregarProducto(Producto producto, int cantidad) {
        productos.add(producto);
        cantidades.add(cantidad);
    }

    // ❌ DUPLICATE CODE: Esta misma lógica de tasas existe en ReporteVentas
    // ❌ FEATURE ENVY: Factura está obsesionada con los datos de Producto
    public double calcularSubtotal() {
        double subtotal = 0;
        for (int i = 0; i < productos.size(); i++) {
            subtotal += productos.get(i).getPrecio() * cantidades.get(i);
        }
        return subtotal;
    }

    // ❌ DUPLICATE CODE: Mismas tasas hardcodeadas que en
    // ReporteVentas.calcularImpuestoTotal()
    public double calcularImpuestos() {
        double totalImpuesto = 0;
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            double precioLinea = p.getPrecio() * cantidades.get(i);
            double tasa;

            // ❌ Si cambia la tasa de ALIMENTO, hay que cambiarlo aquí Y en ReporteVentas
            if (p.getCategoria().equals("ALIMENTO")) {
                tasa = 0.05;
            } else if (p.getCategoria().equals("BEBIDA")) {
                tasa = 0.08;
            } else if (p.getCategoria().equals("LIMPIEZA")) {
                tasa = 0.19;
            } else {
                tasa = 0.19; // ELECTRONICO y otros
            }

            totalImpuesto += precioLinea * tasa;
        }
        return totalImpuesto;
    }

    // ❌ DUPLICATE CODE: La lógica de descuento también está en CarritoCompras
    public double calcularDescuento() {
        double subtotal = calcularSubtotal();
        // Descuento del 10% si supera $100.000
        if (subtotal > 100000) {
            return subtotal * 0.10;
        }
        // Descuento del 5% si supera $50.000
        if (subtotal > 50000) {
            return subtotal * 0.05;
        }
        return 0;
    }

    public double calcularTotal() {
        return calcularSubtotal() + calcularImpuestos() - calcularDescuento();
    }

    public void imprimirFactura() {
        System.out.println("========================================");
        System.out.println("FACTURA #" + numeroFactura);
        System.out.println("Cliente: " + nombreCliente);
        System.out.println("----------------------------------------");
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            System.out.printf("%-20s x%d  $%.2f%n",
                    p.getNombre(), cantidades.get(i),
                    p.getPrecio() * cantidades.get(i));
        }
        System.out.println("----------------------------------------");
        System.out.printf("Subtotal:    $%.2f%n", calcularSubtotal());
        System.out.printf("Impuestos:   $%.2f%n", calcularImpuestos());
        System.out.printf("Descuento:   $%.2f%n", calcularDescuento());
        System.out.printf("TOTAL:       $%.2f%n", calcularTotal());
        System.out.println("========================================");
    }
}

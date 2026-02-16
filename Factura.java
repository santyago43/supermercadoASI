import java.util.ArrayList;
import java.util.List;

/**
 * ✅ Factura refactorizada: sin Duplicate Code, sin Feature Envy.
 *
 * ANTES: Factura contenía la lógica de tasas de impuesto (if-else por
 * categoría)
 * y la lógica de descuentos, ambas duplicadas en otras clases.
 *
 * AHORA: Factura ORQUESTA, no calcula reglas que no le pertenecen.
 * - Los impuestos los calcula Producto (quien conoce su categoría)
 * - Los descuentos los calcula PoliticaDescuento (regla de negocio
 * centralizada)
 * - Factura solo suma y coordina
 *
 * ✅ LOW COUPLING: Factura depende de Producto y PoliticaDescuento,
 * pero esa dependencia es INTENCIONAL y representa relaciones reales del
 * dominio.
 *
 * TRADE-OFF a mencionar:
 * Ahora Factura recibe PoliticaDescuento desde afuera (inyección).
 * Esto significa que quien crea la Factura debe proveer la política.
 * ¿Es más complejo? Levemente. ¿Vale la pena? Sí, porque si mañana
 * hay una política diferente para clientes VIP, no hay que tocar Factura.
 */
public class Factura {

    private String numeroFactura;
    private String nombreCliente;
    private List<LineaFactura> lineas;
    private PoliticaDescuento politicaDescuento; // ✅ Dependencia explícita e inyectada

    public Factura(String numeroFactura, String nombreCliente, PoliticaDescuento politicaDescuento) {
        this.numeroFactura = numeroFactura;
        this.nombreCliente = nombreCliente;
        this.politicaDescuento = politicaDescuento;
        this.lineas = new ArrayList<>();
    }

    public void agregarProducto(Producto producto, int cantidad) {
        lineas.add(new LineaFactura(producto, cantidad));
    }

    // ✅ Sin duplicación: delega en LineaFactura → Producto
    public double calcularSubtotal() {
        return lineas.stream()
                .mapToDouble(LineaFactura::getSubtotal)
                .sum();
    }

    // ✅ Sin duplicación: el impuesto lo calcula Producto, no Factura
    public double calcularImpuestos() {
        return lineas.stream()
                .mapToDouble(LineaFactura::getImpuesto)
                .sum();
    }

    // ✅ Sin duplicación: la regla de descuento la aplica PoliticaDescuento
    public double calcularDescuento() {
        return politicaDescuento.calcularDescuento(calcularSubtotal());
    }

    public double calcularTotal() {
        return calcularSubtotal() + calcularImpuestos() - calcularDescuento();
    }

    public void imprimirFactura() {
        System.out.println("========================================");
        System.out.println("FACTURA #" + numeroFactura);
        System.out.println("Cliente: " + nombreCliente);
        System.out.println("----------------------------------------");
        for (LineaFactura linea : lineas) {
            System.out.printf("%-20s x%d  $%.2f%n",
                    linea.getProducto().getNombre(),
                    linea.getCantidad(),
                    linea.getSubtotal());
        }
        System.out.println("----------------------------------------");
        System.out.printf("Subtotal:    $%.2f%n", calcularSubtotal());
        System.out.printf("Impuestos:   $%.2f%n", calcularImpuestos());
        System.out.printf("Descuento:   $%.2f%n", calcularDescuento());
        System.out.printf("TOTAL:       $%.2f%n", calcularTotal());
        System.out.println("========================================");
    }
}

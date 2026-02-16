import java.util.ArrayList;
import java.util.List;

/**
 * ✅ CarritoCompras refactorizado: sin duplicación de impuestos ni descuentos.
 *
 * ANTES: CarritoCompras tenía su propia copia de la lógica de tasas
 * y su propia copia de la lógica de descuentos.
 *
 * AHORA: Reutiliza las mismas reglas que usa Factura.
 * - La coherencia entre el precio que ves en el carrito
 * y el que aparece en la factura está GARANTIZADA por diseño.
 * - No puede haber un bug donde el carrito muestra 10% de descuento
 * pero la factura aplica 5%, porque ambos llaman el mismo método.
 */
public class CarritoCompras {

    private List<LineaFactura> lineas;
    private PoliticaDescuento politicaDescuento;

    public CarritoCompras(PoliticaDescuento politicaDescuento) {
        this.lineas = new ArrayList<>();
        this.politicaDescuento = politicaDescuento;
    }

    public void agregar(Producto producto, int cantidad) {
        lineas.add(new LineaFactura(producto, cantidad));
    }

    public double calcularSubtotal() {
        return lineas.stream()
                .mapToDouble(LineaFactura::getSubtotal)
                .sum();
    }

    // ✅ Sin duplicación: delega en Producto exactamente como lo hace Factura
    public double estimarImpuestos() {
        return lineas.stream()
                .mapToDouble(LineaFactura::getImpuesto)
                .sum();
    }

    // ✅ Sin duplicación: misma PoliticaDescuento que usa Factura
    public double calcularDescuentoPreview() {
        return politicaDescuento.calcularDescuento(calcularSubtotal());
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

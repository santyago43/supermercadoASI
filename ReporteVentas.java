import java.util.ArrayList;
import java.util.List;

/**
 * ✅ ReporteVentas refactorizado: ya no tiene su propia lógica de impuestos.
 *
 * ANTES: Contenía la tercera copia del bloque if-else de categorías/tasas.
 *
 * AHORA: Simplemente llama los métodos de Factura que ya usan
 * la fuente de verdad correcta (Producto y PoliticaDescuento).
 *
 * ✅ LOW COUPLING: ReporteVentas solo depende de Factura.
 * No necesita conocer ni Producto ni PoliticaDescuento directamente.
 * La cadena de delegación hace su trabajo.
 */
public class ReporteVentas {

    private List<Factura> facturas;

    public ReporteVentas() {
        this.facturas = new ArrayList<>();
    }

    public void agregarFactura(Factura factura) {
        facturas.add(factura);
    }

    public void generarReporte() {
        System.out.println("======== REPORTE DE VENTAS ========");
        System.out.printf("Total de facturas: %d%n", facturas.size());

        // ✅ No repite lógica de impuestos ni descuentos — delega en Factura
        double totalVentas = facturas.stream().mapToDouble(Factura::calcularTotal).sum();
        double totalImpuestos = facturas.stream().mapToDouble(Factura::calcularImpuestos).sum();
        double totalDescuentos = facturas.stream().mapToDouble(Factura::calcularDescuento).sum();

        System.out.printf("Total vendido:    $%.2f%n", totalVentas);
        System.out.printf("Total impuestos:  $%.2f%n", totalImpuestos);
        System.out.printf("Total descuentos: $%.2f%n", totalDescuentos);
        System.out.println("===================================");
    }
}

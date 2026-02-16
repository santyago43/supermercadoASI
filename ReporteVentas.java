import java.util.ArrayList;
import java.util.List;

/**
 * ❌ SMELL: Duplicate Code (tercera instancia)
 * El mismo bloque if-else de categorías/tasas aparece por TERCERA VEZ.
 *
 * Este es el momento en la exposición donde preguntan:
 * "¿Qué pasa si la DIAN cambia la tasa de BEBIDAS del 8% al 10%?"
 * Respuesta: hay que abrir Factura, CarritoCompras Y ReporteVentas.
 * Y rezar para no olvidar ninguno.
 */
public class ReporteVentas {

    private List<Factura> facturas;

    public ReporteVentas() {
        this.facturas = new ArrayList<>();
    }

    public void agregarFactura(Factura factura) {
        facturas.add(factura);
    }

    // ❌ DUPLICATE CODE: Tercera copia de la lógica de impuestos
    // Mismas tasas hardcodeadas que en Factura y CarritoCompras
    public double calcularImpuestoTotal(List<Producto> productos, List<Integer> cantidades) {
        double totalImpuesto = 0;
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            double precioLinea = p.getPrecio() * cantidades.get(i);
            double tasa;

            // ❌ Tercer lugar donde vive esta regla de negocio
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

    // ❌ DUPLICATE CODE: Cuarta copia de la lógica de descuentos
    public double calcularDescuentoAplicado(double subtotal) {
        if (subtotal > 100000) {
            return subtotal * 0.10;
        }
        if (subtotal > 50000) {
            return subtotal * 0.05;
        }
        return 0;
    }

    public void generarReporte() {
        System.out.println("======== REPORTE DE VENTAS ========");
        System.out.printf("Total de facturas: %d%n", facturas.size());

        double totalVentas = 0;
        for (Factura f : facturas) {
            totalVentas += f.calcularTotal();
        }
        System.out.printf("Total vendido: $%.2f%n", totalVentas);
        System.out.println("===================================");
    }
}

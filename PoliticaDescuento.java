/**
 * ✅ DRY APLICADO: La regla de negocio de descuentos vive en UN solo lugar.
 *
 * Antes esta lógica estaba duplicada en Factura, CarritoCompras y
 * ReporteVentas.
 * Ahora hay una sola representación autorizada de "cómo se calculan los
 * descuentos".
 *
 * ✅ LOW COUPLING — ¿Por qué NO es una clase utilitaria artificial?
 * Porque representa un concepto real del dominio: la POLÍTICA de descuentos
 * del supermercado. No es solo una función suelta, es una regla de negocio
 * que tiene identidad propia y puede evolucionar (nuevas reglas, temporadas,
 * etc.)
 *
 * TRADE-OFF a mencionar en la exposición:
 * Factura, CarritoCompras y ReporteVentas ahora dependen de esta clase.
 * ¿Es mal acoplamiento? No, porque:
 * 1. Dependen de un concepto del dominio, no de un detalle de implementación.
 * 2. El acoplamiento existía antes — solo que estaba OCULTO en código
 * duplicado.
 * 3. Ahora el acoplamiento es EXPLÍCITO y controlable.
 */
public class PoliticaDescuento {

    private static final double UMBRAL_DESCUENTO_ALTO = 100_000.0;
    private static final double UMBRAL_DESCUENTO_MEDIO = 50_000.0;
    private static final double TASA_DESCUENTO_ALTO = 0.10;
    private static final double TASA_DESCUENTO_MEDIO = 0.05;

    /**
     * ✅ Una sola función que implementa la regla de descuento.
     * Si el negocio decide cambiar umbrales, se cambia aquí. Una vez. Solo aquí.
     */
    public double calcularDescuento(double subtotal) {
        if (subtotal > UMBRAL_DESCUENTO_ALTO) {
            return subtotal * TASA_DESCUENTO_ALTO;
        }
        if (subtotal > UMBRAL_DESCUENTO_MEDIO) {
            return subtotal * TASA_DESCUENTO_MEDIO;
        }
        return 0;
    }
}

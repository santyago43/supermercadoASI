public class Main {

    public static void main(String[] args) {

        System.out.println("============================================\n");
        Producto leche = new Producto("Leche Entera 1L", 3200, Producto.Categoria.ALIMENTO);
        Producto gaseosa = new Producto("Coca-Cola 2L", 5800, Producto.Categoria.BEBIDA);
        Producto deterg = new Producto("Detergente 500g", 8900, Producto.Categoria.LIMPIEZA);

        PoliticaDescuento politica = new PoliticaDescuento();

        Factura factura = new Factura("F-001", "Carlos López", politica);
        factura.agregarProducto(leche, 3);
        factura.agregarProducto(gaseosa, 2);
        factura.agregarProducto(deterg, 1);
        factura.imprimirFactura();

        System.out.println("\n✅  Si la DIAN cambia la tasa de BEBIDA,");
        System.out.println("   se cambia en Producto.getTasaImpuesto().");
        System.out.println("   Solo ahí. Una vez. Garantizado.");
    }

}

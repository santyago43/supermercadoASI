# 🛒 Sistema de Facturación — Supermercado

## Exposición: DRY + Low Coupling (GRASP)

---

## 📦 Dominio Elegido

Sistema de facturación de un supermercado con tres funcionalidades:

- Cálculo de impuestos por categoría de producto
- Aplicación de descuentos por volumen
- Generación de factura y reporte de ventas

---

## 🐛 Problema de Diseño Inicial

El sistema fue construido sin aplicar DRY ni considerar el acoplamiento.
El resultado fue el siguiente:

### Duplicate Code (Smell)

La lógica de **tasas de impuesto por categoría** aparece copiada en tres clases distintas:

| Clase            | Método                    |
| ---------------- | ------------------------- |
| `Factura`        | `calcularImpuestos()`     |
| `CarritoCompras` | `estimarImpuestos()`      |
| `ReporteVentas`  | `calcularImpuestoTotal()` |

La lógica de **descuentos por umbral** aparece duplicada en:

| Clase            | Método                        |
| ---------------- | ----------------------------- |
| `Factura`        | `calcularDescuento()`         |
| `CarritoCompras` | `calcularDescuentoPreview()`  |
| `ReporteVentas`  | `calcularDescuentoAplicado()` |

**Consecuencia práctica:** Si la DIAN cambia la tasa de BEBIDAS del 8% al 10%,
hay que localizar y modificar el valor en 3 archivos distintos. El compilador
no avisa si alguno queda sin actualizar.

### Feature Envy (Smell)

`Factura` accedía a `producto.getCategoria()` para decidir la tasa de impuesto.
Una clase estaba tomando decisiones basadas en los datos íntimos de otra,
en lugar de pedirle al propio Producto que las tome.

---

## ✅ Principios Aplicados

### DRY — Don't Repeat Yourself

> "Every piece of **knowledge** must have a single, unambiguous,
> authoritative representation within a system."

La clave es **conocimiento**, no código. Dos bloques de código pueden
verse similares y ser conceptualmente distintos. Lo que violaba DRY
era que la misma _regla de negocio_ vivía en múltiples lugares.

**Solución:**

- La lógica de tasas de impuesto se centraliza en `Producto.getTasaImpuesto()`
- La lógica de descuentos se centraliza en `PoliticaDescuento.calcularDescuento()`

### Low Coupling (GRASP)

> Asignar responsabilidades para minimizar dependencias innecesarias,
> no para eliminar dependencias legítimas.

**Decisión clave — ¿por qué NO crear `CalculadoraImpuestos`?**

Una clase `CalculadoraImpuestos` sería una clase utilitaria artificial.
Haría que `Factura`, `CarritoCompras` y `ReporteVentas` dependieran de ella,
pero ese conocimiento ya tiene un hogar natural: `Producto`.

El impuesto depende de la _categoría del producto_ → el producto calcula su impuesto.
No hay acoplamiento artificial: la dependencia refleja la realidad del dominio.

---

## 🔄 Comparación Antes vs. Después

### Diagrama de clases — Antes

```
Factura ──────────────── Producto
  │  (accede a categoría   (solo datos,
  │   para calcular tasa)   sin lógica)
  │
CarritoCompras ──────── Producto
  │  (duplica la misma
  │   lógica de tasas)
  │
ReporteVentas ───────── Producto
     (tercera copia de
      la misma lógica)
```

Las flechas indican que cada clase tenía que "meterse" en los datos de Producto.

### Diagrama de clases — Después

```
Factura ─────────────────────────── Producto
  │    (delega: linea.getImpuesto())  (dueño del conocimiento
  │                                    de tasas)
  │
  └──── PoliticaDescuento            LineaFactura
  │     (dueño del conocimiento    (agrupa Producto + cantidad,
  │      de descuentos)             delega en Producto)
  │
CarritoCompras ─── PoliticaDescuento
                   (misma instancia, misma regla)

ReporteVentas ─── Factura
                  (solo lee totales, no duplica lógica)
```

---

## ⚖️ Trade-offs y Decisiones

### El acoplamiento no desaparece — se vuelve explícito

Antes de refactorizar, el acoplamiento **existía igualmente**,
pero estaba oculto dentro de código duplicado. Si se cambiaba
la tasa en `Factura` pero no en `CarritoCompras`, el sistema
mostraba precios inconsistentes sin ningún error visible.

Después de refactorizar, el acoplamiento es **intencional y controlable**.
`Factura` depende de `PoliticaDescuento`, y eso es bueno: significa que
cualquier cambio en la política se propaga automáticamente a todos los
que la usan.

### ¿Cuándo esta solución podría ser excesiva?

- Si el sistema nunca va a tener más de una pantalla de consulta de precios.
- Si las tasas de impuesto nunca cambian en el negocio.
- Si el equipo es de una sola persona y el código vive menos de 6 meses.

En esos casos, la duplicación controlada puede ser preferible a introducir
abstracciones que nadie más va a mantener.

### El límite de DRY

No toda similitud es duplicación. Si hubiera un módulo de contabilidad con
su propia definición de "tasa aplicable" basada en reglas contables distintas
a las del punto de venta, esos dos módulos NO deberían compartir la misma clase,
aunque el número resultante sea `0.19` en ambos casos. Son conocimientos distintos.

---

## 📁 Estructura del Repositorio

```
supermarket/
├── before/
│   └── model/
│       ├── Producto.java          ← sin lógica de impuesto
│       ├── Factura.java           ← ❌ Duplicate Code + Feature Envy
│       ├── CarritoCompras.java    ← ❌ Duplicate Code
│       └── ReporteVentas.java     ← ❌ Duplicate Code (tercera copia)
│
├── after/
│   └── model/
│       ├── Producto.java          ← ✅ dueño de su tasa de impuesto
│       ├── LineaFactura.java      ← ✅ objeto valor (producto + cantidad)
│       ├── PoliticaDescuento.java ← ✅ única fuente de verdad de descuentos
│       ├── Factura.java           ← ✅ orquesta, no duplica
│       ├── CarritoCompras.java    ← ✅ reutiliza las mismas reglas
│       └── ReporteVentas.java     ← ✅ delega en Factura
│
└── Main.java                      ← demo comparativo de ambas versiones
```

---

## 👥 Equipo G1

Materia: Diseño y Arquitectura de Software
Principios: DRY (Don't Repeat Yourself) + Low Coupling (GRASP)
Integrantes:

- AGUDELO JIMÉNEZ MAURICIO
- ARTEAGA GARCIA CARLOS ANDRES
- FLÓREZ ROJAS JUAN PABLO
- LOPEZ VALLEJO SANTIAGO
- SOTELO GALVIS ALVARO DE JESUS

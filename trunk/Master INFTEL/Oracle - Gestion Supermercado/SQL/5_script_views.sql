/************************************************
* VISTA QUE DEVUELVA LOS BENEFICIOS BRUTOS
* ANUALES PO VENTS DE LOS ÚLTMOS 10 AÑOS.
*************************************************/

CREATE OR REPLACE VIEW beneficios_brutos_anuales 
AS 
	SELECT h.anio,(
		NVL((SELECT sum(dv.precio*dv.cantidad)
			FROM detalle_ventas dv, ventas v
			WHERE dv.codventa=v.codventa
			AND to_number(extract(year from v.fecventa))>=h.anio
			AND to_number(extract(year from v.fecventa))<(h.anio+1)
			),0)
		-
		NVL(sum(h.gastoanual),0)
		) BENEFICIO_BRUTO
	FROM historico h
	WHERE h.anio > (to_number(extract(year from sysdate))-10)
	GROUP BY h.anio
	ORDER BY h.anio DESC
;

/***********************************************
* VISTA QUE DEVUELVA EL PRODUCTO MÁS VENDIDO
* DE CADA DEPARTAMENTO 
************************************************/
CREATE OR REPLACE VIEW producto_mas_vendido
AS
  SELECT g1.departamento,g2.producto, g2.unidades_vendidas
  FROM (SELECT b.nomdept AS departamento,MAX(b.unidades_vendidas) AS max_ventas
        FROM (SELECT d.nomdept,p.codbarras,sum(dv.cantidad) AS unidades_vendidas
              FROM producto p,detalle_ventas dv, departamento d
              WHERE  p.codbarras=dv.codbarras AND p.numdept=d.numdept
              GROUP BY p.codbarras,d.nomdept) b
        GROUP BY b.nomdept) g1,
        (SELECT p.codbarras AS producto, sum(dv.cantidad) AS unidades_vendidas
        FROM producto p,detalle_ventas dv
        WHERE p.codbarras = dv.codbarras
  GROUP BY p.codbarras) g2
  WHERE g2.unidades_vendidas=g1.max_ventas
;

/********************************************
* VISTA QUE DEVUELVE LA FRECUENCIA CON LA
* QUE SE VENDEN DOS PRODUCTOS JUNTOS, 
* RESPECTO AL TOTAL DE VENTAS DEL PRIMER
* PRODUCTO.
*********************************************/

CREATE OR REPLACE VIEW frecuencia_venta_dos_productos 
AS  
SELECT p1.codbarras codigobarras1, p2.codbarras codigobarras2, TRUNC(((SELECT COUNT(*) 
																 FROM detalle_ventas dv
																 WHERE codbarras = p1.codbarras
																 AND EXISTS (SELECT codbarras
																 		     FROM detalle_ventas
																			 WHERE codbarras = p2.codbarras
																			 AND codventa = dv.codventa)) / (SELECT (case COUNT(*) when 0 then 1 else COUNT(*) end)total_a 
																											 FROM detalle_ventas
																											 WHERE codbarras = p1.codbarras)),3) FRECUENCIA
FROM producto p1, producto p2
WHERE p1.codbarras <> p2.codbarras;							


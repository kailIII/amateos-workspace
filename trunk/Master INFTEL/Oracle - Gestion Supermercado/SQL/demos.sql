/********************************************
** TEST DE REALIZACION DE UNA COMPRA
********************************************/

set serveroutput on;

DECLARE
total NUMBER;
BEGIN

-- iniciamos la compra indicando la caja
GESTION_VENTAS.inicia_compra(8);

-- se compran productos
GESTION_VENTAS.compra_producto(8,3394233411002,2);
GESTION_VENTAS.compra_producto(8,3394233411002,2); -- se compran dos unidades más del producto anterior
GESTION_VENTAS.compra_producto(8,8394200441002,5);
GESTION_VENTAS.compra_producto(8,3394199811012,2);
GESTION_VENTAS.compra_producto(8,3394199811012,-1); -- una de las unidades del producto anterior es devuelta

-- se finaliza la compra
total:=GESTION_VENTAS.Total_compra(8);
GESTION_VENTAS.Finaliza_compra(GESTION_VENTAS.Obtener_Codigo_Venta(8),0);
DBMS_OUTPUT.PUT_LINE('Total a pagar: '||total||' Û');

END;


/********************************************
** TEST DE REALIZACION DE PEDIDOS
********************************************/
update stock set numstock=10 where codbarras=3494200211562;


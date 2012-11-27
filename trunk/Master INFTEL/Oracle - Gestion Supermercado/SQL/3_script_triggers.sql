

-- PUNTO 5
/*******************************************************
* TRIGGER PARA ALMACENAR EL PRECIO DE UN PRODUCTO
* EN LA TABLA HISTÓRICO_PRECIOS JUNTO CON LA FECHA
* EN EL EL PRODUCTO TENÍA DICHO PRECIO.
*********************************************************/

CREATE OR REPLACE 
TRIGGER almacenar_precio_historico AFTER INSERT ON producto
FOR EACH ROW
BEGIN

	-- INSERTAMOS DIRECTAMENTE LOS CAMPOS QUE DESEAMOS DEL PRODUCTO
	-- EN LA TABLA HISTÓRICO CON LA FECHA DE HOY.
	INSERT INTO historico_precios (codbarras, fecprecio, precio) 
	  VALUES (:NEW.codbarras, TO_DATE(SYSDATE, 'DD-MM-YY HH24:mi:ss'), :NEW.precio);
	
END;
/





/************************************************
* FUNCTION: tProductoUpdate
* OBJETIVO:
************************************************/


CREATE OR REPLACE 
TRIGGER reponer_estanterias INSTEAD OF UPDATE ON PRODUCTO_V

DECLARE

    min_estanteria NUMBER;
    max_estanteria NUMBER;

BEGIN

		SELECT  minestanteria, maxestanteria 
		 INTO min_estanteria, max_estanteria 
		FROM stock 
		WHERE codbarras=:NEW.codbarras; 

		IF(:NEW.existencias<min_estanteria) THEN

			UPDATE stock 
			SET numstock=numstock-(maxestanteria-:NEW.existencias) 
			WHERE codbarras = :NEW.codbarras;
        
			UPDATE producto 
			SET existencias=max_estanteria 
			WHERE codbarras = :NEW.codbarras;
    
		END IF;
	
		IF(:NEW.precio != :OLD.precio) THEN
			INSERT INTO historico_precios (codbarras,fecprecio,precio) 
			 VALUES (:NEW.codbarras, SYSDATE,:NEW.precio);
    
		END IF;

END;
/





-- PUNTO 7 --
/**********************************************************
* TRIGGER PARA REALIZAR UN PEDIDO CUANDO STOCK SEA
* MODIFICADO, USANDO LOS DATOS RETORNADOS DE LA FUNCIÓN
* mejor_oferta Y CONTROLANDO QUE NO SE HAYA HECHO ANTES
***********************************************************/
create or replace
TRIGGER realizar_un_pedido AFTER UPDATE ON stock
FOR EACH ROW

DECLARE

-- VARIABLE PARA ALMACENAR LAS UNIDADES A PEDIR
-- EN NUESTRO PEDIDO.
v_unidades	NUMBER:=0;

-- VARIABLE PARA ALMACENAR EL IDENTIFICATIVO
-- DE LA OFERTA PARA REALIZAR UN PEDIDO MÁS CONVENIENTE.
v_idelegida	ofertas.idoferta%TYPE;

-- VARIABLE PARA ALMACENAR CUÁNTOS PEDIDOS HAY YA HECHOS
-- PARA UN PRODUCTO.
v_existe	NUMBER;

BEGIN 

	-- SI EL STOCK BAJA DEL MINIMO --
	IF(:NEW.numstock < :NEW.minstock ) THEN
	
		-- FUNCION MEJOR OFERTA --
		-- LE PASAMOS EL CÓDIDO DE BARRAS Y NOS TIENE QUE DEVOLVER EL ID DE LA OFERTA MEJOR Y LAS UNIDADES A PEDIR --
		v_idelegida:= gestion_pedidos.mejor_oferta(:NEW.codBarras, :NEW.maxstock, :NEW.minstock, :NEW.numstock, v_unidades);
		
			-- COMPROBAMOS QUE NO HAY UN PEDIDO REALIZADO ANTERIORMENTE PARA ESE PRODUCTO.
			SELECT 	COUNT(*)
                   INTO v_existe
                   FROM	pedidos_realizados
                   WHERE	v_idelegida = idoferta
                   AND		recibido = 'n';
			
			-- SI v_existe ES IGUAL A 0 NO HAY NINGÚN PEDIDO Y, POR TANTO, LO REALIZAMOS.
			IF (v_existe = 0) THEN
			
				-- INSERTAMOS UN NUEVO PEDIDO --
				INSERT INTO pedidos_realizados 
				 VALUES (v_idelegida, TO_DATE(SYSDATE, 'DD-MM-YY HH24:mi:ss'), v_unidades, 'n');

  		    END IF;
			
		END IF;	
		
END realizar_un_pedido;
/




/************************************************
* FUNCTION tVentas 
* OBJETIVO: 
*   
************************************************/

CREATE OR REPLACE 
TRIGGER actualizar_detalle_ventas INSTEAD OF INSERT ON DETALLE_VENTAS_V

DECLARE

	num_productos NUMBER(4);

	BEGIN
		
		SELECT COUNT(*) 
		 INTO num_productos 
		FROM detalle_ventas 
		WHERE codbarras=:NEW.codbarras AND codventa=:NEW.codventa;
    
		IF(num_productos=0) THEN
			
			INSERT INTO detalle_ventas 
			 VALUES (:NEW.codventa,:NEW.codbarras,:NEW.cantidad, :NEW.precio);
		
		ELSE
		
			UPDATE detalle_ventas 
			SET cantidad = cantidad+:NEW.cantidad 
			WHERE codbarras=:NEW.codbarras AND codventa=:NEW.codventa;
    
		END IF;
		
	    GESTION_VENTAS.retirar_de_estanteria(:NEW.codbarras,:NEW.cantidad);

END;
/


/************************************************
* TRIGGER reponer_estanterias_dv
* OBJETIVO: actualizar existencias tras deshacer una venta
*   
************************************************/
create or replace
TRIGGER reponer_estanterias_dv BEFORE DELETE ON DETALLE_VENTAS
FOR EACH ROW
DECLARE

    min_estanteria NUMBER;
    max_estanteria NUMBER;
    v_existencias NUMBER;

BEGIN

    select min_estanteria into min_estanteria
    from stock
    where codbarras = :OLD.codbarras;
    
    select max_estanteria into max_estanteria
    from stock
    where codbarras = :OLD.codbarras;
    
    select existencias into v_existencias
    from producto
    where codbarras = :OLD.codbarras;
    
    
    IF (:OLD.cantidad+v_existencias>=max_estanteria ) THEN
       update producto
       set existencias = max_estanteria
       where codbarras = :OLD.codbarras;
       
       update stock
       set numstock = numstock+ (ABS(:OLD.cantidad-max_estanteria))
       where codbarras = :OLD.codbarras;
    
    
    ELSE
        UPDATE PRODUCTO
        SET existencias = :OLD.cantidad+v_existencias
        where codbarras = :OLD.codbarras;  
    END IF;
    

END;
/




CREATE OR REPLACE PACKAGE gestion_ventas IS

	PROCEDURE	Crear_Nueva_Venta( p_caja IN VENTAS.caja%TYPE );
	PROCEDURE	Deshacer_Venta( p_codventa IN VENTAS.codventa%TYPE );
	PROCEDURE 	Inicia_Compra(p_caja IN VENTAS.caja%TYPE);
	PROCEDURE 	Finaliza_Compra(p_codventa IN VENTAS.codventa%TYPE, p_numtarjeta IN VENTAS.numtarjeta%TYPE);
	PROCEDURE 	Compra_Producto(p_caja IN VENTAS.caja%TYPE, p_codbarras IN PRODUCTO.codbarras%TYPE, p_ud IN PRODUCTO.existencias%TYPE );
	PROCEDURE 	retirar_de_estanteria( cb IN PRODUCTO.codbarras%TYPE, unidades IN PRODUCTO.existencias%TYPE );
	FUNCTION	Venta_Iniciada(p_caja IN VENTAS.caja%TYPE) RETURN BOOLEAN;
	FUNCTION	Obtener_Codigo_Venta(p_caja IN VENTAS.caja%TYPE) RETURN NUMBER;
	FUNCTION	Total_Compra(p_caja IN VENTAS.caja%TYPE) RETURN NUMBER;
		
END gestion_ventas;
/



CREATE OR REPLACE PACKAGE gestion_pedidos IS

	FUNCTION 	prediccion_media (cb PRODUCTO.codbarras%TYPE) RETURN NUMBER;
	FUNCTION  	prediccion_pendiente (cb PRODUCTO.codbarras%TYPE) RETURN NUMBER;
	FUNCTION 	mejor_oferta (cb IN PRODUCTO.codbarras%TYPE, v_maxstock IN STOCK.maxstock%TYPE, v_minstock IN STOCK.minstock%TYPE, v_numstock IN STOCK.numstock%TYPE, v_unidades_pedido OUT NUMBER) RETURN NUMBER;
	
END gestion_pedidos;
/




/*******************************************************************************
*  PACKAGE: gestion_ventas
*******************************************************************************/
CREATE OR REPLACE PACKAGE BODY gestion_ventas IS  

/*******************************************************************************
 * PROCEDURE Crear_Nueva_Venta 
*  - p_caja: código de la caja que inicia una venta
* DESCRIPCION: 
*    Crea una venta con codigo de caja y fecha de inicio de venta
********************************************************************************/
PROCEDURE Crear_Nueva_Venta( p_caja IN VENTAS.caja%TYPE ) IS
BEGIN
 
	INSERT INTO VENTAS_V (caja,fecventa)
     VALUES (p_caja, TO_DATE(SYSDATE, 'DD-MM-YY HH24:mi:ss')); 

EXCEPTION
	WHEN OTHERS THEN
	    RAISE_APPLICATION_ERROR(-20001, 'Error en procedimiento gestion_ventas.crear_nueva_venta');

END Crear_Nueva_Venta;

  
  
  

/*******************************************************************************
* PROCEDURE Deshacer_Venta                                                     
*  - p_codventa: venta a eliminar de DETALLE_VENTAS                            
* DESCRIPCION: Deshace una venta iniciada. Elimina las filas de DETALLE_VENTAS 
*   y elimina la fila de VENTAS correspondiente.                               
********************************************************************************/
PROCEDURE Deshacer_Venta( p_codventa IN VENTAS.codventa%TYPE ) IS  
BEGIN
  
    DELETE FROM DETALLE_VENTAS_V
    WHERE codventa = p_codventa;
          
    DELETE FROM VENTAS_V
    WHERE codventa = p_codventa;
EXCEPTION
	WHEN OTHERS THEN
	    RAISE_APPLICATION_ERROR(-20001, 'Error en procedimiento gestion_ventas.deshacer_venta');
  
END Deshacer_Venta;


  
  
  
/*******************************************************************************
* FUNCTION Venta_Iniciada                                                      
* Argumentos:                                                                  
*   - p_caja: identificador de la caja                                         
* Return:                                                                      
*   - TRUE: La venta se ha iniciado                                            
*   - FALSE: En caso contrario                                                 
********************************************************************************/
FUNCTION Venta_Iniciada(p_caja IN VENTAS.caja%TYPE) RETURN BOOLEAN IS
    
	v_num_filas       NUMBER:=0;
  v_venta_iniciada  BOOLEAN;

BEGIN
      -- comprobamos si la caja tiene una venta iniciada
      SELECT count(*) INTO v_num_filas
            FROM VENTAS_V
            WHERE (caja=p_caja) AND (forma_pago IS NULL);
    
      IF (v_num_filas>0) THEN
          v_venta_iniciada:=TRUE;
      ELSE
          v_venta_iniciada:=FALSE;
      END IF;    
      
      RETURN v_venta_iniciada;
EXCEPTION
	WHEN OTHERS THEN
	    RAISE_APPLICATION_ERROR(-20001, 'Error en función gestion_ventas.venta_iniciada');	  

END Venta_Iniciada;

  
  
  
  

/*******************************************************************************
* FUNCTION Obtener_Codigo_Venta 
* Argumentos: 
*   - p_caja: identificador de la caja
* Return:
*   - Codigo de venta iniciada por p_caja
*******************************************************************************/
  FUNCTION Obtener_Codigo_Venta(p_caja IN VENTAS.caja%TYPE) RETURN NUMBER IS
  
  v_cod_venta VENTAS.codventa%TYPE:=0;  

BEGIN

    SELECT codventa INTO v_cod_venta
      FROM VENTAS_V
      WHERE (caja=p_caja) AND (forma_pago IS NULL);    
    
    RETURN v_cod_venta;
    
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20001, 'ERROR: Venta NO Existente');
	
END Obtener_Codigo_Venta;




/*******************************************************************************
* PROCEDURE Inicia_Compra 
* Argumentos: 
*    - p_caja: código de la caja que inicia una venta
*******************************************************************************/
PROCEDURE Inicia_Compra(p_caja IN VENTAS.caja%TYPE) IS

  v_cod_venta VENTAS.codventa%TYPE;

BEGIN
    --  si la caja ya tiene una venta iniciada la deshacemos
    IF (gestion_ventas.Venta_Iniciada(p_caja)) THEN
        v_cod_venta := gestion_ventas.Obtener_Codigo_Venta(p_caja);    
        gestion_ventas.Deshacer_Venta(v_cod_venta);    
    END IF;
    
    -- Iniciamos la nueva venta
    gestion_ventas.Crear_Nueva_Venta(p_caja);   
EXCEPTION
	WHEN OTHERS THEN
	    RAISE_APPLICATION_ERROR(-20001, 'Error en procedimiento gestion_ventas.inicia_compra');
		
END Inicia_Compra;



/*******************************************************************************
* PROCEDURE Finaliza_Compra 
* Argumentos: 
*   - p_codventa: identificador de la venta
*   - p_numtarjeta: numero de tarjeta. 
*         Si 0  -> Pago en efectivo
*         Si -1 -> Devolución de la compra
*******************************************************************************/
PROCEDURE Finaliza_Compra(p_codventa IN VENTAS.codventa%TYPE, p_numtarjeta IN VENTAS.numtarjeta%TYPE) IS
BEGIN
  IF ( p_numtarjeta = -1 ) THEN
     -- Devolución de la compra
     gestion_ventas.Deshacer_Venta(p_codventa);
  ELSIF (p_numtarjeta = 0 ) THEN
    -- Pago en efectivo
      UPDATE VENTAS_V 
        SET forma_pago='E' 
        WHERE codventa=p_codventa;
  ELSE
    -- Pago con tarjeta
      UPDATE VENTAS_V 
        SET forma_pago='T',numtarjeta=p_numtarjeta
        WHERE codventa=p_codventa;  
  END IF;

EXCEPTION
	WHEN OTHERS THEN
	    RAISE_APPLICATION_ERROR(-20001, 'Error en procedimiento gestion_ventas.finaliza_compra');  
END Finaliza_Compra;



/*******************************************************************************
* PROCEDURE Compra_Producto 
* Argumentos: 
*   - p_caja: identificador de la caja que hace la venta 
*   - p_codbarras: código de barras del producto
*   - p_ud: número de unidades. Si p_ud=-1 -> Devolver producto.
*
*******************************************************************************/
PROCEDURE Compra_Producto(p_caja IN VENTAS.caja%TYPE, p_codbarras IN PRODUCTO.codbarras%TYPE, p_ud IN PRODUCTO.existencias%TYPE ) IS
  
	v_cod_venta 	                VENTAS.codventa%TYPE;
	v_existencias	                PRODUCTO.existencias%TYPE;
	v_numstock		                STOCK.numstock%TYPE;
	v_precio		                  PRODUCTO.precio%TYPE;
  v_uds			                    DETALLE_VENTAS.cantidad%TYPE;
	v_num_existe	                NUMBER;
  v_unidades_maximas_devolucion NUMBER;

BEGIN

    -- Comprobamos si tenemos productos suficientes para servir. 
    -- En su defecto ofrecemos el máximo disponible
    SELECT existencias INTO v_existencias
      FROM PRODUCTO
      WHERE codbarras=p_codbarras;
            
    SELECT numstock INTO v_numstock
      FROM STOCK 
      WHERE codbarras=p_codbarras;   
    
    v_existencias := v_existencias+v_numstock;
            
    IF (v_existencias>=p_ud) THEN
    
        --Si hay suficientes existencias (estantería+almacen) lo añadimos a la cesta
        v_uds:=p_ud;
    
    ELSE
        --Añadimos a la cesta solo las unidades disponibles en estantería + almacen
        v_uds:=v_existencias;
    
    END IF;

    --si la caja NO tiene la venta iniciada iniciamos una nueva
    IF (NOT gestion_ventas.Venta_Iniciada(p_caja)) THEN
      
        -- Iniciamos la nueva venta
        gestion_ventas.Crear_Nueva_Venta(p_caja);
    
    END IF;
    
    -- obtenemos el codigo de venta asignado
    v_cod_venta := gestion_ventas.Obtener_Codigo_Venta(p_caja);
    
    
    -- En caso de devolucion comprobamos que no se intentan devolver mas unidades de las que se han comprado
    IF (v_uds<0) THEN
            
      -- se obtiene el número de unidades que han sido compradas
      SELECT cantidad
        INTO v_unidades_maximas_devolucion
        FROM DETALLE_VENTAS
        WHERE codbarras=p_codbarras
        AND codventa = v_cod_venta;

            
        -- como maximo se pueden devolver el mismo numero de unidades que han sido compradas
        IF (ABS(v_uds) > v_unidades_maximas_devolucion) THEN          
          v_uds := -v_unidades_maximas_devolucion;  
        END IF;
        
        
    END IF;
    
		-- A–adimos nueva fila en DETALLE_VENTAS   
		-- anotamos el precio al que se hizo la venta para optimizaciones en triggers
		SELECT precio INTO v_precio 
        FROM PRODUCTO
        WHERE codbarras=p_codbarras;
      
		INSERT INTO DETALLE_VENTAS_V
         VALUES (v_cod_venta, p_codbarras,v_uds,v_precio);
      
--    END IF; 

EXCEPTION
	WHEN OTHERS THEN
	    RAISE_APPLICATION_ERROR(-20001, 'Error en procedimiento gestion_ventas.compra_producto');
		
END Compra_Producto;


/*******************************************************************************
* PROCEDURE Retirar_de_estanteria
* Argumentos: 
*   - codbarras: codigo de barras del producto
*   - unidades: numero de unidades a retirar
*******************************************************************************/
PROCEDURE retirar_de_estanteria( cb IN PRODUCTO.codbarras%TYPE, unidades IN PRODUCTO.existencias%TYPE ) IS
 
   v_existencias PRODUCTO.existencias%TYPE;

 BEGIN
  SELECT existencias INTO v_existencias
    FROM producto
    WHERE codbarras = cb;

  -- Se restan los productos de la estanteria  
  UPDATE producto 
    SET existencias = v_existencias-unidades
    WHERE codbarras = cb;

  -- Para reponer estanterías
  UPDATE producto_v 
    SET existencias=existencias 
    WHERE codbarras = cb;
  
END retirar_de_estanteria;



/*******************************************************************************
* FUNCTION Total_Compra 
* Argumentos: 
*   - p_caja: identificador de la caja
* Return:
*   - Total actual de la venta
*******************************************************************************/
FUNCTION Total_Compra(p_caja IN VENTAS.caja%TYPE) RETURN NUMBER IS

	v_cod_venta 	  VENTAS.codventa%TYPE;
	v_num_filas 	  NUMBER:=0;
	v_total_compra	NUMBER:=0;

BEGIN

    -- si la caja tiene una venta iniciada calculamos total venta productos
    IF (gestion_ventas.Venta_Iniciada(p_caja)) THEN
      
        -- capturamos el codigo de venta
        v_cod_venta := gestion_ventas.Obtener_Codigo_Venta(p_caja);  

        -- calculamos el total de la compra
        SELECT SUM(cantidad*precio) INTO v_total_compra
          FROM DETALLE_VENTAS_V
          WHERE (codventa=v_cod_venta);            
    
    END IF;        
    
    RETURN v_total_compra;

EXCEPTION
	WHEN OTHERS THEN
	    RAISE_APPLICATION_ERROR(-20001, 'Error en procedimiento gestion_ventas.total_compra');
		
END Total_Compra;



END gestion_ventas;
/







/*******************************************************************************
*  PACKAGE: gestion_pedidos
*******************************************************************************/
CREATE OR REPLACE PACKAGE BODY gestion_pedidos IS  



/*******************************************************************************
* FUNCTION prediccion_media
* Argumentos: 
*   - cb: identificador del producto
* Return:
*   - total
*******************************************************************************/
FUNCTION prediccion_media (cb PRODUCTO.codbarras%TYPE)
RETURN NUMBER AS 

v_primera	      NUMBER;
v_segunda	      NUMBER;
v_tercera	      NUMBER;
v_cuarta      	NUMBER;
v_total		      NUMBER;
v_divisor_media NUMBER:=0;

BEGIN 

		SELECT SUM(dv.cantidad/7) INTO v_primera
        FROM VENTAS_V v, DETALLE_VENTAS_V dv
        WHERE v.codventa = dv.codventa 
          AND dv.codbarras=cb
          AND v.fecventa< SYSDATE 
          AND v.fecventa>=SYSDATE-8;
      
      IF v_primera IS NULL THEN
        v_primera := 0;
      ELSE
        v_divisor_media := v_divisor_media + 4;
      END IF ;
      

		SELECT SUM(dv.cantidad/7) INTO v_segunda 
        FROM VENTAS_V v, DETALLE_VENTAS_V dv
        WHERE v.codventa = dv.codventa 
          AND dv.codbarras=cb
          AND v.fecventa< SYSDATE-8 
          AND v.fecventa>=SYSDATE-15;
      
      IF v_segunda IS NULL THEN
        v_segunda := 0;
      ELSE
        v_divisor_media := v_divisor_media + 3;
      END IF ;

			
		SELECT SUM(dv.cantidad/7) INTO v_tercera
        FROM VENTAS_V v, DETALLE_VENTAS_V dv
        WHERE v.codventa = dv.codventa
          AND dv.codbarras=cb
          AND v.fecventa< SYSDATE-15 
          AND v.fecventa>=SYSDATE-22;
      
      IF v_tercera IS NULL THEN
        v_tercera := 0;
      ELSE
        v_divisor_media := v_divisor_media + 2;
      END IF ;


		SELECT SUM(dv.cantidad/7) INTO v_cuarta
        FROM VENTAS_V v, DETALLE_VENTAS_V dv
        WHERE v.codventa = dv.codventa 
          AND dv.codbarras=cb
          AND v.fecventa< SYSDATE-22 
          AND v.fecventa>=SYSDATE-29;

      IF v_cuarta IS NULL THEN
        v_cuarta := 0;
      ELSE
        v_divisor_media := v_divisor_media + 1;
      END IF ;

      -- si el divisor es 0 se pone a 1 para no dividir por 0. La media saldrá 0 de todas maneras
      IF v_divisor_media = 0 THEN
        v_divisor_media := 1;
      END IF;

			v_total:= (4*v_primera+3*v_segunda+2*v_tercera+v_cuarta)/v_divisor_media;

			return(v_total);

EXCEPTION
	WHEN OTHERS THEN
	    RAISE_APPLICATION_ERROR(-20001, 'Error en procedimiento gestion_pedidos.prediccion_media');			
			
END prediccion_media;








-- PUNTO 6



/*******************************************************************************
* FUNCTION prediccion_pendiente 
* Argumentos: 
*   - cb: identificador del producto
* Return:
*   - total
*******************************************************************************/
FUNCTION prediccion_pendiente (cb PRODUCTO.codbarras%TYPE)
RETURN NUMBER AS 

v_primera	NUMBER;
v_segunda	NUMBER;
v_tercera	NUMBER;
v_cuarta	NUMBER;
v_total		NUMBER;

	BEGIN 

		SELECT SUM(dv.cantidad/7) INTO v_primera
        FROM VENTAS_V v, DETALLE_VENTAS_V dv
          WHERE v.codventa = dv.codventa 
            AND dv.codbarras=cb
            AND v.fecventa< SYSDATE 
            AND v.fecventa>=SYSDATE-8;
      

		SELECT SUM(dv.cantidad/7) INTO v_segunda 
        FROM VENTAS_V v, DETALLE_VENTAS_V dv
        WHERE v.codventa = dv.codventa 
          AND dv.codbarras=cb
          AND v.fecventa< SYSDATE-8 
          AND v.fecventa>=SYSDATE-15;
      
			
		SELECT SUM(dv.cantidad/7) INTO v_tercera
        FROM VENTAS_V v, DETALLE_VENTAS_V dv
        WHERE v.codventa = dv.codventa
          AND dv.codbarras=cb
          AND v.fecventa< SYSDATE-15 
          AND v.fecventa>=SYSDATE-22;



		SELECT SUM(dv.cantidad/7)  INTO v_cuarta
        FROM VENTAS_V v, DETALLE_VENTAS_V dv
        WHERE v.codventa = dv.codventa 
          AND dv.codbarras=cb
          AND v.fecventa< SYSDATE-22 
          AND v.fecventa>=SYSDATE-29;

			v_total:= (3*(NVL(v_primera,0)-NVL(v_segunda,0))+2*(NVL(v_segunda,0)-NVL(v_tercera,0))+(NVL(v_tercera,0)-NVL(v_cuarta,0)))/(6*7);
			
			return(v_total);

EXCEPTION
	WHEN OTHERS THEN
	    RAISE_APPLICATION_ERROR(-20001, 'Error en procedimiento gestion_pedidos.prediccion_pendiente');			

END prediccion_pendiente;







/*******************************************************************************
* FUNCTION mejor_oferta
* Argumentos: 
*   - cb: identificador del producto
*   - v_unidades_pedido: unidades totales del pedido
* Return:
*   - total
*******************************************************************************/
FUNCTION mejor_oferta (cb IN PRODUCTO.codbarras%TYPE, v_maxstock IN STOCK.maxstock%TYPE, v_minstock IN STOCK.minstock%TYPE, v_numstock IN STOCK.numstock%TYPE, v_unidades_pedido OUT NUMBER)
RETURN NUMBER AS

	v_id 				      NUMBER;
	v_precio 			    PRODUCTO.precio%TYPE;
	v_nummin			    NUMBER;
	v_uds 				    NUMBER;
	v_dias 				    NUMBER;
	v_minprecio 		  NUMBER:=NULL;
	v_minoferta 		  NUMBER;
	v_pendiente 		  NUMBER;
	v_media 			    NUMBER;
	v_prediccion 		  NUMBER:=0;
	--v_maxstock			  STOCK.maxstock%TYPE;
	--v_minstock			  STOCK.minstock%TYPE;
	--v_numstock			  STOCK.numstock%TYPE;
	v_espaciostock 		NUMBER;
	v_unidades 			  NUMBER:=0;
	v_perdidas			  NUMBER:=0;
	v_precio_producto PRODUCTO.precio%TYPE;
	v_coste_estimado	NUMBER;
	v_mejor_oferta		NUMBER;


CURSOR c1 IS 
			SELECT idoferta, precio_ud, nummin_ud, uds_paquete, numdias
        FROM OFERTAS_V
        WHERE codbarras = cb;

BEGIN 
			-- Obtencion de la media ponderada de las ventas del producto/dia en el ultimo mes
			v_media:=gestion_pedidos.prediccion_media(cb);

			-- Obtención del precio de venta del producto
			SELECT precio INTO v_precio_producto
        FROM PRODUCTO_V
        WHERE codBarras = cb;
					    
			-- Cálculo del espacio disponible en el almacen para el producto
			v_espaciostock:=(v_maxstock-v_numstock);
           
			-- Estimación de la pendiente de ventas
			v_pendiente:= prediccion_pendiente (cb);

			OPEN c1;
			
			-- Borrado de la tabla global temporary
			DELETE FROM temporal_ofertas_validas;
			
			-- Iteración sobre las ofertas para el producto
			LOOP 
				FETCH c1 INTO v_id, v_precio, v_nummin, v_uds, v_dias;
					EXIT WHEN c1%NOTFOUND;
				
					-- Estimación de las ventas que se van a producir durante la espera de la llegada del pedido          
					v_prediccion:=(v_media + (v_pendiente * (v_dias/2)))*v_dias;

					-- Cálculo del número de unidades del producto a pedir (se establece margen de error de un 20% en la predicción de ventas)
					-- Las unidades a pedir serán el espacio que haya en stock + las unidades que está previsto vender
					v_unidades:= TRUNC((v_espaciostock + (v_prediccion*0.80))/v_uds)*v_uds;
            
					-- En caso de que el número de unidades a pedir supere el máximo del stock -> pedir unidades máximas de stock (en múltiplo de paquete)
					IF(v_unidades>v_maxstock) THEN
              
						v_unidades:= TRUNC(v_maxstock/v_uds)*v_uds;
            
					END IF;

					-- Obtención de las pérdidas producidas por no disponer de stock del producto mientras se espera la llegada del pedido
					IF (v_prediccion > v_numstock) THEN

						v_perdidas:=(v_prediccion-v_numstock)*v_precio_producto;
					
					ELSE
					
						v_perdidas:=0;
					
					END IF;
									
					-- Obtención del coste estimado por unidad del producto
					v_coste_estimado:=(v_perdidas+v_precio*v_unidades)/v_unidades;
					
					-- Se inserta la oferta en la tabla temporal que almacena las ofertas válidas para el producto
					-- comprobando que se cumplen las restriciones que se piden
					IF(v_unidades>v_nummin AND v_unidades+v_numstock>v_minstock) THEN
						
						INSERT INTO temporal_ofertas_validas
              VALUES (v_id, v_coste_estimado, v_nummin, v_unidades);
					
					END IF;
					
			END LOOP;
		
			CLOSE c1;
      
      		-- Se selecciona la mejor oferta de la tabla temporal
			
			SELECT a.idoferta, a.numunidades INTO v_mejor_oferta, v_unidades_pedido			
        FROM temporal_ofertas_validas a
        WHERE a.coste_estimado=(SELECT MIN(b.coste_estimado)
                                FROM temporal_ofertas_validas b)
              AND v_unidades > a.nummin;

	RETURN (v_mejor_oferta);
	
END mejor_oferta;


END gestion_pedidos;
/

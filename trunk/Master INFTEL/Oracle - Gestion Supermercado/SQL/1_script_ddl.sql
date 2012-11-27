-- PARA ACTIVAR LAS HORAS, MINUTOS Y SEGUNDOS EN LA FECHA
ALTER SESSION SET nls_date_format = 'dd-mm-yyyy hh24:mi:ss';

/*******************************************************
--   BORRADO DE TABLAS SI EXISTEN
********************************************************/
DROP TABLE HISTORICO;
DROP TABLE DETALLE_VENTAS;
DROP TABLE VENTAS; 
DROP TABLE HISTORICO_PRECIOS;
DROP TABLE STOCK;
DROP TABLE PEDIDOS_REALIZADOS;
DROP TABLE OFERTAS;
DROP TABLE PROVEEDOR;
DROP TABLE PRODUCTO;
DROP TABLE DEPARTAMENTO;
DROP TABLE TIPOS_DEPARTAMENTO;


/*******************************************************
--   BORRADO DE VISTAS SI EXISTEN
*******************************************************/
DROP VIEW HISTORICO_V;
DROP VIEW DETALLE_VENTAS_V;
DROP VIEW VENTAS_V; 
DROP VIEW HISTORICO_PRECIOS_V;
DROP VIEW STOCK_V;
DROP VIEW PEDIDOS_REALIZADOS_V;
DROP VIEW OFERTAS_V;
DROP VIEW PROVEEDOR_V;
DROP VIEW PRODUCTO_V;
DROP VIEW DEPARTAMENTO_V;
DROP VIEW TIPOS_DEPARTAMENTO_V;


/*******************************************************
--  CREACION DE TABLAS
********************************************************/
CREATE TABLE TIPOS_DEPARTAMENTO
(
    id_tipo           NUMBER NOT NULL,        
    desctipo          VARCHAR2(10)
);


CREATE TABLE DEPARTAMENTO 
(
    numdept           NUMBER NOT NULL,        
    nomdept           VARCHAR2(50) NOT NULL,  
    telefono          CHAR(9),
    gasto             NUMBER,
    presupuesto       NUMBER,
    tipodepart        NUMBER NOT NULL  
);


CREATE TABLE HISTORICO
(
    anio              NUMBER(4) NOT NULL,  
    numdept           NUMBER NOT NULL,    
    gastoanual        NUMBER
);

CREATE TABLE VENTAS
(
    codventa          NUMBER NOT NULL,
    caja              NUMBER,
    fecventa          TIMESTAMP,
    forma_pago        CHAR(1), /*E=Efectivo,T=Tarjeta*/
    numtarjeta        NUMBER(16)    
);


CREATE TABLE PRODUCTO
(
    codbarras         NUMBER(13) NOT NULL,
    desproducto       VARCHAR2(50),
    precio            NUMBER,
    existencias       NUMBER,
    numdept           NUMBER NOT NULL
);

CREATE TABLE DETALLE_VENTAS
(
    codventa          NUMBER NOT NULL,
    codbarras         NUMBER(13) NOT NULL,
    cantidad          NUMBER,
    precio            NUMBER
);

CREATE TABLE HISTORICO_PRECIOS
(
    codbarras         NUMBER(13) NOT NULL,
    fecprecio         DATE NOT NULL,
    precio            NUMBER
);


CREATE TABLE STOCK
(
    codbarras         NUMBER(13) NOT NULL,
    minestanteria     NUMBER,
    maxestanteria     NUMBER,
    minstock          NUMBER,
    maxstock          NUMBER,
    numstock          NUMBER
);


CREATE TABLE PROVEEDOR
(
    cif               CHAR(9) NOT NULL,
    nomproveedor      VARCHAR2(50),
    telefono          NUMBER(9),
    direccion         VARCHAR2(250)
);


CREATE TABLE OFERTAS
(
    idoferta          NUMBER NOT NULL,
    cif               CHAR(9) NOT NULL,
    codbarras         NUMBER(13) NOT NULL,
    precio_ud         NUMBER,
    nummin_ud         NUMBER,
    uds_paquete       NUMBER,
    numdias           NUMBER
);

CREATE TABLE PEDIDOS_REALIZADOS
(
    idoferta          NUMBER NOT NULL,
    fecpedido         DATE,
    uds_solicitadas   NUMBER,
	recibido		  CHAR(1)
);

CREATE GLOBAL TEMPORARY TABLE temporal_ofertas_validas 
(
	idoferta 		NUMBER,
	coste_estimado 	NUMBER, 
	nummin 			NUMBER, 
	numunidades 	NUMBER
);

/*******************************************************
--  RESTRICCIONES
********************************************************/

-- TABLA: TIPOS_DEPARTAMENTO
--------------------------------------------------------
ALTER TABLE TIPOS_DEPARTAMENTO 
ADD CONSTRAINT pk_tipos_departamento
PRIMARY KEY (id_tipo);


-- TABLA: DEPARTAMENTO
--------------------------------------------------------
CREATE UNIQUE INDEX idx_nomdepartamento
ON DEPARTAMENTO (nomdept ASC);

ALTER TABLE DEPARTAMENTO 
ADD CONSTRAINT pk_departamento
PRIMARY KEY (numdept);

ALTER TABLE DEPARTAMENTO
ADD CONSTRAINT fk_Tipos_Departamentos 
FOREIGN KEY (tipodepart) 
REFERENCES TIPOS_DEPARTAMENTO(id_tipo);


-- TABLA: HISTORICO
--------------------------------------------------------
ALTER TABLE HISTORICO 
ADD CONSTRAINT pk_historico
PRIMARY KEY (anio,numdept);

ALTER TABLE HISTORICO
ADD CONSTRAINT fk_Departamento_Historico 
FOREIGN KEY (numdept) 
REFERENCES DEPARTAMENTO(numdept);


-- TABLA: VENTAS
--------------------------------------------------------
ALTER TABLE VENTAS 
ADD CONSTRAINT pk_ventas
PRIMARY KEY (codventa);


-- TABLA: PRODUCTO
--------------------------------------------------------
ALTER TABLE PRODUCTO 
ADD CONSTRAINT pk_producto
PRIMARY KEY (codbarras);

ALTER TABLE PRODUCTO
ADD CONSTRAINT fk_Departamento_Producto 
FOREIGN KEY (numdept) 
REFERENCES DEPARTAMENTO(numdept);


-- TABLA: DETALLE_VENTAS
--------------------------------------------------------
ALTER TABLE DETALLE_VENTAS 
ADD CONSTRAINT pk_detalle_ventas
PRIMARY KEY (codventa, codbarras);

ALTER TABLE DETALLE_VENTAS
ADD CONSTRAINT fk_Ventas_Detalle_Ventas
FOREIGN KEY (codventa) 
REFERENCES VENTAS(codventa);

ALTER TABLE DETALLE_VENTAS
ADD CONSTRAINT fk_Producto_Detalle_Ventas 
FOREIGN KEY (codbarras) 
REFERENCES PRODUCTO(codbarras);


-- TABLA: HISTORICO_PRECIOS
--------------------------------------------------------
ALTER TABLE HISTORICO_PRECIOS 
ADD CONSTRAINT pk_historico_precios
PRIMARY KEY (codbarras, fecprecio);

ALTER TABLE HISTORICO_PRECIOS
ADD CONSTRAINT fk_Producto_Historico_Precios 
FOREIGN KEY (codbarras) 
REFERENCES PRODUCTO(codbarras);


-- TABLA: STOCK
--------------------------------------------------------
ALTER TABLE STOCK 
ADD CONSTRAINT pk_stock
PRIMARY KEY (codbarras);

ALTER TABLE STOCK
ADD CONSTRAINT fk_Producto_Stock 
FOREIGN KEY (codbarras) 
REFERENCES PRODUCTO(codbarras);


-- TABLA: PROVEEDOR
--------------------------------------------------------
ALTER TABLE PROVEEDOR 
ADD CONSTRAINT pk_proveedor
PRIMARY KEY (cif);


-- TABLA: OFERTAS
--------------------------------------------------------
ALTER TABLE OFERTAS 
ADD CONSTRAINT pk_idoferta
PRIMARY KEY (idoferta);

ALTER TABLE OFERTAS
ADD CONSTRAINT fk_Proveedor_Ofertas 
FOREIGN KEY (cif) 
REFERENCES PROVEEDOR(cif);

ALTER TABLE OFERTAS
ADD CONSTRAINT fk_Producto_Ofertas 
FOREIGN KEY (codbarras) 
REFERENCES PRODUCTO(codbarras);


-- TABLA: PEDIDOS_REALIZADOS
--------------------------------------------------------
ALTER TABLE PEDIDOS_REALIZADOS 
ADD CONSTRAINT pk_pedidos_realizados
PRIMARY KEY (idoferta, fecpedido);

ALTER TABLE PEDIDOS_REALIZADOS
ADD CONSTRAINT fk_Oferta_Pedidos_Realizados 
FOREIGN KEY (idoferta) 
REFERENCES OFERTAS(idoferta);



/*******************************************************
--  CREACION DE VISTAS PARA MODELO 3 CAPAS
*******************************************************/

CREATE OR REPLACE VIEW DEPARTAMENTO_V AS
SELECT *
FROM DEPARTAMENTO;

CREATE OR REPLACE VIEW DETALLE_VENTAS_V AS
SELECT *
FROM DETALLE_VENTAS;

CREATE OR REPLACE VIEW HISTORICO_PRECIOS_V AS
SELECT *
FROM HISTORICO_PRECIOS;

CREATE OR REPLACE VIEW OFERTAS_V AS
SELECT *
FROM OFERTAS;

CREATE OR REPLACE VIEW PEDIDOS_REALIZADOS_V AS
SELECT *
FROM PEDIDOS_REALIZADOS;

CREATE OR REPLACE VIEW PRODUCTO_V AS
SELECT *
FROM PRODUCTO;

CREATE OR REPLACE VIEW STOCK_V AS
SELECT *
FROM STOCK;

CREATE OR REPLACE VIEW TIPOS_DEPARTAMENTO_V AS
SELECT *
FROM TIPOS_DEPARTAMENTO;

CREATE OR REPLACE VIEW VENTAS_V AS
SELECT *
FROM VENTAS;


/*******************************************************
--  CREACION DE SECUENCIAS PARA AUTOINCREMENTALES
********************************************************/


CREATE SEQUENCE id_tipodepart_seq
  INCREMENT BY 1
  START WITH 1
  NOMAXVALUE
  NOCYCLE
  CACHE 10;

CREATE OR REPLACE TRIGGER BI_TIPOS_DEPARTAMENTO
  BEFORE INSERT ON TIPOS_DEPARTAMENTO
  FOR EACH ROW
BEGIN
  IF :new.id_tipo IS NULL THEN
      SELECT  id_tipodepart_seq.NEXTVAL
      INTO    :new.id_tipo
      FROM    DUAL;
  END IF;
END;
/


CREATE SEQUENCE codventa_seq
  INCREMENT BY 1
  START WITH 1
  NOMAXVALUE
  NOCYCLE
  CACHE 10;

CREATE OR REPLACE TRIGGER BI_VENTAS
  BEFORE INSERT ON VENTAS
  FOR EACH ROW
BEGIN
  IF :new.codventa IS NULL THEN
      SELECT  codventa_seq.NEXTVAL
      INTO    :new.codventa
      FROM    DUAL;
  END IF;
END;
/



CREATE SEQUENCE idoferta_seq
  INCREMENT BY 1
  START WITH 1
  NOMAXVALUE
  NOCYCLE
  CACHE 10;

CREATE OR REPLACE TRIGGER BI_OFERTAS
  BEFORE INSERT ON OFERTAS
  FOR EACH ROW
BEGIN
  IF :new.idoferta IS NULL THEN
      SELECT  idoferta_seq.NEXTVAL
      INTO    :new.idoferta
      FROM    DUAL;
  END IF;
END;
/

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias`
--

CREATE TABLE `categorias` (
  `id` int(3) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) NOT NULL,
  `posicion` int(3) NOT NULL,
  `creado` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id` bigint(7) NOT NULL AUTO_INCREMENT,
  `marca` varchar(50) NOT NULL,
  `descripcion` varchar(100) NOT NULL,
  `formato` varchar(10) ,
  `categoria` int(2) NOT NULL,
  `subcategoria` int(2) NOT NULL,
  `subsubcategoria` int(2) NOT NULL,
  `codigo_barras` varchar(13) DEFAULT NULL,
  `imagen_src` varchar(100) ,
  `precio_mercadona` decimal(5,2),
  `precio_carrefour` decimal(5,2),
  `precio_hipercor` decimal(5,2),
  `precio_corteIngles` decimal(5,2),
  `precio_relativo_mercadona` varchar(20) ,
  `precio_relativo_carrefour` varchar(20) ,
  `precio_relativo_hipercor` varchar(20) ,
  `precio_relativo_corteIngles` varchar(20) ,
  `creado` date,
  `codigo_creado` date DEFAULT NULL,
  `oferta_mercadona` varchar(100) ,
  `oferta_carrefour` varchar(100) ,
  `oferta_hipercor` varchar(100) ,
  `oferta_corteIngles` varchar(100) ,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `subcategorias`
--

CREATE TABLE `subcategorias` (
  `id` int(3) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) NOT NULL,
  `posicion` int(3) NOT NULL,
  `creado` date,
  `categoria` int(3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `subsubcategorias`
--

CREATE TABLE `subsubcategorias` (
  `id` int(3) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) NOT NULL,
  `posicion` int(3) NOT NULL,
  `creado` date,
  `categoria` int(3) NOT NULL,
  `subcategoria` int(3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- TRIGGERS
--
--
-- (Evento) desencadenante `categorias`
--
DROP TRIGGER IF EXISTS `categoria_creado_insert_auto`;
DELIMITER //
CREATE TRIGGER `categoria_creado_insert_auto` BEFORE INSERT ON `categorias`
 FOR EACH ROW BEGIN

SET NEW.creado = SYSDATE();
   
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `categoria_creado_update_auto`;
DELIMITER //
CREATE TRIGGER `categoria_creado_update_auto` BEFORE UPDATE ON `categorias`
 FOR EACH ROW BEGIN

SET NEW.creado = SYSDATE();
 

END
//
DELIMITER ;

--
-- (Evento) desencadenante `productos`
--
DROP TRIGGER IF EXISTS `producto_fecha_creado_insert_auto`;
DELIMITER //
CREATE TRIGGER `producto_fecha_creado_insert_auto` BEFORE INSERT ON `productos`
 FOR EACH ROW BEGIN

SET NEW.creado = SYSDATE();
    IF NEW.codigo_barras IS NOT NULL THEN
        SET NEW.codigo_creado = SYSDATE();
    END IF;
		

END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `producto_fecha_creado_update_auto`;
DELIMITER //
CREATE TRIGGER `producto_fecha_creado_update_auto` BEFORE UPDATE ON `productos`
 FOR EACH ROW BEGIN

SET NEW.creado = SYSDATE();
    IF NEW.codigo_barras IS NOT NULL THEN
        SET NEW.codigo_creado = SYSDATE();
    END IF;
		

END
//
DELIMITER ;

--
-- (Evento) desencadenante `subcategorias`
--
DROP TRIGGER IF EXISTS `subcategoria_creado_insert_auto`;
DELIMITER //
CREATE TRIGGER `subcategoria_creado_insert_auto` BEFORE INSERT ON `subcategorias`
 FOR EACH ROW BEGIN

SET NEW.creado = SYSDATE();
   
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `subcategoria_creado_update_auto`;
DELIMITER //
CREATE TRIGGER `subcategoria_creado_update_auto` BEFORE UPDATE ON `subcategorias`
 FOR EACH ROW BEGIN

SET NEW.creado = SYSDATE();
 

END
//
DELIMITER ;

--
-- (Evento) desencadenante `subsubcategorias`
--
DROP TRIGGER IF EXISTS `subsubcategoria_creado_insert_auto`;
DELIMITER //
CREATE TRIGGER `subsubcategoria_creado_insert_auto` BEFORE INSERT ON `subsubcategorias`
 FOR EACH ROW BEGIN

SET NEW.creado = SYSDATE();
   
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `subsubcategoria_creado_update_auto`;
DELIMITER //
CREATE TRIGGER `subsubcategoria_creado_update_auto` BEFORE UPDATE ON `subsubcategorias`
 FOR EACH ROW BEGIN

SET NEW.creado = SYSDATE();
 

END
//
DELIMITER ;


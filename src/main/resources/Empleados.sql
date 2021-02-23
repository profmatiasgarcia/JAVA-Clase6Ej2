-- phpMyAdmin SQL Dump
-- version 4.8.0
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 14-10-2018 a las 06:40:30
-- Versión del servidor: 10.1.31-MariaDB
-- Versión de PHP: 7.1.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `Empleados`
--

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `contactosXId` (IN `in_id` INT)  NO SQL
select * from DatosContacto where idPersona = in_id$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insertarContacto` (IN `in_nom` VARCHAR(40), IN `in_ape` VARCHAR(40), IN `in_mail` VARCHAR(100), IN `in_tel` VARCHAR(20))  NO SQL
INSERT INTO `DatosContacto`(`idPersona`,`Nombre`, `Apellido`, `Email`, `Telefono`) VALUES (null,in_nom,in_ape,in_mail,in_tel)$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `mostrarContactos` ()  NO SQL
select * from DatosContacto$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `DatosContacto`
--

CREATE TABLE `DatosContacto` (
  `idPersona` int(11) NOT NULL,
  `Nombre` text COLLATE utf8_spanish_ci NOT NULL,
  `Apellido` text COLLATE utf8_spanish_ci NOT NULL,
  `Email` text COLLATE utf8_spanish_ci NOT NULL,
  `Telefono` text COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `DatosContacto`
--

INSERT INTO `DatosContacto` (`idPersona`, `Nombre`, `Apellido`, `Email`, `Telefono`) VALUES
(1, 'Matias', 'Garcia', 'matias@mail.com', '011 4589-2564'),
(2, 'Angie', 'Lione', 'malione@mail.com', '011 5645-5464'),
(3, 'Julieta', 'Gonzalez', 'julygon@hotmail.com', '011 2344-4321'),
(4, 'Cristina', 'Perez', 'cp@gmail.com', '011 15 5896-8321'),
(5, 'Gustavo', 'Vittola', 'vittolaGus@gmail.com', '011 6589-8954'),
(6, 'Laura', 'Arriola', 'laurita33@hotmail.com', '011 5698-5232');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `DatosContacto`
--
ALTER TABLE `DatosContacto`
  ADD PRIMARY KEY (`idPersona`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `DatosContacto`
--
ALTER TABLE `DatosContacto`
  MODIFY `idPersona` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

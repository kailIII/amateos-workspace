LIBRARY ieee;
USE ieee.std_logic_1164.ALL;
USE ieee.std_logic_arith;
USE ieee.std_logic_unsigned;

ENTITY generadorFreqs IS

	PORT(

		seleccionador: IN std_logic_vector(3 DOWNTO 0); --Variable para seleccionar la frecuencia a entrega
		clk: IN BIT;	--Definimos el reloj del cual tomaremos la referencia
		reloj_salida: OUT BIT     --Definimos lo que será la salida de nuestro reloj a la frecuencia selecionada             
	);

END generadorFreqs;

ARCHITECTURE decodificador_frecuencias OF generadorFreqs IS

	COMPONENT display
		PORT(
			entrada: IN std_logic_vector(3 DOWNTO 0);
			salidaD1: OUT std_logic_vector(6 DOWNTO 0);
			salidaD2: OUT std_logic_vector(6 DOWNTO 0)
		);
	END COMPONENT;
			SIGNAL contador_pulsos: std_logic_vector (15 DOWNTO 0);
			SIGNAL num_pulsos_max: std_logic_vector (15 DOWNTO 0);  -- Variable con la que comparar el contador para que se haga el flanco de bajada
			SIGNAL salidaD1: std_logic_vector(6 DOWNTO 0);
			SIGNAL salidaD2: std_logic_vector(6 DOWNTO 0);
		BEGIN			
		 	
		proceso_seleccion: PROCESS (seleccionador) IS   --Definimos un proceso que se encarga de asignar el valor al limite de pulsos para cada frecuencia
			BEGIN

			CASE (seleccionador) IS

				WHEN "0000" => num_pulsos_max <= "00000000000000";
				salidaD1<="0000001";salidaD2 <= "0000001";
				WHEN "0001" => num_pulsos_max <= "11000100101100";--12588
				salidaD1<="1001111";salidaD2 <= "0000001";
				WHEN "0010" => num_pulsos_max <= "1100010010110";--6294
				salidaD1<="0010011";salidaD2 <= "0000001";
				WHEN "0011" => num_pulsos_max <= "1000001100100";--4196
				salidaD1<="0000110";salidaD2 <= "0000001";
				WHEN "0100" => num_pulsos_max <= "110001001011";--3147
				salidaD1<="1001101";salidaD2 <= "0000001";
				WHEN "0101" => num_pulsos_max <= "100111010110";--2518
				salidaD1<="0100100";salidaD2 <= "0000001";
				WHEN "0110" => num_pulsos_max <= "100000110010"; --2098
				salidaD1<="0100000";salidaD2 <= "0000001";
				WHEN "0111" => num_pulsos_max <= "11100000110";--1798
				salidaD1<="0001111";salidaD2 <= "0000001";
				WHEN "1000" => num_pulsos_max <= "11000100110";--1574
				salidaD1<="0000000";salidaD2 <= "0000001";
				WHEN "1001" => num_pulsos_max <= "10101110111";--1399
				salidaD1<="0001100";salidaD2 <= "0000001";
				WHEN "1010" => num_pulsos_max <= "10011101011";--1259
				salidaD1<="0000001";salidaD2 <= "1001111";
				WHEN "1011" => num_pulsos_max <= "10001111000";--1144
				salidaD1<="1001111";salidaD2 <= "1001111";
				WHEN "1100" => num_pulsos_max <= "10000011001";--1049
				salidaD1<="0010011";salidaD2 <= "1001111";
				WHEN "1101" => num_pulsos_max <= "1111001000";--968
				salidaD1<="0000110";salidaD2 <= "1001111";
				WHEN "1110" => num_pulsos_max <= "1110000011";--899
				salidaD1<="1001101";salidaD2 <= "1001111";
				WHEN "1111" => num_pulsos_max <= "1101000111";--839
				salidaD1<="0100100";salidaD2 <= "1001111";
				WHEN OTHERS => num_pulsos_max <= "000000000000000";

			END CASE;

		END PROCESS;

		D1:display PORT MAP(seleccionador,salidaD1,salidaD2);
		reloj_salida <= '1';  --Inicializamos el estado del reloj de salida a uno

		proceso_generador: PROCESS (clk)   --Como es un proceso sincrono la lista de sensibilidad es el reloj

			BEGIN

			IF clk'event AND clk='1' THEN
				contador_pulsos <= contador_pulsos + '000000000000001';
			END IF;

			IF contador_pulsos = num_pulsos_max THEN
				reloj_salida <= NOT(reloj_salida); --Realizamos el flanco de bajada
				contador_pulsos <= '0';  --Ponemos el contador a cero para que vuelva a empezar a contar de nuevo
			END IF;

		END PROCESS;		

END decodificador_frecuencias;
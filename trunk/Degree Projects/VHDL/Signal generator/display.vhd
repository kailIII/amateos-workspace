LIBRARY ieee;
USE ieee.std_logic_1164.ALL;

ENTITY display IS

	PORT(
	
		entrada: IN std_logic_vector(3 DOWNTO 0);
		salidaD1: OUT std_logic_vector(6 DOWNTO 0);
		salidaD2: OUT std_logic_vector(6 DOWNTO 0)
	);
	
END display;

ARCHITECTURE decodificador OF display IS

BEGIN

WITH entrada SELECT
			   --abcdefg
	salidaD1 <= "0000001" WHEN "0000",
				"1001111" WHEN "0001",
				"0010010" WHEN "0010",
				"0000110" WHEN "0011",
				"1001100" WHEN "0100",
				"0100100" WHEN "0101",
				"0100000" WHEN "0110",
				"0001111" WHEN "0111",
				"0000000" WHEN "1000",
				"0001100" WHEN "1001",
				"0000001" WHEN "1010",
				"1001111" WHEN "1011",
				"0010010" WHEN "1100",
				"0000110" WHEN "1101",
				"1001100" WHEN "1110",
				"0100100" WHEN "1111",
				"0000000" WHEN OTHERS;
				
	WITH entrada SELECT
			   --abcdefg	
	salidaD2 <= "0000001" WHEN "0000",
				"0000001" WHEN "0001",
				"0000001" WHEN "0010",
				"0000001" WHEN "0011",
				"0000001" WHEN "0100",
				"0000001" WHEN "0101",
				"0000001" WHEN "0110",
				"0000001" WHEN "0111",
				"0000001" WHEN "1000",
				"0000001" WHEN "1001",
				"1001111" WHEN "1010",
				"1001111" WHEN "1011",
				"1001111" WHEN "1100",
				"1001111" WHEN "1101",
				"1001111" WHEN "1110",
				"1001111" WHEN "1111",
				"0000000" WHEN OTHERS;
	
END decodificador;
	
package br.ce.wcaquino.servicos;

import org.junit.Test;
import org.mockito.Mockito;

public class CalculadoraMockTest {

	@Test
	public void teste() {
	Calculadora calc= Mockito.mock(Calculadora.class);
	
	
//	Mockito.when(calc.soma(5, 2)).thenReturn(10);
	Mockito.when(calc.soma(Mockito.eq(3), Mockito.anyInt())).thenReturn(8); //soma de 6 a qualquer num, retorna 8

	//Mockito.when(calc.soma(Mockito.anyInt(), Mockito.anyInt())).thenReturn(8); //soma de 6 a qualquer num, retorna 8
	
	//System.out.println("Valor é"+calc.soma(5, 2));
	//System.out.println("Valor é"+calc.soma(6, 3));
	System.out.println("Valor é "+calc.soma(3, 76));

	}
}

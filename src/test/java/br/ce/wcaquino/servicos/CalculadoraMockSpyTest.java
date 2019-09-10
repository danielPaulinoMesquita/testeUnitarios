package br.ce.wcaquino.servicos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

/*
 * spy não serve para interfaces
 */

public class CalculadoraMockSpyTest {
	
	@Mock
	private Calculadora calcMock;
	
	@Spy
	private Calculadora calcSpy;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	/*
	 * Quando mockar alguma classe, 
	 * e quiser testar algum método, é necessário
	 * criar um retorno com when e thenReturn
	 */
	
	@Test
	public void deveMostrarDiferencaMockSpy() {
	//	Mockito.when(calcMock.soma(1, 2)).thenCallRealMethod();
		
		Mockito.doReturn(3).when(calcMock).soma(1, 2);
		System.out.println("soma com MOCK: "+calcMock.soma(1,2));
		
		
		Mockito.when(calcSpy.soma(1, 2)).thenReturn(4);
		System.out.println("soma com SPY: "+calcSpy.soma(1,5));
		
		System.out.println("Mock");
		calcMock.imprime();
		System.out.println("Spy");
		calcSpy.imprime();


	}
	

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

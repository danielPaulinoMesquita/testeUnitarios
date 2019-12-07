package br.ce.wcaquino.servicos;

import org.junit.*;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class CalculadoraTest {

	public static StringBuffer ordem= new StringBuffer();
	
	private Calculadora calculadora;
	
	@Before
	public void setup() {
		calculadora = new Calculadora();
		System.out.println("Iniciando ..");
		ordem.append("1");
	}

	@After
	public void tearDown(){
		System.out.println("Finalizando ..");
	}

	@AfterClass
	public static void tearDownClass(){
		System.out.println(ordem);
	}

	@Test
	public void calculaSomaDoisNumeros() {
		// Cenário
		int i = 5;
		int j = 3;

		// Ação
		int resultado = calculadora.soma(i, j);

		// Verificação
		Assert.assertEquals(8, resultado);
	}

	@Test
	public void calculaSubtrairDoisNumeros() {
		// Cenário
		int i = 5;
		int j = 3;

		// Ação
		int resultado = calculadora.subtrair(i, j);

		// Verificação
		Assert.assertEquals(2, resultado);
	}

	@Test
	public void calculaDividirDoisNumeros() throws NaoPodeDividirPorZeroException {
		// Cenário
		int i = 6;
		int j = 3;

		// Ação
		int resultado = calculadora.dividir(i, j);

		// Verificação
		Assert.assertEquals(2, resultado);
	}

	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLançarExceptionDividirPorZero() throws NaoPodeDividirPorZeroException {
		// Cenário
		int i = 10;
		int j = 0;

		// Ação
		int resultado = calculadora.dividir(i, j);

		// Verificação
		Assert.assertEquals(2, resultado);
	}

}

package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class CalculadoraTest {
	
	private Calculadora calculadora;
	
	@Before
	public void setup() {
		calculadora = new Calculadora();
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

package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

public class CalculadoraTest {

	@Test
	public void calculaSomaDoisNumeros() {
		//Cenário
		int i=5;
		int j=3;
		Calculadora calculadora= new Calculadora();
		
		//Ação
		int resultado= calculadora.soma(i,j);
		
		
		//Verificação
		Assert.assertEquals(8,resultado);
	}
	
}

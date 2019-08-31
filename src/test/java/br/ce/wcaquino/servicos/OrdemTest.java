package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

public class OrdemTest {
	
	static int i = 0;

	public void inicia() {
		i++;
	}
	
	//Esse metodo depende do outro pra rodar de forma correta, 
	// ou seja ele deve ser o segundo a ser executado
	public void verifica() { 
		Assert.assertEquals(1,i);
	}
	
	@Test
	public void testeGeral() {
		inicia();
		verifica();
		
	}
}

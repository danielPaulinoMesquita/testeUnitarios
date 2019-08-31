package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/*
 * @FixMethodOrder(MethodSorters.NAME_ASCENDING) ordena os testes por ordem alfabetica
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdemTest {
	
	static int i = 0;

	@Test
	public void inicia() {
		i++;
	}
	
	// Esse metodo 'verifica' depende do outro pra rodar de forma correta, 
	// ou seja ele deve ser o segundo a ser executado
	
	@Test
	public void verifica() { 
		Assert.assertEquals(1,i);
	}
	
	
}

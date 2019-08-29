package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class Asserts {
	
	@Test
	public void Assert() {
		// Asserts
		Assert.assertTrue(true);
		Assert.assertTrue(!false);
		Assert.assertTrue(true);

		Assert.assertEquals(4.434, 4.433, 0.01);// precisa de uma margem de erro, utiliza-se o delta no final
		Assert.assertEquals(Math.PI, 3.14, 0.01);// PI é infinito, por isso a necessidade de comparar com delta

		// Asserts Comparar primitivos;
		int i = 3;
		Integer i2 = 7;
		Assert.assertNotEquals(i, i2.intValue());

		Assert.assertEquals("vola", "Vola".toLowerCase());
		Assert.assertTrue("vola".equalsIgnoreCase("Vola"));
		Assert.assertTrue("vola".startsWith("vol"));

		// Asserts comparar Objetos
		Usuario u1 = new Usuario("usuario 1");
		Usuario u2 = new Usuario("usuario 1");
		Usuario nl = null;

		Assert.assertEquals(u1, u2);
		Assert.assertNotSame(u1, u2);

		u2 = u1;
		Assert.assertSame(u1, u2);

		Assert.assertNull(nl);
		Assert.assertNotNull(nl = u2);

		// Possivel passar uma mensagem como string caso der erro.
		// Assert.assertNotSame("Erro na comparação!!!",u1,u2);
	}
}

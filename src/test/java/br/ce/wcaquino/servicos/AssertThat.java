package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class AssertThat {

	public void assertThat() {
		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 4.0);

		// acao
		Locacao locacao = service.alugarFilme(usuario, filme);

		// Assert that
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(4.0));
		Assert.assertThat(locacao.getValor(), not(9.0));

		assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}
}

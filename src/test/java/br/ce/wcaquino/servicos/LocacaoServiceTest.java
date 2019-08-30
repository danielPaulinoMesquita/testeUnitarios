package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void teste() {
		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 1, 4.0);

		// acao
		Locacao locacao;
		try {
			locacao = service.alugarFilme(usuario, filme);
			// Destaca os errors com error(O esperado dos dois parâmetros é definido no
			// ultimo param que é is())
			error.checkThat(locacao.getValor(), is(equalTo(4.0)));
			error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
			error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail("Lançado a excessão");
		}

	}

	// Exception específica
	@Test(expected = FilmeSemEstoqueException.class)
	public void testeFilmeSemEstoque() throws Exception {
		LocacaoService service = new LocacaoService();
		Usuario usuario2 = new Usuario("Usuario 1");
		Filme filme2 = new Filme("Filme 1", 0, 4.0);
		service.alugarFilme(usuario2, filme2);
	}

	@Test
	public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {
		// Cenário
		LocacaoService service = new LocacaoService();
		Filme filme2 = new Filme("Filme 1", 2, 4.0);
		Usuario usuario = new Usuario("Usuario 1");

		// Ação
		try {
			service.alugarFilme(null, filme2);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuário Vazio"));
		}
	}

	@Test
	public void testLocacao_filmeVazio() throws FilmeSemEstoqueException {
		// Cenário
		LocacaoService service = new LocacaoService();
		Filme filme2 = new Filme("Filme 1", 2, 4.0);
		Usuario usuario = new Usuario("Usuario 1");

		// Ação
		try {
			service.alugarFilme(usuario, null);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Filme Vazio"));
		}
	}

	@Test
	public void testLocacao_filmeVazio2() throws FilmeSemEstoqueException, LocadoraException {
		// Cenário
		LocacaoService service = new LocacaoService();
		Filme filme2 = new Filme("Filme 1", 2, 4.0);
		Usuario usuario = new Usuario("Usuario 1");

		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme Vazio");
		
		// Ação
		service.alugarFilme(usuario, null);

	}

}
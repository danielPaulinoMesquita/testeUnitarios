package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
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

	LocacaoService service;

	static int i;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	// É usado o before em todos os testes
	@Before
	public void setup() {
		service = new LocacaoService();
	

	}

	@After
	public void tearDown() {		
	}

	@BeforeClass
	public static void beforeClass() {
	}

	@AfterClass
	public static void tearDownClass() {
		
	}

	@Test
	public void teste() {
		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0),
				new Filme("Filme 2", 1, 4.0));
		// acao
		Locacao locacao;
		try {
			locacao = service.alugarFilme(usuario, filmes);
			// Destaca os errors com error(O esperado dos dois parâmetros é definido no
			// ultimo param que é is())
			error.checkThat(locacao.getValor(), is(equalTo(8.0)));
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
		Usuario usuario2 = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 4.0),
				new Filme("Filme 2", 0, 4.0));
		
		
		service.alugarFilme(usuario2, filmes);
	}

	@Test
	public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {
		// Cenário
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0),
				new Filme("Filme 2", 1, 4.0));		Usuario usuario = new Usuario("Usuario 1");

		// Ação
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuário Vazio"));
		}
	}

	@Test
	public void testLocacao_filmeVazio() throws FilmeSemEstoqueException {
		// Cenário
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 4.0),
				new Filme("Filme 2", 0, 4.0));
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
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 4.0),
				new Filme("Filme 2", 0, 4.0));
		Usuario usuario = new Usuario("Usuario 1");

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme Vazio");

		// Ação
		service.alugarFilme(usuario, null);

	}

}
package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.LocacaoBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	@InjectMocks
	private LocacaoService service;

	@Mock
	private SPCService spc;

	@Mock
	private LocacaoDAO dao;

	@Mock
	private EmailService email;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	// É usado o before em todos os testes
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);	
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
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().comValor(6.0).agora(),
				FilmeBuilder.umFilme().comValor(7.0).agora());
		// acao
		Locacao locacao;
		try {
			locacao = service.alugarFilme(usuario, filmes);
			// Destaca os errors com error(O esperado dos dois parâmetros é definido no
			// ultimo param que é is())
			error.checkThat(locacao.getValor(), is(equalTo(13.0)));
			error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
			// error.checkThat(isMesmaData(locacao.getDataRetorno(),
			// DataUtils.obterDataComDiferencaDias(1)), is(true));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail("Lançado a excessão");
		}

	}

	// Exception específica
	@Test(expected = FilmeSemEstoqueException.class)
	public void deveDarExceptionFilmeSemEstoque1() throws Exception {
		Usuario usuario2 = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().semEstoque().agora(),
				FilmeBuilder.umFilme().semEstoque().agora());

		service.alugarFilme(usuario2, filmes);
	}

	@Test
	public void deveDarLocadoraExceptionUsuarioVazio() throws FilmeSemEstoqueException {
		// Cenário
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora(), FilmeBuilder.umFilme().agora());
		Usuario usuario = UsuarioBuilder.umUsuario().agora();

		// Ação
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuário Vazio"));
		}
	}

	@Test
	public void deveDarExceptionFilmeSemEstoque2() throws FilmeSemEstoqueException {
		// Cenário
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().semEstoque().agora(),
				FilmeBuilder.umFilme().semEstoque().agora());
		Usuario usuario = UsuarioBuilder.umUsuario().agora();

		// Ação
		try {
			service.alugarFilme(usuario, null);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Filme Vazio"));
		}
	}

	@Test
	public void deveDarExceptionFilmeNull() throws FilmeSemEstoqueException, LocadoraException {
		// Cenário
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 4.0), new Filme("Filme 2", 0, 4.0));
		Usuario usuario = UsuarioBuilder.umUsuario().agora();

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme Vazio");

		// Ação
		service.alugarFilme(usuario, null);

	}

	@Test
	public void devePagar75pcNoFilme() throws FilmeSemEstoqueException, LocadoraException {

		// Cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 1, 4.0),
				new Filme("Filme 3", 2, 4.0));

		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// verficacao
		assertThat(resultado.getValor(), is(11.0));
	}

	@Test
	public void devePagar50pcNoFilme() throws FilmeSemEstoqueException, LocadoraException {

		// Cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 1, 4.0),
				new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0));

		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// verficacao
		assertThat(resultado.getValor(), is(13.0));
	}

	@Test
	public void devePagar25pcNoFilme() throws FilmeSemEstoqueException, LocadoraException {

		// Cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 1, 4.0),
				new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0));

		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// verficacao
		assertThat(resultado.getValor(), is(14.0));
	}

	@Test
	public void devePagar100pcNoFilme() throws FilmeSemEstoqueException, LocadoraException {

		// Cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 1, 4.0),
				new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0),
				new Filme("Filme 6", 2, 4.0));

		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// verficacao
		assertThat(resultado.getValor(), is(14.0));
	}

	@Test
	// @Ignore
	public void naoDevolverFimesNoDomingo() throws FilmeSemEstoqueException, LocadoraException {
		// ESSE TESTE VAI SER IGNORADO POR CAUSA DA ANOTAÇÃO @Ignore

		// Cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0));

		// Ação
		Locacao retorno = service.alugarFilme(usuario, filmes);
		boolean ehretorno = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.SUNDAY);
		Assert.assertFalse(ehretorno);

		Assume.assumeFalse(DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.SUNDAY));

//		assertThat(retorno.getDataRetorno(), caiEm(Calendar.TUESDAY));
//		assertThat(retorno.getDataRetorno(), caiNaSegunda());
		// assertThat(retorno.getDataRetorno(), ehHoje());
		// assertThat(retorno.getDataRetorno(), ehHojeDiferencaDias(1));

	}

	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws FilmeSemEstoqueException {
		// Cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("User 2").agora();

		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		Mockito.when(spc.possuiNegativacao(usuario)).thenReturn(true);
		// Mockito.when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

//		exception.expect(LocadoraException.class);
//		exception.expectMessage("Usuário Negativado");

		// Ação
		try {
			service.alugarFilme(usuario, filmes);

			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuário Negativado"));
		}

		// Verificacao
		Mockito.verify(spc).possuiNegativacao(usuario);
	}

	@Test
	public void deveEnviarEmailParaLocacaoesAtrasados() {
		// cenário

		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario 2").agora();

		// Aqui seria a simulação do resultado de um resultSet do repository ou dao
		List<Locacao> locacoes = Arrays.asList(LocacaoBuilder.umLocacao().atrasado().comUsuario(usuario).agora(),
				LocacaoBuilder.umLocacao().comUsuario(usuario2).agora());

		// Aqui indicamos quen o resultSet de uma pseudo consulta no banco de dados é o
		// locacoes criado anteriormente
		// Usamos mockito para implementar essa simulação de resultado do repository ou
		// DAO
		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

		// acao
		service.notificarAtrasos();

		// verificar atrasos(Aqui será verificado se de fato o service chamou o
		// notificarAtraso do emailService)
		Mockito.verify(email).notificarAtraso(usuario);
		Mockito.verify(email, times(1)).notificarAtraso(Mockito.any(Usuario.class));// no minimo uma vez do tipo usuário
		Mockito.verify(email, Mockito.atLeastOnce()).notificarAtraso(usuario);// pelo menos uma vez
		Mockito.verify(email, Mockito.atLeast(1)).notificarAtraso(usuario);// no minimo uma vez
		Mockito.verify(email, times(0)).notificarAtraso(usuario2);

		Mockito.verify(email, Mockito.never()).notificarAtraso(usuario2);// Outra forma de verificar e constatar que não
																			// houve invocações
		Mockito.verifyNoMoreInteractions(email);
		// Mockito.verifyZeroInteractions(spc);
		// Mockito.verifyZeroInteractions(email);

	}
}
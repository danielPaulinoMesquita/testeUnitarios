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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.LocacaoBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.utils.DataUtils;

/*
 * o PowerMockito configurado só vai modificar as classes/funcoes e metodos 
 * das classes dentro do Prepare for test 
 * ex: @PrepareForTest({LocacaoService.class,DataUtils.class})
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(LocacaoService.class)
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
		service = PowerMockito.spy(service);
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
	public void naoDevolverFimesNoDomingo() throws Exception {
		// ESSE TESTE VAI SER IGNORADO POR CAUSA DA ANOTAÇÃO @Ignore

		// Cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0));

		// usando power mock para Date()
		// o Date configurado só vai modificar as datas do locacao service
//		PowerMockito.whenNew(Date.class).withNoArguments()
//		.thenReturn(DataUtils.obterData(15, 9, 2019));

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 15);
		calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
		calendar.set(Calendar.YEAR, 2019);

		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
		// PowerMockito.when(Calendar.getInstance().getTime()).thenReturn();

		// Ação
		Locacao retorno = service.alugarFilme(usuario, filmes);

		// Verificação
		assertThat(retorno.getDataRetorno(), MatchersProprios.caiNaSegunda());
		// PowerMockito.verifyNew(Date.class, times(2)).withNoArguments();

		// Verificar testes estáticos
		PowerMockito.verifyStatic(times(2));
		Calendar.getInstance();

//		boolean ehretorno = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.SUNDAY);
//		Assert.assertFalse(ehretorno);
//
//		Assume.assumeFalse(DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.SUNDAY));

//		assertThat(retorno.getDataRetorno(), caiEm(Calendar.TUESDAY));
//		assertThat(retorno.getDataRetorno(), caiNaSegunda());
		// assertThat(retorno.getDataRetorno(), ehHoje());
		// assertThat(retorno.getDataRetorno(), ehHojeDiferencaDias(1));

	}

	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
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

	@Test
	public void deveTratarErronoSPC() throws Exception {
		// Cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		Mockito.when(spc.possuiNegativacao(usuario))
				.thenThrow(new LocadoraException("Problemas com o SPC, tente novamente"));

		// verificação
		exception.expect(LocadoraException.class);
		exception.expectMessage("Problemas com o SPC, tente novamente");

		// ação
		service.alugarFilme(usuario, filmes);

	}

	@Test
	public void deveProrrogarUmaLocacao() {
		// Cenário
		Locacao locacao = LocacaoBuilder.umLocacao().agora();

		// Ação
		service.prorrogarLocacao(locacao, 3);

		// Verificação
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argCapt.capture());
		Locacao locacaoRetorno = argCapt.getValue();

		assertThat(locacaoRetorno.getValor(), is(30.0));
		assertThat(locacaoRetorno.getDataLocacao(), MatchersProprios.ehHoje());
		assertThat(locacaoRetorno.getDataRetorno(), MatchersProprios.ehHojeDiferencaDias(3));

	}

	@Test
	public void deveCalcularFilme_SemCalcularValor() throws Exception {
		// Cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		PowerMockito.doReturn(1.0).when(service, "calcularVaorTotal", filmes);

		// ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// Verificação
		Assert.assertThat(locacao.getValor(), is(1.0));
		PowerMockito.verifyPrivate(service).invoke("calcularVaorTotal", filmes);

	}

}
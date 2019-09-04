package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

/**
 * 
 * @author daniel-paulino
 * @RunWith- vai indicar ao junit a forma em que deve tratar os metodos
 *
 */
@RunWith(Parameterized.class)
public class CalcularValorLocacaoTest {

	private LocacaoService service;

	@Parameter
	public List<Filme> filmes;

	@Parameter(value = 1) // Value mostra a ordem que está definida os parametros
	public Double valorLocacao;

	@Parameter(value = 2)
	public String descricao;

	// É usado o before em todos os testes
	@Before
	public void setup() {
		service = new LocacaoService();
	}

	private static Filme filme1 = new Filme("Filme 1", 2, 4.0);
	private static Filme filme2 = new Filme("Filme 2", 1, 4.0);
	private static Filme filme3 = new Filme("Filme 3", 1, 4.0);
	private static Filme filme4 = new Filme("Filme 4", 2, 4.0);
	private static Filme filme5 = new Filme("Filme 5", 1, 4.0);
	private static Filme filme6 = new Filme("Filme 6", 1, 4.0);
	
	//Outra forma de instânciar o Filme, mas com o FilmeBuilder(DataBuilder)
	private static Filme outroFilme =FilmeBuilder.umFilme().agora();

	@Parameters(name = "Teste {index}= {2}")
	public static Collection<Object[]> getParametros() {
		return Arrays.asList(new Object[][] { { Arrays.asList(filme1, filme2, filme3), 11.0, "3 Filmes 25%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4), 13.00, "4 Filmes 50%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.00, "5 Filmes 75%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.00, "6 Filmes 100%" } });
		// pode ser colocado outra linha com outras condições para ser verificado
	}

	@Test
	public void deveCalcularValorDaLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {

		// Cenário
		Usuario usuario = new Usuario("Usuario 1");

		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// verficacao
		assertThat(resultado.getValor(), is(valorLocacao));
	}
}

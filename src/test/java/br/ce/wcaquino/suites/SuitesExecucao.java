package br.ce.wcaquino.suites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.servicos.CalculadoraTest;
import br.ce.wcaquino.servicos.CalcularValorLocacaoTest;
import br.ce.wcaquino.servicos.LocacaoServiceTest;

@RunWith(Suite.class)
@SuiteClasses({
	CalculadoraTest.class,
	CalcularValorLocacaoTest.class,
	LocacaoServiceTest.class
	
})
public class SuitesExecucao {
//Remova se puder	
	
	@BeforeClass
	public static void before() {
		System.out.println("Before do suite");
	}
	
	@AfterClass
	public static void after() {
		System.out.println("After do suite");

	}
	
}

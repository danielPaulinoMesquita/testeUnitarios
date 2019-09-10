package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class Calculadora {

	public int soma(int i, int j) {
		// TODO Auto-generated method stub
		return i + j;
	}

	public int subtrair(int i, int j) {
		// TODO Auto-generated method stub
		return i - j;
	}
	
	public void imprime() {
		System.out.println("Passei aqui");
	}

	public int dividir(int i, int j) throws NaoPodeDividirPorZeroException {
		if (j == 0) {
			throw new NaoPodeDividirPorZeroException();
		}
		return i / j;
	}

}

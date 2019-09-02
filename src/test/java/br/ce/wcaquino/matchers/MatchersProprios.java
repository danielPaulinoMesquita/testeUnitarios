package br.ce.wcaquino.matchers;

import java.util.Calendar;

public class MatchersProprios {

	public static DiaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaSemanaMatcher(diaSemana);
	}
	
	public static DiaSemanaMatcher caiNaSegunda() {
		return new DiaSemanaMatcher(Calendar.TUESDAY);
	}
	
	public static DataDiferencaDiasMatchers ehHoje() {
		return new DataDiferencaDiasMatchers(0);
	}
	
	public static DataDiferencaDiasMatchers ehHojeDiferencaDias(Integer dias) {
		return new DataDiferencaDiasMatchers(dias);
	}

}

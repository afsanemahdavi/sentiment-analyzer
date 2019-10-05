package test;

import mark.Marker;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Marker m = new Marker();
		//m.mark("بازی|N زیادم|ADV بد|ADJ نبود|V_PA");
		//m.mark("بازی|N خیلی|ADV زیادم|ADV بد|ADJ نبود|V_PA");
		//m.mark("اگه|CON ما|PRO بریم|V_PR میآن|V_PR");
		//m.mark("آنها|PRO قضاوت|V_PI می|V_PIO کنند|V_PA");
		//m.mark("نه|CON بازی|N خوب|ADJ بود|V_PA نه|CON حال|ADJ داد|V_PA");
		//m.mark("بازی|N خوب|ADJ نبود|V_PA ولی|CON حال|ADJ داد|V_PA");
		m.mark("خیلی|ADV خوب|ADJ ولی|CON خیلی|ADV سخت|ADJ است|V_PR");
		m.reduceTokens();
		m.printOrderdTokens();
		m.makeTwoWayGroups();
		m.printGroups();
		System.out.println(m.calculateSentiment());
	}
}

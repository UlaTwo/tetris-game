package tetris;

import tetris.view.TetrisBackgroundFrame;
/** Implementuję grę: tetris.
*@author UlaTwo
*data: 27.01.20
*/
public class StartTetris {
	/**Rozpoczęcie gry.
	* Utworzenie nowego obiektu klasy TetrisBackgroundFrame
	* i zainicjowanie jej działania
	* @param ars string
	*/
	public static void main (String[] ars)
	{
		TetrisBackgroundFrame newGame = new TetrisBackgroundFrame();
		newGame.init();
	}
}

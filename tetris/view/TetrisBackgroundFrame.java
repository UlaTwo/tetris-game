package tetris.view;
import javax.swing.*;
import java.awt.*;
/**
 *Klasa ustanawiająca ramę działania gry tetris.
 *Rozszerza komponent JFrame.
 *Posiada dwa prywatne pola:
 * private TetrisBoard newBoard;
 *które służy do zainicjalizowania działania nowej planszy
 * private JLabel statusBar;
 *które służy do wyświetlenia tekstu - statusu gry w oknie programu
*/
public class TetrisBackgroundFrame extends JFrame {

	private TetrisBoard newBoard;
	private JLabel statusBar;
	/**
	*Konstruktor klasy TetrisBackgroundFrame
	*W metodzie następuje zainicjalizowanie pól prywatnych.
	*JLabel statusBar zostaje zapisany tekstem powitalnym dla urzytkownika.
	* Ustawiane są także jego parametry: kolor, czcionka i wielkość.
	*/
	public TetrisBackgroundFrame() {
		statusBar = new JLabel(" Witamy w grze. Wciśnij m - menu.");
		statusBar.setForeground(Color.white);
		statusBar.setFont(new Font("Verdana", Font.PLAIN, 20));
		newBoard = new TetrisBoard(this);
	}
	/**
	*Klasa inicjująca pojawienie się okna gry.
	*Następuje:
	*ustawienie lokalizacji okna, ustawienie layout'u jako BrderLayout,
	*dodanie do okna ststusBar oraz newBoard, wystartowanie działania newBoard,
	*ustawienie koloru tła oraz rozmiaru okna, ustawienie tytułu okna,
	*ustawienie możliwych parametrów wyświetlania okna.
	*/
	public void init() {
		setLocation(100,30);
		setLayout(new BorderLayout());
		add(statusBar, BorderLayout.SOUTH);
    add(newBoard, BorderLayout.CENTER);
    newBoard.start();
		getContentPane().setBackground( new Color(25,25,112));
    setSize(400, 700);
    setPreferredSize(new Dimension(400, 700));
    setTitle("Gra: Tetris");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    pack();
    setVisible(true);
    setResizable(true);
	}

	JLabel getStatusBar() {
		return statusBar;
	}
}

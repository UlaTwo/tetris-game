/**
* Pakiet implementujący część widoku programu tetris napisanego według wzorca MVC.
*/
package tetris.view;


import tetris.controller.Controller;

import javax.swing.*;
import java.awt.*;
/**
* Zaimportowanie interfejsów słuchaczy.
*/
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.Random;

/**
* Klasa implemenrująca planszę tetrisa:
*    pola prywatne:
*      wysokosc, szerokosc, ststusBar, control
* Klasa zawiera metody służące do rysowania elementów na planszy,
* nasłuchiwania za pomocą słuchacza (ActionListener) naciśnięcia przez urzytkownika klawiszy,
* wyświetlania informacji dla użytkownika (w tym Menu).
 */
public class TetrisBoard extends JPanel implements ActionListener
{

    private final int WIDTH = 10;
    private final int HEIGHT = 22;
    private JLabel statusBar;
    private Controller control;

    /**
    * Konstruktor klasy TetrisBoard.
    *@param parent
    *zainicjowanie controlera: control.
    *zainicjowanie statusBar jako pochodzącego od przodka statusBar z klasy TetrisBackgroundFrame
    *dodanie słuchacz klawiatury (addKeyListener).
    */
    public TetrisBoard(TetrisBackgroundFrame parent)
    {
        setFocusable(true);
        control = new Controller(WIDTH, HEIGHT, this);
        statusBar = parent.getStatusBar();
        addKeyListener(new TetrisKeyAdapter());
    }

    /** wystartowanie działania planszy jest bezpośrednio zależne
    * od wystartowania działania kontrolera,
    * który startuje wszystkie metody TetrisBoard
    */
    void start()
    {
        control.start();
    }

    /**
    *Metoda nadpisująca działanie metody actionPerformed,
    *która jest wywoływana, kiedy zostanie wygenerowane zdarzenie
    * na obiekcie powiązanym ze słuchaczem.
    *Jest wtedy uruchamiana metoda z klasy kontrolera.
    *@param e wydarzenie słuchacza
    */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        control.gameAction();
    }

    /**
    * Metoda inicjująca narysowanie komponentów planszy oraz kontrolera,
    * Nie można jej bezpośrednio wywołać -
    * jedyną możliwością jej wywołania jest użycie metody repaint().
    * @param g potrzebny do metody ustawiającej komponenty graficzne
     */
    public void paint(Graphics g)
    {
        super.paint(g);
        control.paint(g, getSize().getWidth(), getSize().getHeight());
    }

    /**
    * Metoda ustawiająca tło dla póżniej rysowanych elementów:
    * kolor oraz rozmiar.
    * @param g potrzebny do metody ustawiającej komponenty graficzne
    * @param width wysokość planszy
    * @param height szerokość planszy
    */
    public void drawBackgroundForElements(Graphics g, int width, int height)
    {
      Color color = new Color(25,25,125);
      g.setColor(color);
      g.fillRect(0,0, width, height);

    }

    /**
    * Metody prywatne potrzebne do wyliczenia szerokości i wysokości
    * pojedynczego kwadracika dla klocka tetrisa.
    */
    private int elementWidth()
    {
      int width = (int) getSize().getWidth() / WIDTH;
      return width;
    }

    private int elementHeight()
    {
      int height = (int) getSize().getHeight() / HEIGHT;
      return height;
    }

    /**
    * Metoda prywatna pomocnicza, do ustalenia wartości liczbowej
    * potrzebnej do ustalenia koloru komponentu na podstawie typu kształtu.
    */
    private int setParamShape(tetris.model.Model.TetrisShape shape){
      int c = 10;
      if(shape!=null)
      {
        switch (shape)
        {
          case Blank:
                    c=0;
                    break;
          case ZElement:
                    c=1;
                    break;
          case SElement:
                    c=2;
                    break;
          case IElement:
                    c=3;
                    break;
          case TElement:
                    c=4;
                    break;
          case OElement:
                    c=5;
                    break;
          case LElement:
                    c=6;
                    break;
          case MirroredLElement:
                    c=7;
                    break;
        }
      }
      return c;
    }

    /**
    * Metoda rysująca jeden kwadrat (element podstawowy kształtów Tetris'a)
    * wypełniony kolorem w oknie programu i obramowany liniami.
    * @param element potrzebny do metody ustawiającej komponenty graficzne
    * @param x współrzędna x punktu, w którym ma się znajdywać lewy górny róg kwadratu
    * @param y współrzędna y punktu, w którym ma się znajdywać lewy górny róg kwadratu
    * @param shape kształt, który ma zostać narysowany - informacja potrzebna do doboru koloru
    */
    public void drawElement(Graphics element, int x, int y, tetris.model.Model.TetrisShape shape)
    {
          if(shape!=tetris.model.Model.TetrisShape.Blank)
          {
            int c=10;
            c = setParamShape(shape);
            int a = (c*30)%255;
            int b = (c*20);
            /*kwadracik*/
            Color color = new Color(255,255-a,b);
            element.setColor(color);
            element.fillRect(x + 1, y + 1, elementWidth() - 1, elementHeight() - 1);
            /*linie jaśniejsze*/
            element.setColor(color.brighter().brighter());
            element.drawLine(x, y + elementHeight() - 1, x, y);
            element.drawLine(x, y, x + elementWidth() - 1, y);
            /*linie ciemniejsze*/
            element.setColor(color.darker().darker());
            element.drawLine(x + 1, y + elementHeight() - 1, x + elementWidth() - 1, y + elementHeight() - 1);
            element.drawLine(x + elementWidth() - 1, y + elementHeight() - 1, x + elementWidth() - 1, y + 1);
          }
          /*kształt jest pusty - narysowanie klocka tła */
          else
          {
            Color color = new Color(25,25,112);
            element.setColor(color);
            element.fillRect(x + 1, y + 1, elementWidth() - 1, elementHeight() - 1);
            color = new Color(25,25,125);
            element.setColor(color);
            element.drawLine(x, y + elementHeight() - 1, x, y);
            element.drawLine(x, y, x + elementWidth() - 1, y);
            element.drawLine(x + 1, y + elementHeight() - 1, x + elementWidth() - 1, y + elementHeight() - 1);
            element.drawLine(x + elementWidth() - 1, y + elementHeight() - 1, x + elementWidth() - 1, y + 1);
          }
    }

    /**
    * Metoda służąca do rysowania menu z informacjami dla urzytkownika programu.
    * Ustawienie tła pod tekst, koloru i czcionki oraz wypisanie tekst.
    * @param g potrzebny do metody ustawiającej komponenty graficzne
    * @param width wysokość planszy
    * @param height szerokość planszy
    */
    public void drawMenu(Graphics g, int width, int height)
    {
      Color color = new Color(25,25,125);
      g.setColor(color);

      g.fillRect(0,0, width, height);
      color = new Color (225, 225,225);
      g.setColor(color);
      g.setFont(new Font("Verdana", Font.BOLD, 20));

      g.drawString("Menu:", width/3, 100);
      g.setFont(new Font("Verdana", Font.PLAIN, 18));
      g.drawString("p - pauza", width/5, 150);
      g.drawString("m - menu", width/5, 175);
      g.drawString("strzałka:", width/5, 200);
      g.drawString("dół - jedna kratka w dół", width/4, 225);
      g.drawString("góra - rotacja elmentu", width/4, 250);
      g.drawString("lewo - jedna kratka w lewo", width/4, 275);
      g.drawString("prawo - jedna kratka w prawo", width/4, 300);
      g.drawString("spacja - element na sam dół", width/5, 325);
      g.drawString("s - nowa gra", width/5, 350);
      g.drawString("e - zakończenie programu", width/5, 375);
    }

    /**
    *Metoda służąca do ustawiania wartości tekstu statusBar na aktualnie potrzebny dla urzytkownika.
    * @param text tekst na który aktualizowany jest tekst wyświetlany w oknie (statusBar)
    */
    public void setStatusBarText(String text)
    {
        statusBar.setText(text);
    }

    /**
    * Klasa obsługująca zdarzenia polegające na naciśnięciu przez urzytkownika klawisza klawiatury.
    */
    private class TetrisKeyAdapter extends KeyAdapter
    {
        /**
        * Metoda służąca do obsługi naciśnięcia przez urzytkownika obsługiwanych w programiw klawiszy klawiatury.
        * Metoda ta odwołuje się do odpowiednich przy danym zdarzeniu metod kontrolera,
        * w nim to są zapisane metody obsługi tych zdarzeń.
        * @param keyClicked wartość naciśniętego przez urzytkownika klawisza
        */
        public void keyPressed(KeyEvent keyClicked)
        {
            if ((control.started()==false || control.actualPieceBlank()==true) && control.waitingForNewGame()==false)
            {
                return;
            }
            int key = keyClicked.getKeyCode();
            if(key =='s' || key == 'S')
            {
              control.newGame();
              return;
            }
            if(key =='e' || key == 'E')
            {
              control.end();
              return;
            }
            if (key == 'p' || key == 'P')
            {
                control.pause();
                return;
            }
            if (key == 'm' || key == 'M')
            {
                control.menu();
                return;
            }
            if (control.paused()) return;

            switch (key)
            {
                case KeyEvent.VK_LEFT:
                    control.stepLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    control.stepRight();
                    break;
                case KeyEvent.VK_DOWN:
                    control.stepDown();
                    break;
                case KeyEvent.VK_UP:
                    control.rotate();
                    break;
                case KeyEvent.VK_SPACE:
                    control.goDown();
                    break;
            }
        }
    }
}

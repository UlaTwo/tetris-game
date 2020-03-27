/**
* Pakiet implementujący część kontrolera programu tetris napisanego według wzorca MVC.
*/
package tetris.controller;

import tetris.model.Model;
import tetris.view.TetrisBoard;

import javax.swing.*;
import java.awt.*;

/**
* Klasa implementująca kontroler do obsługi programu według wzorca MVC.
* Kontroler posiada wszelkie metody potrzebne do obsługi zdarzeń występujących w programie:
* start programu, jego zakończenia, tworzenie i wyświetlanie spadających klocków tetrisa.
* To także tutaj znajduje się zegar (timer) działania programu.
*/
public class Controller
{
    private TetrisBoard tetrisBoard;
    /*rozmiary planszy */
    private int boardWidth;
    private int boardHeight;
    /*zmienne określające stan programu */
    private boolean fallingEnd = false;
    private boolean started = false;
    private boolean paused = false;
    private boolean displayMenu = false;
    private boolean waitingForNewGame = false;
    /*liczba punktów*/
    private int points = 0;
    /*współrzędne aktualnego elementu Tetris'a */
    private int actualX = 0;
    private int actualY = 0;

    private Timer timer;
    /*aktualnie spadający element Tetrisa */
    private Model actualPiece;
    /*tablica zawiarająca wszystkie elementy tetrisa */
    private Model.TetrisShape[] board;

    /**
    * Kostruktor kontrolera.
    * @param boardWidth szerokość planszy
    * @param boardHeight wysokość planszy
    * @param tetrisBoard obiekt planszy gry
    * w konstruktorze zostają ustawione prywatne pola klasy Controller
    * dotyczące planszy i jej wymiarów, zostaje utworzony obiekt aktualnego klocka tetrisa
    * oraz zostaje uruchomiony zegar (timer).
    */
    public Controller(int boardWidth, int boardHeight, TetrisBoard tetrisBoard)
    {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.tetrisBoard = tetrisBoard;
        actualPiece = new Model();
        timer = new Timer(400, tetrisBoard);
        timer.start();
        board = new Model.TetrisShape[boardWidth * boardHeight];
        boardCleaner();
    }

    /**
    * Metoda podstawowej akcji zdarzeń w Tetrisie.
    * Uzależniona od wartości fallingEnd, która określa,
    * czy zakończyło się już spadanie aktualnego klocka.
    * Jeśli się nie zakończyło, to zostaje wywołana metoda do przesunięcia go o jedną linię w dół.
    * Jeśli się zakończyło, to uruchamiana jest metoda do utworzenia nowego.
    */
    public void gameAction()
    {
        if (fallingEnd)
        {
            fallingEnd = false;
            createNewPiece();
        }
        else
        {
            stepDown();
        }
    }

    /**
    * Metoda do użytku dla klas zewnętrznych,
    * aby poznać wartość prywatnego pola started
    * @return started wartość boolean, czy kontroler wystartował
    */
    public boolean started()
    {
        return started;
    }

    /**
    * Metoda do użytku dla klas zewnętrznych,
    * aby poznać wartość prywatnego pola paused
    * @return paused wartość boolean, czy program jest zapauzowany
    */
    public boolean paused()
    {
        return paused;
    }

    /**
    * Metoda do użytku dla klas zewnętrznych,
    * aby poznać wartość prywatnego pola waitingForNewGame
    * @return waitingForNewGame wartość boolean, czy program jest w stanie oczekiwania na decyzję urzytkownika o nowej grze
    */
    public boolean waitingForNewGame()
    {
        return waitingForNewGame;
    }

    /**
    * Metoda do użytku dla klas zewnętrznych,
    * aby poznać czy kształt tetrisa zapisany w prywatnym polu actualPiece jest kształtem pustym (Blank)
    * @return wartość boolean, czy actualPiece przechowuje pusty klocek
    */
    public boolean actualPieceBlank()
    {
        return actualPiece.getOneShape() == Model.TetrisShape.Blank;
    }

    /**
    * Metoda sprawiająca wystartowanie działania programu.
    * Jeżeli program jest zapauzowany, to nic się nie dzieje.
    * W przeciwnym przypadku inicjowane są pola: started, fallingEnd, points na wartości początkowe,
    * plansza jest czyszczona ze znajdujących się na niej wcześniej klocków,
    * tworzony jest nowy klocek Tetrisa oraz wystartowany zostaje timer.
    */
    public void start()
    {
        if (paused) return;
        started = true;
        fallingEnd = false;
        points = 0;
        boardCleaner();
        createNewPiece();
        timer.start();
    }

    /**
    * Metoda obsługująca zdarzenie zapauzowania programu.
    * Jeżeli program jest nie wystartowany, to nic się nie dzieje.
    * W przypadku, gdy program już wcześniej był zapauzowany
    * timer zostaje wystartowany, a wartość wyświetlanej informacji
    * dla urzytkownika zostaje zmieniona na liczbę punktów i informację o menu.
    * W przeciwnym przypadku timer zostaje zastopowany,
    * a informacja dla urzytkownika zostaje zmieniona na taką informującą go o pauzie.
    * w każdym z tych przypadków na końcu wygląd planszy zostaje zaktualizowany (odmalowany - repaint).
    */
    public void pause()
    {
        if (started==false)
            return;
        if (paused==false)
        {
            paused=true;
            timer.stop();
            tetrisBoard.setStatusBarText("Gra została wstrzymana. Wciśnij p.");
        }
        else
        {
            paused = false;
            timer.start();
            tetrisBoard.setStatusBarText("Punkty: " + String.valueOf(points)+"   Wciśnij m - menu.");
        }
        /*repaint, gdyż do paint nie można sie bezpośrednio odwołać */
        tetrisBoard.repaint();
    }

    /**
    * Metoda obsługująca zdarzenie wyświetlania menu.
    * Jeżeli program jest nie wystartowany, to nic się nie dzieje.
    * W przypadku, gdy program jest zapauzowany
    * timer zostaje wystartowany, a wartość wyświetlanej informacji
    * dla urzytkownika zostaje zmieniona na liczbę punktów i informację o menu,
    * wartość zmiennej boolean o wyświetlaniu menu zostaje zmieniona na false.
    * W przeciwnym przypadku timer zostaje zastopowany,
    * a zmienna boolean o wyświetlaniu menu zostaje zmieniona na true.
    * W każdym z tych przypadków na końcu wygląd planszy zostaje zaktualizowany (odmalowany - repaint).
    */
    public void menu(){
      if (started==false)
          return;

      if (paused==false)
      {
          paused=true;
          timer.stop();
          tetrisBoard.setStatusBarText("Nowa Gra!  Wciśnij m - menu.");
          displayMenu = true;
      }
      else
      {
          paused = false;
          timer.start();
          tetrisBoard.setStatusBarText("Punkty: " + String.valueOf(points)+"   Wciśnij m - menu.");
          displayMenu = false;
      }
      /*repaint, gdyż do paint nie można sie bezpośrednio odwołać*/
      tetrisBoard.repaint();
    }

    /**
    * Metoda ustawiająca nową grę.
    * Czyści plansze, ustawia pola klasy Controller na początkowe wartości,
    * a następnie startuje program.
    */
    public void newGame(){
      boardCleaner();
      /*ustawienie pól tak jak na początku*/
      fallingEnd = false;
      started = false;
      paused = false;
      displayMenu = false;
      waitingForNewGame = false;
      points = 0;
      tetrisBoard.setStatusBarText("Nowa Gra!  Wciśnij m - menu.");
      start();
    }

    /*Metoda kończąca działanie programu.
    * ustawia zmienną fallingEnd na true, oraz started na false,
    * Zastopowuje działanie timera, oraz ustawia tekst do wyświetlania
    * dla urzytkownika jako ten o końcu gry.
    */
    public void end()
    {
      fallingEnd = true;
      started=false;
      points=1;
      paused = true;
      displayMenu = false;
      waitingForNewGame = false;
      timer.stop();
      tetrisBoard.setStatusBarText("Program zakończony!");
    }

    /**
    * Metoda uruchamiająca metodę zmieniająca pozycję aktualnego klocka na o jedną linie niższą
    * pod warunkiem, że jest to możliwe.
    */
    public void stepDown()
    {
        if (checkMove(actualPiece, actualX, actualY - 1)==false)
            actualPieceDropped();
    }

    /** Metoda zapełniająca wszystkie elementy tablicy kształtów
    * kształtem Blank - pustym kształtem
    */
    private void boardCleaner()
    {
        for (int i = 0; i < boardHeight * boardWidth; i++)
            board[i] = Model.TetrisShape.Blank;
    }

    /**
    * Metoda uruchamiająca metodę zmieniająca pozycję aktualnego klocka na najniższą z możliwych
    * na koniec uruchamia metodę dla sytuacji, gdy aktualny klocek spadł na sam dół.
    */
    public void goDown()
    {
        int newY = actualY;
        while (newY > 0)
        {
            if (checkMove(actualPiece, actualX, newY - 1)==false) break;
            newY--;
        }
        actualPieceDropped();
    }

    /** Metoda pomocnicza:
     * zwraca jaki kształt jest zapisany pod danym polem w tablicy.
     */
    private Model.TetrisShape whichShapeOnBoard(int x, int y)
    {
        return board[(y * boardWidth) + x];
    }

    /**
     * Metoda służąca do wyrysowywania klocków tetrisa zapisanych w tablicy board
     * (uruchomienia stosownej metody z klasy TetrisBoard)
     * jeśli zmienna displayMenu jest fals, to zostaje uruchomiona metoda
     * z klasy TetrisBoard służąca wyświetleniu Menu.
     * @param g potrzebny do metody ustawiającej komponenty graficzne
     * @param width wysokość planszy
     * @param height szerokość planszy
     */
    public void paint(Graphics g, double width, double height)
    {
      if(displayMenu == false)
      {
        int elementWidth = (int) width / boardWidth;
        int elementHeight = (int) height / boardHeight;
        int boardTop = (int) height - boardHeight * elementHeight;
        /*wyrysowanie tła*/
        tetrisBoard.drawBackgroundForElements(g, (int)width, (int)height);

        /* rysuje kwadraty tam, gdzie powinny być */
        for (int i = 0; i < boardHeight; ++i)
        {
            for (int j = 0; j < boardWidth; ++j)
            {
                tetris.model.Model.TetrisShape shape = whichShapeOnBoard(j, boardHeight - i - 1);
                tetrisBoard.drawElement(g, j * elementWidth, boardTop + i * elementHeight, shape);
            }
        }
       if (actualPiece.getOneShape() != tetris.model.Model.TetrisShape.Blank)
       {
            for (int i = 0; i < 4; ++i)
            {
                int x = actualX + actualPiece.checkX(i);
                int y = actualY - actualPiece.checkY(i);
                tetrisBoard.drawElement(g, x * elementWidth, boardTop + (boardHeight - y - 1) * elementHeight, actualPiece.getOneShape());
            }
          }
      }
      else
      {
        tetrisBoard.drawMenu( g, (int) width, (int) height);
      }
    }

    /**
    * Metoda prywatna do tworzenia nowego klocka tetrisa.
    */
    private void createNewPiece()
    {
        actualPiece.setRandomShape();
        actualX = boardWidth / 2 + 1;
        actualY = boardHeight - 1 + actualPiece.minimumY();

        /*nie da się już dodać nowego elementu na planszę - gra zakończona*/
        if (checkMove(actualPiece, actualX, actualY)==false)
        {
            actualPiece.setOneShape(Model.TetrisShape.Blank);
            paused();
            started = false;
            tetrisBoard.setStatusBarText("Gra zakończona! Nowa: s, end: e ");
            waitingForNewGame = true;
        }
    }

    /**
    * Metoda prywatna do sprawdzenia, czy aktualny klocek o danym kształcie
    * może zostać położonym na planszy w danym miejscu, a także do przęłożenia go
    * w podane miejsce, jeśli jest to możliwe
    * Zwraca wartość boolean w zależności od tego, czy zmiana pozycji jest możaliwa.
    */
    private boolean checkMove(Model shape, int tryX, int tryY)
    {
        for (int i = 0; i < 4; i++)
        {
            int x = tryX + shape.checkX(i);
            int y = tryY - shape.checkY(i);
            /* sprawdzwnie, czy nie wychodzi poza planszę*/
            if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight) return false;
            /*sprawdzenie, czy nie jest już na tym miejscu inny element*/
            if (whichShapeOnBoard(x, y) != Model.TetrisShape.Blank) return false;
        }

        /* uaktualniamy dane o aktualnym kształcie */
        actualPiece = shape;
        actualX = tryX;
        actualY = tryY;
        tetrisBoard.repaint();
        return true;
    }
    /**
    * Metoda prywatna zapisująca do tablicy board informację o aktualnym klocku,
    * wywoływana gdy zakończy już on spadanie.
    */
    private void actualPieceDropped()
    {
        for (int i = 0; i < 4; ++i)
        {
            int x = actualX + actualPiece.checkX(i);
            int y = actualY - actualPiece.checkY(i);
            board[(y * boardWidth) + x] = actualPiece.getOneShape();
        }
        removeFullLines();
        if (fallingEnd==false)
            createNewPiece();
    }

    /**
    * Metoda prywatna usuwająca pełne linie i doliczająca zdobyte przez urzytkownika punkty.
    */
    private void removeFullLines()
    {
        int numFullLines = 0;
        for (int i = boardHeight - 1; i >= 0; --i)
        {   //sprawdzanie, czy któraś z linia jest zapełniona
            boolean lineIsFull = true;
            for (int j = 0; j < boardWidth; ++j)
            {
                if (whichShapeOnBoard(j, i) == Model.TetrisShape.Blank)
                {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull)
            {   // jesłi zapełniona, to w tablicy board przepisywane są na tę linię kształty z jednej linii wyżej
                numFullLines++;
                for (int k = i; k < boardHeight - 1; ++k)
                {
                    for (int j = 0; j < boardWidth; ++j)
                        board[(k * boardWidth) + j] = whichShapeOnBoard(j, k + 1);
                }
            }
        }
        if (numFullLines > 0)
        {   //doliczenie punktów, zakończenie opadania klocka
            points = points + numFullLines;
            tetrisBoard.setStatusBarText("Punkty: " + String.valueOf(points)+"   Wciśnij m - menu.");
            fallingEnd = true;
            actualPiece.setOneShape(Model.TetrisShape.Blank);
            tetrisBoard.repaint();
        }
    }

    /**
    * Metoda uruchamiająca metodę zmieniająca pozycję aktualnego klocka na o jedną linie w lewo
    * pod warunkiem, że jest to możliwe.
    */
    public void stepLeft()
    {
        checkMove(actualPiece, actualX - 1, actualY);
    }
    /**
    * Metoda uruchamiająca metodę zmieniająca pozycję aktualnego klocka na o jedną w prawo
    * pod warunkiem, że jest to możliwe.
    */
    public void stepRight()
    {
        checkMove(actualPiece, actualX + 1, actualY);
    }
    /**
    * Metoda uruchamiająca metodę zmieniająca pozycję aktualnego klocka w rotacji
    * pod warunkiem, że jest to możliwe.
    */
    public void rotate()
    {
        checkMove(actualPiece.rotate(), actualX, actualY);
    }

}

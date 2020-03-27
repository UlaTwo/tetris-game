/**
* Pakiet implementujący część kontrolera programu tetris napisanego według wzorca MVC.
*/
package tetris.model;

import java.util.Random;

/**
* Klasa implementująca model do programu tetris według wzorca MVC.
* To tutaj są przechowywane informacje o danych - w tym przypadku o kształtach klocków używanych w tetrisie
* oraz podstawowe metody udostępniające informacje o nim.
*/
public class Model {

    /**
    * Publiczny typ enum rodajów klocków w tetrisie.
    */
    public enum TetrisShape {
        Blank, ZElement, SElement, IElement, TElement, OElement, LElement,
         MirroredLElement
    }

    private TetrisShape oneShape;
    private int[][] oneShapeTable;
    private int[][][] shapesTable;

    /**
    * Konstruktor klasy model - inicjuje tablice oneShapeTable,
    * która ma przechowywać współrzędne jednego klocka (ustawia ten kształt na kształt pusty)
    * inicjuje i zapełnia tablicę shapesTable, która przechowuje informacje (współrzędne)
    * wszystkich klockach tetrisa
    */
    public Model()
    {
        oneShapeTable = new int[4][2];
        shapesTable = new int[][][]
        {
                {{0, 0}, {0, 0}, {0, 0}, {0, 0}},
                {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}},
                {{0, -1}, {0, 0}, {1, 0}, {1, 1}},
                {{0, -1}, {0, 0}, {0, 1}, {0, 2}},
                {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},
                {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
                {{-1, -1}, {0, -1}, {0, 0}, {0, 1}},
                {{1, -1}, {0, -1}, {0, 0}, {0, 1}}
        };
        /*ustawienie oneShape w naszej klasie*/
        setOneShape(TetrisShape.Blank);
    }


    /**
    * Metoda służaca do przekopiowanie na oneShapeTable zadanego kształtu
    * @param oneShape zadany kształt do przekopiowania
    */
    public void setOneShape(TetrisShape oneShape)
    {
        for (int i = 0; i < 4 ; i++)
        {
            System.arraycopy(shapesTable[oneShape.ordinal()][i], 0, oneShapeTable[i], 0, 2);
        }
        this.oneShape = oneShape;
    }

    /**
     * Metoda, która zwraca pieceShape, potrzebna klasie kontrolera
     * @return oneShape kształt aktualnie przechowywany w danym obiekcie model
     */
    public TetrisShape getOneShape() {
        return oneShape;
    }

    /**
    * Metoda, która służy do losowego ustawianie własnego oneShape.
    */
    public void setRandomShape()
    {
        Random r = new Random();
        //nie moze zostać wylosowanu klocek 0 - bo jest to kształt pusty
        int x = Math.abs(r.nextInt()) % 7+1;
        TetrisShape[] values = TetrisShape.values();
        setOneShape(values[x]);
    }

    /**
    * Metoda służąca do wyliczenia minimalnej wartości Y, czyli jaka jest najniższa współrzędna klocka,
    * metoda potrzbne później do skorzystania w kontrolerze przy ustawianiu klocka na planszy.
    * @return minimum wartość minimalna współrzędnej y danego kształtu
    */
    public int minimumY()
    {
        int minimum = oneShapeTable[0][1];
        for (int i = 0; i < 4; i++)
        {
            minimum = Math.min(minimum, oneShapeTable[i][1]);
        }
        return minimum;
    }

    /**
    * Metoda służąca do rotacji elementu w tablicy
    * Tworzy nowy obiekt Model, w którego tablicy oneShapeTable zapisuje klocek,
    * który został obrócony.
    * @return rotatedShape obiekt z przerotowanym klockiem Tetris'a
    */
    public Model rotate()
    {
        if (oneShape == TetrisShape.OElement) return this;
        Model rotatedShape = new Model();
        rotatedShape.oneShape = oneShape;

        for (int i = 0; i < 4; ++i)
        {
            rotatedShape.oneShapeTable[i][0] = oneShapeTable[i][1];
            rotatedShape.oneShapeTable[i][1] = (-1)*oneShapeTable[i][0];
        }
        return rotatedShape;
    }

    /**
    *Metoda służaca do sprawdzenie współrzędnej x danego elementu z tablicy
    * @param i która współrzędna klocka
    * @return oneShape[i][0] współrzędna x zadanej współrzędnej klocka
    */
    public int checkX(int i)
    {
      return oneShapeTable[i][0];
    }

    /**
    *Metoda służaca do sprawdzenie współrzędnej y danego elementu z tablicy
    * @param i która współrzędna klocka
    * @return oneShape[i][1] współrzędna y zadanej współrzędnej klocka
    */
    public int checkY(int i)
    {
      return oneShapeTable[i][1];
    }
}

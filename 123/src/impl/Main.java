package impl; // Объявление пакета, в котором находится класс Main.

/*  Класс Main содержит метод main, который является точкой входа для 
    выполнения программы. Он устанавливает размеры окна игры и уровень 
    сложности по умолчанию. В методе main загружается изображение 
    для иконки игры, создается экземпляр класса Game 
    с заданными параметрами, игра запускается, и загружается 
    сцена главного меню. Этот класс служит основным контроллером 
    для инициализации и запуска игры
*/

import gameEngine.Game; // Импорт класса Game из пакета gameEngine.
import gameEngine.ResourceLoader; // Импорт класса ResourceLoader из пакета gameEngine.
import impl.scenes.MainMenuScene; // Импорт класса MainMenuScene из пакета impl.scenes.
import java.awt.Image; // Импорт класса Image из пакета java.awt для работы с изображениями.

public class Main { // Объявление класса Main.
    public static int WIDTH = 1270; // Ширина окна игры.
    public static int HEIGHT = 640; // Высота окна игры.

    public static Difficulty difficulty = Difficulty.MEDIUM; // Установка уровня сложности по умолчанию на MEDIUM.

    public static void main(String[] args) { // Главный метод, точка входа в программу.
        Image icon = ResourceLoader.loadImage("res/images/Icon.png"); // Загрузка изображения иконки игры.
        Game game = new Game("Space", WIDTH, HEIGHT, icon); // Создание нового объекта Game с названием, размерами и иконкой.
        game.start(); // Запуск игры.
        game.loadScene(new MainMenuScene()); // Загрузка главного меню игры.
    }
}

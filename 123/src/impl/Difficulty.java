package impl; // Объявление пакета, в котором находится перечисление Difficulty.

/*  Перечисление Difficulty определяет три уровня сложности: EASY, MEDIUM и HARD, 
    каждый из которых имеет соответствующий модификатор, используемый для 
    изменения игровых параметров (например, сложности игры). 
    Каждый уровень сложности инициализируется с помощью конструктора, 
    который устанавливает значение модификатора. Метод getModifier позволяет 
    получить значение модификатора для каждого уровня сложности.
*/

public enum Difficulty { // Объявление перечисления Difficulty.
    EASY(1), // Определение уровня сложности EASY с модификатором 1.
    MEDIUM(1.25), // Определение уровня сложности MEDIUM с модификатором 1.25.
    HARD(1.50); // Определение уровня сложности HARD с модификатором 1.50.

    private double modifier; // Поле для хранения модификатора сложности.

    // Конструктор перечисления, принимающий модификатор.
    Difficulty(double modifier) {
        this.modifier = modifier; // Инициализация поля модификатора.
    }

    // Метод для получения значения модификатора.
    public double getModifier() {
        return modifier; // Возврат значения модификатора.
    }
}

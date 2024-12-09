package impl;

/*  
*   Уровни сложности
*/

public enum Difficulty { 
    EASY(1),
    MEDIUM(1.25), 
    HARD(1.50); 

    private double modifier; // Тут храним числовое значение (модификатор)


    Difficulty(double modifier) {
        this.modifier = modifier; 
    }


    public double getModifier() {
        return modifier;
    }
}

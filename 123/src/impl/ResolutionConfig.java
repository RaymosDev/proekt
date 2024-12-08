package impl; // Объявление пакета, в котором находится класс ResolutionConfig.

/*  Класс ResolutionConfig управляет разрешениями и размерами различных игровых 
    объектов, таких как HealthDrop, Asteroid, PlayerShip, и других. Он содержит 
    вложенный класс Resolution, который хранит ширину и высоту. 
    В классе определены статические финальные переменные для различных 
    разрешений объектов в зависимости от разрешения экрана. 
    Методы класса возвращают размеры соответствующих объектов в зависимости 
    от текущего разрешения игры, определяемого в классе Main. 
    Если разрешение не соответствует ни одному из заданных, 
    возвращается значение по умолчанию (размер для 1280x720).
*/

public class ResolutionConfig { // Объявление класса ResolutionConfig.
    
    public static class Resolution { // Вложенный класс Resolution для хранения ширины и высоты.
        public final int width; // Ширина разрешения.
        public final int height; // Высота разрешения.

        // Конструктор класса Resolution, принимающий ширину и высоту.
        public Resolution(int width, int height) {
            this.width = width; // Инициализация ширины.
            this.height = height; // Инициализация высоты.
        }
    }

    // Размеры для HealthDrop
    private static final Resolution RESOLUTION_1280x720_HEALTHDROP = new Resolution(20, 32); // Разрешение для HealthDrop при 1280x720.
    private static final Resolution RESOLUTION_1800x800_HEALTHDROP = new Resolution(30, 48); // Разрешение для HealthDrop при 1800x800.

    // Размеры для AsteroidLarge
    private static final Resolution RESOLUTION_1280x720_ASTEROID = new Resolution(133, 133); // Разрешение для AsteroidLarge при 1280x720.
    private static final Resolution RESOLUTION_1800x800_ASTEROID = new Resolution(200, 200); // Разрешение для AsteroidLarge при 1800x800.
    
    // Размеры для PlayerShip
    private static final Resolution RESOLUTION_1280x720_PLAYERSHIP = new Resolution(98, 47); // Разрешение для PlayerShip при 1280x720.
    private static final Resolution RESOLUTION_1800x800_PLAYERSHIP = new Resolution(147, 70); // Разрешение для PlayerShip при 1800x800.
    
    // Размеры для Laser PlayerShip
    private static final Resolution RESOLUTION_1280x720_LASER = new Resolution(33, 7); // Разрешение для Laser PlayerShip при 1280x720.
    private static final Resolution RESOLUTION_1800x800_LASER = new Resolution(50, 10); // Разрешение для Laser PlayerShip при 1800x800.
    
    // Размеры для AsteroidSmall
    private static final Resolution RESOLUTION_1280x720_ASTEROID_SMALL = new Resolution(67, 67); // Разрешение для AsteroidSmall при 1280x720.
    private static final Resolution RESOLUTION_1800x800_ASTEROID_SMALL = new Resolution(100, 100); // Разрешение для AsteroidSmall при 1800x800.
    
    // Размеры для Hornet
    private static final Resolution RESOLUTION_1280x720_HORNET = new Resolution(88, 42); // Разрешение для Hornet при 1280x720.
    private static final Resolution RESOLUTION_1800x800_HORNET = new Resolution(132, 63); // Разрешение для Hornet при 1800x800.
    
    // Размеры для Laser Hornet
    private static final Resolution RESOLUTION_1280x720_LASER_Hornet = new Resolution(20, 4); // Разрешение для Laser Hornet при 1280x720.
    private static final Resolution RESOLUTION_1800x800_LASER_Hornet = new Resolution(30, 6); // Разрешение для Laser Hornet при 1800x800.
    
    // Размеры для Javelin
    private static final Resolution RESOLUTION_1280x720_Javelin = new Resolution(106, 60); // Разрешение для Javelin при 1280x720.
    private static final Resolution RESOLUTION_1800x800_Javelin = new Resolution(159, 116); // Разрешение для Javelin при 1800x800.
    
    // Размеры для Laser Javelin
    private static final Resolution RESOLUTION_1280x720_LASER_Javelin = new Resolution(60, 10); // Разрешение для Laser Javelin при 1280x720.
    private static final Resolution RESOLUTION_1800x800_LASER_Javelin = new Resolution(90, 15); // Разрешение для Laser Javelin при 1800x800.
    
    // Размеры для Marauder
    private static final Resolution RESOLUTION_1280x720_Marauder = new Resolution(167, 167); // Разрешение для Marauder при 1280x720.
    private static final Resolution RESOLUTION_1800x800_Marauder = new Resolution(250, 250); // Разрешение для Marauder при 1800x800.
    
    // Размеры для LARGE_ORB Marauder
    private static final Resolution RESOLUTION_1280x720_LARGE_ORB_Marauder = new Resolution(33, 33); // Разрешение для LARGE_ORB Marauder при 1280x720.
    private static final Resolution RESOLUTION_1800x800_LARGE_ORB_Marauder = new Resolution(50, 50); // Разрешение для LARGE_ORB Marauder при 1800x800.
    
    // Размеры для SMALL_ORB Marauder
    private static final Resolution RESOLUTION_1280x720_SMALL_ORB_Marauder = new Resolution(20, 20); // Разрешение для SMALL_ORB Marauder при 1280x720.
    private static final Resolution RESOLUTION_1800x800_SMALL_ORB_Marauder = new Resolution(30, 30); // Разрешение для SMALL_ORB Marauder при 1800x800.

    // Метод для получения размера HealthDrop в зависимости от разрешения.
    public static Resolution getHealthDropSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_HEALTHDROP; // Возвращает размер для 1280x720.
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_HEALTHDROP; // Возвращает размер для 1800x800.
        }
        return RESOLUTION_1280x720_HEALTHDROP; // Значение по умолчанию.
    }

    // Метод для получения размера Asteroid в зависимости от разрешения.
    public static Resolution getAsteroidSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_ASTEROID; // Возвращает размер для 1280x720.
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_ASTEROID; // Возвращает размер для 1800x800.
        }
        return RESOLUTION_1280x720_ASTEROID; // Значение по умолчанию.
    }
    
    // Метод для получения размера PlayerShip в зависимости от разрешения.
    public static Resolution getPlayerShipSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_PLAYERSHIP; // Возвращает размер для 1280x720.
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_PLAYERSHIP; // Возвращает размер для 1800x800.
        }
        return RESOLUTION_1280x720_PLAYERSHIP; // Значение по умолчанию.
    }

    // Метод для получения размера лазера PlayerShip в зависимости от разрешения.
    public static Resolution getLaserSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_LASER; // Возвращает размер для 1280x720.
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_LASER; // Возвращает размер для 1800x800.
        }
        return RESOLUTION_1280x720_LASER; // Значение по умолчанию.
    }

    // Метод для получения размера AsteroidSmall в зависимости от разрешения.
    public static Resolution getAsteroidSmallSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_ASTEROID_SMALL; // Возвращает размер для 1280x720.
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_ASTEROID_SMALL; // Возвращает размер для 1800x800.
        }
        return RESOLUTION_1280x720_ASTEROID_SMALL; // Значение по умолчанию.
    }

    // Метод для получения размера Hornet в зависимости от разрешения.
    public static Resolution getHornetSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_HORNET; // Возвращает размер для 1280x720.
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_HORNET; // Возвращает размер для 1800x800.
        }
        return RESOLUTION_1280x720_HORNET; // Значение по умолчанию.
    }

    // Метод для получения размера лазера Hornet в зависимости от разрешения.
    public static Resolution getLaserHornetSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_LASER_Hornet; // Возвращает размер для 1280x720.
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_LASER_Hornet; // Возвращает размер для 1800x800.
        }
        return RESOLUTION_1280x720_LASER_Hornet; // Значение по умолчанию.
    }

    // Метод для получения размера Javelin в зависимости от разрешения.
    public static Resolution getJavelinSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_Javelin; // Возвращает размер для 1280x720.
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_Javelin; // Возвращает размер для 1800x800.
        }
        return RESOLUTION_1280x720_Javelin; // Значение по умолчанию.
    }

    // Метод для получения размера лазера Javelin в зависимости от разрешения.
    public static Resolution getLaserJavelinSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_LASER_Javelin; // Возвращает размер для 1280x720.
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_LASER_Javelin; // Возвращает размер для 1800x800.
        }
        return RESOLUTION_1280x720_LASER_Javelin; // Значение по умолчанию.
    }

    // Метод для получения размера Marauder в зависимости от разрешения.
    public static Resolution getMarauderSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_Marauder; // Возвращает размер для 1280x720.
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_Marauder; // Возвращает размер для 1800x800.
        }
        return RESOLUTION_1280x720_Marauder; // Значение по умолчанию.
    }

    // Метод для получения размера LARGE_ORB Marauder в зависимости от разрешения.
    public static Resolution getLargeOrbSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_LARGE_ORB_Marauder; // Возвращает размер для 1280x720.
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_LARGE_ORB_Marauder; // Возвращает размер для 1800x800.
        }
        return RESOLUTION_1280x720_LARGE_ORB_Marauder; // Значение по умолчанию.
    }

    // Метод для получения размера SMALL_ORB Marauder в зависимости от разрешения.
    public static Resolution getSmallOrbSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_SMALL_ORB_Marauder; // Возвращает размер для 1280x720.
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_SMALL_ORB_Marauder; // Возвращает размер для 1800x800.
        }
        return RESOLUTION_1280x720_SMALL_ORB_Marauder; // Значение по умолчанию.
    }
}
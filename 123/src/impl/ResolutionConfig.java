package impl; 

/*  
* Тут храним размеры всех объектов в зависимости от выбранного разрешения
* По дефолту на всех объектах возвращаем 1280x720
*/

public class ResolutionConfig { 
    
    public static class Resolution {
        public final int width; 
        public final int height; 

        public Resolution(int width, int height) {
            this.width = width; 
            this.height = height; 
        }
    }

    // Размеры для HealthDrop
    private static final Resolution RESOLUTION_1280x720_HEALTHDROP = new Resolution(20, 32); 
    private static final Resolution RESOLUTION_1800x800_HEALTHDROP = new Resolution(30, 48); 

    // Размеры для AsteroidLarge
    private static final Resolution RESOLUTION_1280x720_ASTEROID = new Resolution(133, 133); 
    private static final Resolution RESOLUTION_1800x800_ASTEROID = new Resolution(200, 200); 
    
    // Размеры для PlayerShip
    private static final Resolution RESOLUTION_1280x720_PLAYERSHIP = new Resolution(98, 47); 
    private static final Resolution RESOLUTION_1800x800_PLAYERSHIP = new Resolution(147, 70); 
    
    // Размеры для Laser PlayerShip
    private static final Resolution RESOLUTION_1280x720_LASER = new Resolution(33, 7); 
    private static final Resolution RESOLUTION_1800x800_LASER = new Resolution(50, 10); 
    
    // Размеры для AsteroidSmall
    private static final Resolution RESOLUTION_1280x720_ASTEROID_SMALL = new Resolution(67, 67); 
    private static final Resolution RESOLUTION_1800x800_ASTEROID_SMALL = new Resolution(100, 100); 
    
    // Размеры для Hornet
    private static final Resolution RESOLUTION_1280x720_HORNET = new Resolution(88, 42); 
    private static final Resolution RESOLUTION_1800x800_HORNET = new Resolution(132, 63); 
    
    // Размеры для Laser Hornet
    private static final Resolution RESOLUTION_1280x720_LASER_Hornet = new Resolution(20, 4);
    private static final Resolution RESOLUTION_1800x800_LASER_Hornet = new Resolution(30, 6);
    
    // Размеры для Javelin
    private static final Resolution RESOLUTION_1280x720_Javelin = new Resolution(106, 60);
    private static final Resolution RESOLUTION_1800x800_Javelin = new Resolution(159, 116); 
    
    // Размеры для Laser Javelin
    private static final Resolution RESOLUTION_1280x720_LASER_Javelin = new Resolution(60, 10); 
    private static final Resolution RESOLUTION_1800x800_LASER_Javelin = new Resolution(90, 15);
    
    // Размеры для Marauder
    private static final Resolution RESOLUTION_1280x720_Marauder = new Resolution(167, 167); 
    private static final Resolution RESOLUTION_1800x800_Marauder = new Resolution(250, 250);
    
    // Размеры для LARGE_ORB Marauder
    private static final Resolution RESOLUTION_1280x720_LARGE_ORB_Marauder = new Resolution(33, 33);
    private static final Resolution RESOLUTION_1800x800_LARGE_ORB_Marauder = new Resolution(50, 50); 
    
    // Размеры для SMALL_ORB Marauder
    private static final Resolution RESOLUTION_1280x720_SMALL_ORB_Marauder = new Resolution(20, 20);
    private static final Resolution RESOLUTION_1800x800_SMALL_ORB_Marauder = new Resolution(30, 30);



    // Методы для получения размеров объектов в зависимости от разрешения:

    public static Resolution getHealthDropSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_HEALTHDROP; 
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_HEALTHDROP; 
        }
        return RESOLUTION_1280x720_HEALTHDROP; 
    }


    public static Resolution getAsteroidSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_ASTEROID; 
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_ASTEROID; 
        }
        return RESOLUTION_1280x720_ASTEROID; 
    }
    

    public static Resolution getPlayerShipSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_PLAYERSHIP; 
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_PLAYERSHIP; 
        }
        return RESOLUTION_1280x720_PLAYERSHIP; 
    }


    public static Resolution getLaserSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_LASER; 
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_LASER; 
        }
        return RESOLUTION_1280x720_LASER; 
    }


    public static Resolution getAsteroidSmallSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_ASTEROID_SMALL; 
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_ASTEROID_SMALL; 
        }
        return RESOLUTION_1280x720_ASTEROID_SMALL; 
    }


    public static Resolution getHornetSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_HORNET; 
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_HORNET; 
        }
        return RESOLUTION_1280x720_HORNET; 
    }


    public static Resolution getLaserHornetSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_LASER_Hornet; 
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_LASER_Hornet; 
        }
        return RESOLUTION_1280x720_LASER_Hornet; 
    }


    public static Resolution getJavelinSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_Javelin; 
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_Javelin; 
        }
        return RESOLUTION_1280x720_Javelin; 
    }


    public static Resolution getLaserJavelinSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_LASER_Javelin; 
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_LASER_Javelin; 
        }
        return RESOLUTION_1280x720_LASER_Javelin; 
    }


    public static Resolution getMarauderSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_Marauder; 
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_Marauder; 
        }
        return RESOLUTION_1280x720_Marauder; 
    }


    public static Resolution getLargeOrbSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_LARGE_ORB_Marauder; 
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_LARGE_ORB_Marauder; 
        }
        return RESOLUTION_1280x720_LARGE_ORB_Marauder; 
    }


    public static Resolution getSmallOrbSize() {
        if (Main.WIDTH == 1280 && Main.HEIGHT == 720) {
            return RESOLUTION_1280x720_SMALL_ORB_Marauder; 
        } else if (Main.WIDTH == 1800 && Main.HEIGHT == 800) {
            return RESOLUTION_1800x800_SMALL_ORB_Marauder; 
        }
        return RESOLUTION_1280x720_SMALL_ORB_Marauder; 
    }
}
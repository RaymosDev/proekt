package gameEngine; 

/*  
 *   Entity - это SceneObject, который имеет Collider для обработки столкновений и использует Vector2 для установки позиции и размера. 
*/

public abstract class Entity extends SceneObject 
{
    private Collider collider; 

    public Entity(Vector2 position, Vector2 size) 
    { 
        collider = new EntityCollider(this, position, size); 
    }

    @Override
    public void initialize() 
    { 
        collider.setActive(true); // активируем коллайдер при инициализации сущности
    }

    public final Vector2 getPosition() 
    { 
        return collider.getCenter(); 
    }

    public final void setPosition(Vector2 position) 
    { 
        collider.setCenter(position);
    }

    public Vector2 getSize() 
    { 
        return collider.getDimensions().clone(); 
    }

    public void setSize(Vector2 size) 
    { 
        collider.setDimensions(size); 
    }

    @Override
    public void dispose() 
    { 
        collider.setActive(false); // деактивируем коллайдер при освобождении сущности
    }

    public abstract void onCollisionEnter(Entity other); // для обработки входа в столкновение с другой сущностью

    public abstract void onCollisionExit(Entity other); // для обработки выхода из столкновения с другой сущностью




    public static class EntityCollider extends Collider 
    { 
        private Entity entity; // Ссылка на сущность, к которой принадлежит этот коллайдер

        private EntityCollider(Entity entity, Vector2 position, Vector2 size) 
        { 
            super(position, size.getX(), size.getY()); // Вызов конструктора Collider
            this.entity = entity; // cохранение ссылки на сущность.
        }

        public Entity getEntity() // получение сущности, к которой принадлежит этот коллайдер
        { 
            return entity;
        }


        @Override
        public void onCollisionEnter(Collider other) // Обработка входа в столкновение
        { 
            if (other instanceof EntityCollider) // Если другой коллайдер - это тоже Entity
            { 
                Entity otherEntity = ((EntityCollider) other).getEntity(); 
                entity.onCollisionEnter(otherEntity); // вызов метода обработки входа в столкновение для текущей сущности
            }
        }


        @Override
        public void onCollisionExit(Collider other) // Обработка выхода из столкновения
        {
            if (other instanceof EntityCollider)  // Если другой коллайдер - это тоже Entity
            {
                Entity otherEntity = ((EntityCollider) other).getEntity();
                entity.onCollisionExit(otherEntity); // вызов метода обработки выхода из столкновения для текущей сущности
            }
        }
    }
}

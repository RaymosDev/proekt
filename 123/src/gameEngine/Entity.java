package gameEngine;

public abstract class Entity extends SceneObject {
    private Collider collider;

    public Entity(Vector2 position, Vector2 size) {
	collider = new EntityCollider(this, position, size);
    }

    @Override
    public void initialize() {
	collider.setActive(true);
    }

    public final Vector2 getPosition() {
	return collider.getCenter();
    }

    public final void setPosition(Vector2 position) {
	collider.setCenter(position);
    }

    public Vector2 getSize() {
	return collider.getDimensions().clone();
    }

    public void setSize(Vector2 size) {
	collider.setDimensions(size);
    }

    @Override
    public void dispose() {
	collider.setActive(false);
    }

    public abstract void onCollisionEnter(Entity other);

    public abstract void onCollisionExit(Entity other);

    public static class EntityCollider extends Collider {
	private Entity entity;

	private EntityCollider(Entity entity, Vector2 position, Vector2 size) {
	    super(position, size.getX(), size.getY());
	    this.entity = entity;
	}

	public Entity getEntity() {
	    return entity;
	}

	@Override
	public void onCollisionEnter(Collider other) {
	    if (other instanceof EntityCollider) {
		Entity otherEntity = ((EntityCollider) other).getEntity();
		entity.onCollisionEnter(otherEntity);
	    }
	}

	@Override
	public void onCollisionExit(Collider other) {
	    if (other instanceof EntityCollider) {
		Entity otherEntity = ((EntityCollider) other).getEntity();
		entity.onCollisionExit(otherEntity);
	    }
	}
    }
}

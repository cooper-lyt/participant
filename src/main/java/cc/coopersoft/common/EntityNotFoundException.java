package cc.coopersoft.common;

/**
 * Created by cooper on 6/10/16.
 */
public class EntityNotFoundException extends RuntimeException{

    private static final long serialVersionUID = -3469578090343847583L;

    private Object id;

    public EntityNotFoundException(Object id)
    {
        super( String.format("entity not found: %s", id) );
        this.id = id;
    }


    public Object getId()
    {
        return id;
    }
}

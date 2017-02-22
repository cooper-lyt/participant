package cc.coopersoft.common;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by cooper on 6/19/16.
 */
public abstract class EntityConverter implements Converter, Serializable {

    @Inject
    private EntityLoader entityLoader;

    protected abstract EntityManager getEntityManager();

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0)
        {
            return null;
        }
        return entityLoader.get(getEntityManager(),value);
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null)
        {
            return null;
        }
        if (value instanceof String)
        {
            return (String) value;
        }
        return entityLoader.put(value);
    }
}

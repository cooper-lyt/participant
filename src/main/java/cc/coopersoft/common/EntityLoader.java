package cc.coopersoft.common;

import cc.coopersoft.common.util.EntityHelper;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by cooper on 6/19/16.
 */
@ViewAccessScoped
public class EntityLoader implements Serializable{

    @Inject
    private Logger logger;

    public static class Identifier implements Serializable {

        public Identifier(Class clazz, Object id) {
            if (clazz == null || id == null)
            {
                throw new IllegalArgumentException("Id and clazz must not be null");
            }
            this.clazz = clazz;
            this.id = id;
        }

        private Class clazz;
        private Object id;

        public Class getClazz() {
            return clazz;
        }

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Identifier that = (Identifier) o;

            if (clazz != null ? !clazz.equals(that.clazz) : that.clazz != null) return false;
            return id != null ? id.equals(that.id) : that.id == null;

        }

        @Override
        public int hashCode() {
            int result = clazz != null ? clazz.hashCode() : 0;
            result = 31 * result + (id != null ? id.hashCode() : 0);
            return result;
        }
    }



    private Identifier createIdentifier(Object entity)
    { return new Identifier(EntityHelper.getEntityClass(entity.getClass()), EntityHelper.getEntityId(entity));
    }

    private List<Identifier> store = new ArrayList<Identifier>();



    /**
     * Load and return the entity stored
     * @param key
     * @return The entity or null if no entity is available at that key
     */
    @Transactional
    public Object get(EntityManager em, String key)
    {

        logger.config("get key:" + key);
        Identifier identifier;
        try {
            identifier = store.get(new Integer(key));
        } catch (NumberFormatException e){
            return null;
        }
        if (identifier != null)
        {
            logger.config("find entity:" + identifier.getClazz() + "-" + identifier.getId());

                return em.find(identifier.getClazz(), identifier.getId());

        }
        else
        {
            logger.config("key not found:" + key);
            return null;
        }
    }

    /**
     * Store an entity id/clazz
     * @param entity The entity to store
     * @return The key under which the clazz/id are stored
     */
    @Transactional
    public String put(Object entity)
    {
        Identifier identifier = createIdentifier(entity);
        if (!store.contains(identifier)){
            store.add(identifier);
        }
        logger.config("put entity:" + identifier.getClazz() + "-" + identifier.getId() + "-" + ((Integer) store.indexOf(identifier)).toString());
        return ((Integer) store.indexOf(identifier)).toString();
    }


}

package cc.coopersoft.house.participant;

import org.picketlink.annotations.PicketLink;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cooper on 23/02/2017.
 */
public class EntityManagerProducer implements java.io.Serializable{

    @Inject
    private Logger logger;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Produces
    @PicketLink
    public EntityManager createPicketLinkEM(){
        return this.entityManagerFactory.createEntityManager();
    }

    public void disposePicketLinkEM(@Disposes @PicketLink EntityManager entityManager)
    {
        if (entityManager.isOpen())
        {
            entityManager.close();
        }
    }

    @Produces
    @Default
    @ConversationScoped
    public EntityManager create(){
        logger.log(Level.CONFIG,"disposes picketLink EntityManager: open");
        logger.severe("call entityManager create");
        return this.entityManagerFactory.createEntityManager();
    }


    public void dispose(@Disposes @Default EntityManager entityManager){
        if (entityManager.isOpen()){
            entityManager.close();
        }
    }
}

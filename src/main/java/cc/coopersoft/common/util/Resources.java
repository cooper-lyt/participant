package cc.coopersoft.common.util;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by cooper on 6/5/16.
 */
public class Resources {



    @Produces
    public Logger produceLog(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    @Produces
    @DefaultMessageBundle
    public ResourceBundle getBundle() {

        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getResourceBundle(context, "messages");
    }

    @Named
    @Produces
    @RequestScoped
    public FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

}

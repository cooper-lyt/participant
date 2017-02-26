package cc.coopersoft.common;

import cc.coopersoft.common.util.DefaultMessageBundle;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by cooper on 6/16/16.
 */
@Named
public class EnumHelper {

    @Inject
    private FacesContext facesContext;

    @Inject @DefaultMessageBundle
    private ResourceBundle bundle;

    public String getLabel(Enum value){
        if (value == null){
            return "";
        }
        try {
            return bundle.getString(value.getClass().getName() + "." + value.name());
        }catch (MissingResourceException e){
            return value.getClass().getName() + "." + value.name();
        }
    }
}

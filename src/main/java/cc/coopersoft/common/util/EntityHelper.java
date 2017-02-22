package cc.coopersoft.common.util;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by cooper on 6/19/16.
 */
public class EntityHelper {

    //TODO define in xml

    /**
     * Get the bean class from a container-generated proxy
     * class
     *
     */
    public static Class getEntityClass(Class clazz)
    {
        while (clazz != null && !Object.class.equals(clazz))
        {
            if (clazz.isAnnotationPresent(Entity.class))
            {
                return clazz;
            }
            else
            {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }


    public static Object getEntityId(Object entity){
        Class clazz = getEntityClass(entity.getClass());



        while (clazz != null && !Object.class.equals(clazz))
        {
            if (clazz.isAnnotationPresent(Entity.class))
            {
                for(Method method: clazz.getDeclaredMethods()){
                    if (method.isAnnotationPresent(Id.class)){
                        try {
                            return method.invoke(entity);
                        } catch (IllegalAccessException e) {
                            throw new IllegalArgumentException(e);
                        } catch (InvocationTargetException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                }

                for(Field field: clazz.getDeclaredFields()){
                    if (field.isAnnotationPresent(Id.class)){
                        try {
                            return field.get(entity);
                        } catch (IllegalAccessException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                }
            }
            else
            {
                clazz = clazz.getSuperclass();
            }
        }

        throw new IllegalArgumentException("not have define Annotation Id");

    }
}

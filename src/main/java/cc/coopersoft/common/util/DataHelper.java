package cc.coopersoft.common.util;

/**
 * Created by cooper on 6/22/16.
 */
public class DataHelper {

    public static  <U> boolean isDirty(U oldValue, U newValue)
    {
        return oldValue!=newValue && (
                oldValue==null ||
                        !oldValue.equals(newValue)
        );
    }

    public static boolean empty(String value){
        return value == null || "".equals(value.trim());
    }


}

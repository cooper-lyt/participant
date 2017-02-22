package cc.coopersoft.common;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.*;

/**
 * Created by cooper on 7/22/16.
 */

@Named
@SessionScoped
public class I18n implements java.io.Serializable{

    private Locale locale;

    @PostConstruct
    public void initParam(){
        locale = Locale.CHINA;
    }


    public Locale getLocale() {
        return locale;
    }

    public TimeZone getTimeZone(){
        return Calendar.getInstance(getLocale()).getTimeZone();
    }

    public Date getDayBeginTime(Date value){
        GregorianCalendar gc = new GregorianCalendar(getLocale());
        gc.setTime(value);
        gc.set(Calendar.HOUR_OF_DAY, 0);
        gc.set(Calendar.MINUTE, 0);
        gc.set(Calendar.SECOND, 0);
        gc.set(Calendar.MILLISECOND, 0);


        return gc.getTime();
    }

    public Date getDayEndTime(Date value){
        return new Date(getDayBeginTime(value).getTime() + 24 * 60 * 60 * 1000 - 1000);
    }
}

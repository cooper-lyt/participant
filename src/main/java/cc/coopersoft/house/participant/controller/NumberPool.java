package cc.coopersoft.house.participant.controller;

import cc.coopersoft.house.participant.data.model.NumberSequence;
import cc.coopersoft.house.participant.data.repository.NumberSequenceRepository;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by cooper on 13/10/2017.
 */
@Singleton
@Startup
public class NumberPool implements java.io.Serializable{

    @Inject
    private NumberSequenceRepository numberSequenceRepository;

    //private Map<String,Long> appNumbers = new HashMap<String, Long>();


    @PostConstruct
    public void create() {

    }

    public String getNumber(String name){
        NumberSequence ns = numberSequenceRepository.findBy(name);
        if (ns == null){
            ns = new NumberSequence(name);

        }else{
            Calendar now = GregorianCalendar.getInstance();
            now.setTime(new Date());
            Calendar ut = GregorianCalendar.getInstance();
            ut.setTime(ns.getUpdateTime());

            if (
                    (NumberSequence.SequenceType.DAY.equals(ns.getType()) && ((now.get(Calendar.YEAR) != ut.get(Calendar.YEAR)) || (now.get(Calendar.MONTH) != ut.get(Calendar.MONTH)) || (now.get(Calendar.DAY_OF_MONTH) != ut.get(Calendar.DAY_OF_MONTH)))) ||
                            (NumberSequence.SequenceType.MONTH.equals(ns.getType()) && ((now.get(Calendar.YEAR) != ut.get(Calendar.YEAR)) || (now.get(Calendar.MONTH) != ut.get(Calendar.MONTH)))) ||
                            (NumberSequence.SequenceType.YEAR.equals(ns.getType()) && (now.get(Calendar.YEAR) != ut.get(Calendar.YEAR)))
                    ){
                ns.setNumber(1l);
            }else {
                ns.setNumber(ns.getNumber() + 1l);
            }
            ns.setUpdateTime(now.getTime());
        }

        String resultNumber = String.valueOf(ns.getNumber());
        while (resultNumber.length() < ns.getMinLength()){
            resultNumber = "0" + resultNumber;
        }

        numberSequenceRepository.save(ns);
        SimpleDateFormat numberDateFormat;
        switch (ns.getType()){

            case DAY:
                numberDateFormat = new SimpleDateFormat("yyyyMMdd");
                return numberDateFormat.format(new Date()) + resultNumber;
            case MONTH:
                numberDateFormat = new SimpleDateFormat("yyyyMM");
                return numberDateFormat.format(new Date()) + resultNumber;
            case YEAR:
                numberDateFormat = new SimpleDateFormat("yyyy");
                return numberDateFormat.format(new Date()) + resultNumber;
            default:
                return resultNumber;
        }

    }

//    public String getAppNumber(String name){
//
//    }

}

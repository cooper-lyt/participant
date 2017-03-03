package cc.coopersoft.house.participant.controller;

import cc.coopersoft.house.sale.data.HouseValidInfo;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * Created by cooper on 02/03/2017.
 */
public class HouseValidInfoProducer {

    @Produces
    @ViewAccessScoped
    @Named("houseValidInfo")
    public HouseValidInfo createHouseValidInfo(){
        return new HouseValidInfo(HouseValidInfo.ValidType.MBBH_NUMBER);
    }


}

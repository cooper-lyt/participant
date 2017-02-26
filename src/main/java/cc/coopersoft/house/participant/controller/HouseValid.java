package cc.coopersoft.house.participant.controller;

import cc.coopersoft.house.participant.annotations.Seller;
import cc.coopersoft.house.participant.data.HouseValidInfo;
import cc.coopersoft.house.participant.service.HouseSaleApply;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by cooper on 22/02/2017.
 */
@Named
@RequestScoped
public class HouseValid {

    @Inject
    private HouseSaleApply houseSaleApply;

    private HouseValidInfo houseValidInfo = new HouseValidInfo(HouseValidInfo.ValidType.MBBH_NUMBER);

    public HouseValidInfo getHouseValidInfo() {
        return houseValidInfo;
    }

    @Seller
    public Class<? extends ViewConfig> validHouse(){
        houseSaleApply.validSaleHouse(houseValidInfo) ;

        return cc.coopersoft.house.participant.pages.Seller.Apply.HouseSellInfo.class;
    }

}

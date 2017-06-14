package cc.coopersoft.house.participant.controller;

import cc.coopersoft.house.participant.annotations.Seller;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by cooper on 13/06/2017.
 */
@Named
public class HouseSaleInfoEdit implements java.io.Serializable{


    @Inject
    private HouseSourceHome houseSourceHome;


    @Seller
    @Transactional
    public Class<? extends ViewConfig> saveHouseSource(){
        //houseSaleInfo = houseSellRepository.save(houseSaleInfo);
        houseSourceHome.save();
        return cc.coopersoft.house.participant.pages.Seller.Apply.HouseSalePicUpload.class;
    }

}

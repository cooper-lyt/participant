package cc.coopersoft.house.participant.controller;

import cc.coopersoft.house.participant.Messages;
import cc.coopersoft.house.participant.annotations.Seller;
import cc.coopersoft.house.participant.data.model.HouseSource;
import cc.coopersoft.house.participant.service.HouseSaleApply;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.jsf.api.message.JsfMessage;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by cooper on 22/02/2017.
 */
@Named
@RequestScoped
public class HouseValid implements java.io.Serializable{

    @Inject
    private HouseSaleApply houseSaleApply;

    @Inject
    private HouseValidInfo houseValidInfo;

    @Inject
    private JsfMessage<Messages> messages;

    private HouseSource houseSource;

    public HouseValidInfo getHouseValidInfo() {
        return houseValidInfo;
    }

    public HouseSource getHouseSource() {
        return houseSource;
    }

    @Seller
    public Class<? extends ViewConfig> validHouse(){

        HouseSaleApply.ValidResult validResult = houseSaleApply.validSaleHouse(houseValidInfo);
        switch (validResult.getValidStatus()) {
            case SUCCESS:
                houseSource = validResult.getHouseSource();
                return cc.coopersoft.house.participant.pages.Seller.Apply.HouseSellInfo.class;
            case HOUSE_NOT_FOUND:
                 messages.addError().validHouseNotFound();
                break;
            case OWNER_FAIL:
                messages.addError().validHouesOwnerFail();
                break;
            case ERROR:
                messages.addError().validHouseError();
                break;
        }

        return null;

    }

}

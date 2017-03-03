package cc.coopersoft.house.participant.controller;

import cc.coopersoft.comm.exception.HttpApiServerException;
import cc.coopersoft.common.EnumHelper;
import cc.coopersoft.house.participant.Messages;
import cc.coopersoft.house.participant.annotations.Seller;
import cc.coopersoft.house.sale.HouseSellService;
import cc.coopersoft.house.sale.data.HouseSaleInfo;
import cc.coopersoft.house.sale.data.HouseValidInfo;
import cc.coopersoft.house.sale.data.HouseValidResult;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.jsf.api.message.JsfMessage;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

/**
 * Created by cooper on 22/02/2017.
 */
@Named
@RequestScoped
public class HouseValid implements java.io.Serializable{


    @Inject
    private HouseValidInfo houseValidInfo;

    @Inject
    private JsfMessage<Messages> messages;

    @Inject
    private FacesContext facesContext;

    @Inject
    private Logger logger;

    @Inject
    private RunParam runParam;

    @Inject
    private EnumHelper enumHelper;

    private HouseSaleInfo houseSaleInfo;

    public HouseValidInfo getHouseValidInfo() {
        return houseValidInfo;
    }

    public HouseSaleInfo getHouseSaleInfo() {
        return houseSaleInfo;
    }

    @Seller
    public Class<? extends ViewConfig> validHouse(){

        try {
            HouseValidResult result = HouseSellService.houseValid(runParam.getStringParam("nginx_address"),houseValidInfo);
            switch (result.getValidStatus()){

                case SUCCESS:
                    if (result.getLimits().isEmpty()){
                        houseSaleInfo = result.getHouseSaleInfo();
                        return cc.coopersoft.house.participant.pages.Seller.Apply.HouseSellInfo.class;
                    }else{
                        for (HouseValidResult.Limit limit: result.getLimits()){
                            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,enumHelper.getLabel(limit.getLockType()),limit.getDescription()));
                        }
                    }
                    logger.config(result.getHouseSaleInfo().getHouseCode());
                    break;
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
        } catch (HttpApiServerException e) {
            messages.addError().serverFail();
        }

//
//        HouseSaleApply.ValidResult validResult = houseSaleApply.validSaleHouse(houseValidInfo);
//        switch (validResult.getValidStatus()) {
//            case SUCCESS:
//                houseSaleInfo = validResult.getHouseSource().getHouseSaleInfo();
//                return cc.coopersoft.house.participant.pages.Seller.Apply.HouseSellInfo.class;
//            case HOUSE_NOT_FOUND:
//                 messages.addError().validHouseNotFound();
//                break;
//            case OWNER_FAIL:
//                messages.addError().validHouesOwnerFail();
//                break;
//            case ERROR:
//                messages.addError().validHouseError();
//                break;
//        }

        return null;

    }

}

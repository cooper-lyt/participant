package cc.coopersoft.house.participant.controller;

import cc.coopersoft.comm.exception.HttpApiServerException;
import cc.coopersoft.common.EnumHelper;
import cc.coopersoft.house.SaleLimitType;
import cc.coopersoft.house.participant.AttrUser;
import cc.coopersoft.house.participant.Messages;
import cc.coopersoft.house.participant.annotations.Seller;
import cc.coopersoft.house.participant.data.repository.HouseSourceRepository;
import cc.coopersoft.house.participant.service.HouseSourceService;
import cc.coopersoft.house.sale.data.*;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.deltaspike.jsf.api.message.JsfMessage;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cooper on 22/02/2017.
 */
@Named
@RequestScoped
public class HouseValid implements java.io.Serializable{

    @Inject
    @Default
    private Conversation conversation;

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

    @Inject
    private HouseSourceRepository houseSourceRepository;


    @Inject
    private AttrUser attrUser;

    @Inject
    private HouseSourceHome houseSourceHome;

    @Inject
    private HouseSourceService houseSourceService;

    //private HouseSaleInfo houseSaleInfo;

    public HouseValidInfo getHouseValidInfo() {
        return houseValidInfo;
    }

    private void addLimitMessages(List<SellLimit> limits){
        for (SellLimit limit : limits) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, enumHelper.getLabel(limit.getLimitType()), limit.getDescription()));
        }
    }



    @Seller
    @Transactional
    public Class<? extends ViewConfig> saveHouseSource(){
        //houseSaleInfo = houseSellRepository.save(houseSaleInfo);
        logger.config("source Id:" + houseSourceHome.getInstance().getId() + " | sale info ID:" + houseSourceHome.getInstance().getId());
        houseSourceService.updateHouseSourceByHouse(houseSourceHome.getInstance().getHouseCode());
        try {
            Class<? extends ViewConfig> resultPath = null;
            HouseSource passHouseSource = houseSourceService.existsPassHouseSource(houseSourceHome.getInstance().getHouseCode());
            if (passHouseSource != null ){
                messages.addInfo().houseSourceExists();
                houseSourceHome.clearInstance();
                houseSourceHome.setId(passHouseSource.getId());

                resultPath = cc.coopersoft.house.participant.pages.Seller.HouseSourceView.class;
            }else{

                HouseValidResult result = houseSourceService.validHouseSource(houseValidInfo);
                switch (result.getValidStatus()){

                    case SUCCESS:
                        if (result.getLimits().isEmpty()){
                            houseSourceHome.save();
                            resultPath = cc.coopersoft.house.participant.pages.Seller.Apply.HouseSalePicUpload.class;
                        }else{
                            addLimitMessages(result.getLimits());
                            houseSourceHome.clearInstance();
                            resultPath = cc.coopersoft.house.participant.pages.Seller.Apply.HouseValid.class;
                        }
                        break;
                    case HOUSE_NOT_FOUND:
                        messages.addError().validHouseNotFound();
                        houseSourceHome.clearInstance();
                        resultPath = cc.coopersoft.house.participant.pages.Seller.Apply.HouseValid.class;
                        break;
                    case OWNER_FAIL:

                        messages.addError().validHouseOwnerFail();
                        houseSourceHome.clearInstance();
                        resultPath = cc.coopersoft.house.participant.pages.Seller.Apply.HouseValid.class;
                        break;
                    case ERROR:
                        messages.addError().validHouseError();
                        break;
                }
            }

            endConversation();
            return resultPath;

        } catch (HttpApiServerException e) {
            logger.log(Level.WARNING,e.getMessage(),e);
            messages.addError().serverFail();
            return null;
        }



    }

    @Seller
    @Transactional
    public Class<? extends ViewConfig> validHouse(){

        try {
            HouseValidResult result = houseSourceService.validHouseSource(houseValidInfo);
            switch (result.getValidStatus()){

                case SUCCESS:
                    houseSourceService.updateHouseSourceByHouse(result.getHouseSource().getHouseCode());

                    HouseSource passHouseSource = houseSourceService.existsPassHouseSource(result.getHouseSource().getHouseCode());
                    if (passHouseSource != null ){
                        messages.addInfo().houseSourceExists();
                        houseSourceHome.setId(passHouseSource.getId());
                        return cc.coopersoft.house.participant.pages.Seller.HouseSourceView.class;
                    }



                    if (result.getLimits().isEmpty()) {

                        HouseSource editHouseSource = houseSourceService.existsEditHouseSource(result.getHouseSource().getHouseCode(),attrUser.getLoginData().getCorpInfo().getId());
                        if (editHouseSource != null){
                            houseSourceHome.setId(editHouseSource.getId());
                            messages.addInfo().houseSourceToEdit();
                            return cc.coopersoft.house.participant.pages.Seller.Apply.HouseSellInfo.class;
                        }

                        houseSourceHome.setInstance(result.getHouseSource());

                        houseSourceHome.getInstance().setId(UUID.randomUUID().toString().replace("-",""));
                        houseSourceHome.getInstance().getHouseSaleInfo().setId(houseSourceHome.getInstance().getId());
                        houseSourceHome.getInstance().setGroupId(attrUser.getLoginData().getCorpInfo().getId());
                        houseSourceHome.getInstance().getHouseSourceCompanies().add(
                            new HouseSourceCompany(UUID.randomUUID().toString().replace("-",""),attrUser.getLoginData().getCorpInfo().getId(),houseSourceHome.getInstance()));
                        houseSourceHome.getInstance().setPowerCardNumber(houseValidInfo.getPowerCardNumber());
                        houseSourceHome.getInstance().setCredentialsNumber(houseValidInfo.getCredentialsNumber());
                        houseSourceHome.getInstance().setCredentialsType(houseValidInfo.getCredentialsType());
                        houseSourceHome.getInstance().setPersonName(houseValidInfo.getPersonName());
                        houseSourceHome.getInstance().setTel(houseValidInfo.getTel());

                        //houseSaleInfoHome.getInstance().getHouseSource().setHouseSaleInfo(houseSaleInfoHome.getInstance());
                        houseSourceHome.getInstance().setSaleType(HouseSource.SaleType.SELLER);
                        houseSourceHome.getInstance().setApplyTime(new Date());
                        houseSourceHome.getInstance().setCheckTime(new Date());
                        houseSourceHome.getInstance().setStatus(HouseSource.HouseSourceStatus.PREPARE);

                        if ( conversation.isTransient() )
                        {

                            conversation.begin();
                            conversation.setTimeout(1200000);
                        }

                        return cc.coopersoft.house.participant.pages.Seller.Apply.HouseSellInfo.class;
                    } else {
                        addLimitMessages(result.getLimits());
                    }

                    logger.config(result.getHouseSource().getHouseCode());
                    break;
                case HOUSE_NOT_FOUND:
                    messages.addError().validHouseNotFound();
                    break;
                case OWNER_FAIL:

                    messages.addError().validHouseOwnerFail();
                    break;
                case ERROR:
                    messages.addError().validHouseError();
                    break;
            }
        } catch (HttpApiServerException e) {
            logger.log(Level.WARNING,e.getMessage(),e);
            messages.addError().serverFail();
        }

        return null;

    }


    public void endConversation() {
        if ( !conversation.isTransient() )
        {
            conversation.end();
        }
    }

}

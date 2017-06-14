package cc.coopersoft.house.participant.controller;

import cc.coopersoft.comm.exception.HttpApiServerException;
import cc.coopersoft.common.EnumHelper;
import cc.coopersoft.house.participant.AttrUser;
import cc.coopersoft.house.participant.Messages;
import cc.coopersoft.house.participant.annotations.Seller;
import cc.coopersoft.house.participant.data.repository.HouseSourceRepository;
import cc.coopersoft.house.sale.HouseSellService;
import cc.coopersoft.house.sale.data.*;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.deltaspike.jsf.api.message.JsfMessage;

import javax.annotation.PreDestroy;
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

    //private HouseSaleInfo houseSaleInfo;

    public HouseValidInfo getHouseValidInfo() {
        return houseValidInfo;
    }

    private void addLimitMessages(List<HouseValidResult.Limit> limits){
        for (HouseValidResult.Limit limit : limits) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, enumHelper.getLabel(limit.getLockType()), limit.getDescription()));
        }
    }



    @Seller
    @Transactional
    public Class<? extends ViewConfig> saveHouseSource(){
        //houseSaleInfo = houseSellRepository.save(houseSaleInfo);
        logger.config("source Id:" + houseSourceHome.getInstance().getId() + " | sale info ID:" + houseSourceHome.getInstance().getId());
        houseSourceHome.save();
        endConversation();
        return cc.coopersoft.house.participant.pages.Seller.Apply.HouseSalePicUpload.class;
    }

    @Seller
    @Transactional
    public Class<? extends ViewConfig> validHouse(){

        try {
            HouseValidResult result = HouseSellService.houseValid(runParam.getStringParam("nginx_address"),houseValidInfo);
            switch (result.getValidStatus()){

                case SUCCESS:
                    houseSourceHome.setInstance(result.getHouseSource());
                    List<HouseSource> sellInfo =
                            houseSourceRepository.houseSourceByStatus(new ArrayList<HouseSource.HouseSourceStatus>(EnumSet.of(HouseSource.HouseSourceStatus.CHECK,HouseSource.HouseSourceStatus.SHOWING,HouseSource.HouseSourceStatus.CHECK_PASS)), houseSourceHome.getInstance().getHouseCode());
                    if (sellInfo.isEmpty()) {
                        if (result.getLimits().isEmpty()) {

                            houseSourceHome.getInstance().setId(UUID.randomUUID().toString().replace("-",""));
                            houseSourceHome.getInstance().getHouseSaleInfo().setId(houseSourceHome.getInstance().getId());
                            houseSourceHome.getInstance().setGroupId(attrUser.getLoginData().getCorpInfo().getId());

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
                    }else{
                        houseSourceHome.setId(sellInfo.get(0).getId());
                       // houseSaleInfo = sellInfo.get(0);
                        if (result.getLimits().isEmpty()) {
                            return cc.coopersoft.house.participant.pages.Seller.Apply.HouseSaleJoin.class;
                        }else{
                            addLimitMessages(result.getLimits());
                            houseSourceHome.getInstance().setStatus(HouseSource.HouseSourceStatus.CANCEL);
                            houseSourceHome.getInstance().setCheckView(result.getLimits().get(0).getDescription());
                            houseSourceHome.saveOrUpdate();
                        }
                    }
                    logger.config(result.getHouseSource().getHouseCode());
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

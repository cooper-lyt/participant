package cc.coopersoft.house.participant.controller;

import cc.coopersoft.comm.exception.HttpApiServerException;
import cc.coopersoft.common.EnumHelper;
import cc.coopersoft.house.participant.AttrUser;
import cc.coopersoft.house.participant.Messages;
import cc.coopersoft.house.participant.annotations.Seller;
import cc.coopersoft.house.participant.data.repository.HouseSellRepository;
import cc.coopersoft.house.sale.HouseSellService;
import cc.coopersoft.house.sale.data.*;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.deltaspike.jsf.api.message.JsfMessage;

import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
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
@ConversationScoped
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
    private HouseSellRepository houseSellRepository;

    @Inject
    private SaleAreaCache saleAreaCache;

    @Inject
    private AttrUser attrUser;

    private HouseSaleInfo houseSaleInfo;

    public HouseValidInfo getHouseValidInfo() {
        return houseValidInfo;
    }

    public HouseSaleInfo getHouseSaleInfo() {
        return houseSaleInfo;
    }

    private void addLimitMessages(List<HouseValidResult.Limit> limits){
        for (HouseValidResult.Limit limit : limits) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, enumHelper.getLabel(limit.getLockType()), limit.getDescription()));
        }
    }

    public List<SaleArea> getSchoolAreaList(){
        return saleAreaCache.getSaleAreas(SaleArea.SaleAreaType.SCHOOL,houseSaleInfo.getDistrict(),false);
    }

    public List<SaleArea> getSaleLocalAreaList(){
        return saleAreaCache.getSaleAreas(SaleArea.SaleAreaType.SALE,houseSaleInfo.getDistrict(),false);
    }

    @Seller
    @Transactional
    public Class<? extends ViewConfig> saveHouseSource(){
        houseSaleInfo = houseSellRepository.save(houseSaleInfo);
        return cc.coopersoft.house.participant.pages.Seller.Apply.HouseSalePicUpload.class;
    }

    @Seller
    @Transactional
    public Class<? extends ViewConfig> validHouse(){

        try {
            HouseValidResult result = HouseSellService.houseValid(runParam.getStringParam("nginx_address"),houseValidInfo);
            switch (result.getValidStatus()){

                case SUCCESS:
                    houseSaleInfo = result.getHouseSaleInfo();
                    List<HouseSaleInfo> sellInfo =
                            houseSellRepository.houseSalebyStatus(new ArrayList<HouseSaleInfo.HouseSourceStatus>(EnumSet.of(HouseSaleInfo.HouseSourceStatus.CHECK,HouseSaleInfo.HouseSourceStatus.SHOWING,HouseSaleInfo.HouseSourceStatus.CHECK_PASS)),houseSaleInfo.getHouseCode());
                    if (sellInfo.isEmpty()) {
                        if (result.getLimits().isEmpty()) {
                            houseSaleInfo.setHouseSource(new HouseSource(UUID.randomUUID().toString().replace("-",""),attrUser.getLoginData().getCorpInfo().getId(),houseValidInfo,houseSaleInfo));
                            houseSaleInfo.setId(houseSaleInfo.getHouseSource().getId());
                            houseSaleInfo.getHouseSource().setHouseSaleInfo(houseSaleInfo);
                            houseSaleInfo.setSaleType(HouseSaleInfo.SaleType.SELLER);
                            houseSaleInfo.setApplyTime(new Date());
                            houseSaleInfo.setCheckTime(new Date());
                            houseSaleInfo.setStatus(HouseSaleInfo.HouseSourceStatus.PREPARE);

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

                        houseSaleInfo = sellInfo.get(0);
                        if (result.getLimits().isEmpty()) {
                            return cc.coopersoft.house.participant.pages.Seller.Apply.HouseSaleJoin.class;
                        }else{
                            addLimitMessages(result.getLimits());
                            houseSaleInfo.setStatus(HouseSaleInfo.HouseSourceStatus.CANCEL);
                            houseSaleInfo.getHouseSource().setCheckView(result.getLimits().get(0).getDescription());
                            houseSellRepository.save(houseSaleInfo);
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
            logger.log(Level.WARNING,e.getMessage(),e);
            messages.addError().serverFail();
        }

        return null;

    }


    @PreDestroy
    public void endConversation() {
        if ( !conversation.isTransient() )
        {
            conversation.end();
        }
    }

}

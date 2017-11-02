package cc.coopersoft.house.participant.controller;

import cc.coopersoft.comm.exception.HttpApiServerException;
import cc.coopersoft.common.EnumHelper;
import cc.coopersoft.house.participant.AttrUser;
import cc.coopersoft.house.participant.Messages;
import cc.coopersoft.house.participant.annotations.Seller;
import cc.coopersoft.house.participant.data.ContractContextMap;
import cc.coopersoft.house.participant.service.HouseSourceService;
import cc.coopersoft.house.sale.data.*;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.deltaspike.jsf.api.message.JsfMessage;
import org.json.JSONException;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cooper on 29/10/2017.
 */
@Named
@RequestScoped
public class HouseSourceCreate {


    @Inject
    private EnumHelper enumHelper;

    @Inject
    @Default
    private Conversation conversation;

    @Inject
    private FacesContext facesContext;

    @Inject
    private HouseSourceService houseSourceService;

    @Inject
    private JsfMessage<Messages> messages;

    @Inject
    private HouseSourceHome houseSourceHome;

    @Inject
    private AttrUser attrUser;

    @Inject
    private HouseValidInfo houseValidInfo;

    @Inject
    protected Logger logger;

    private void endConversation() {
        if ( !conversation.isTransient() )
        {
            conversation.end();
        }
    }

    private void addLimitMessages(List<SellLimit> limits){
        for (SellLimit limit : limits) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, enumHelper.getLabel(limit.getLimitType()), limit.getDescription()));
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
                        houseSourceHome.getInstance().getHouseSaleInfo().setShowAreaType(HouseSaleInfo.ShowAreaType.TO_SELL);
                        houseSourceHome.getInstance().setGroupId(attrUser.getLoginData().getCorpInfo().getId());


                        houseSourceHome.getInstance().setPowerCardNumber(houseValidInfo.getPowerCardNumber());
                        houseSourceHome.getInstance().setCredentialsNumber(houseValidInfo.getCredentialsNumber());
                        houseSourceHome.getInstance().setCredentialsType(houseValidInfo.getCredentialsType());
                        houseSourceHome.getInstance().setPersonName(houseValidInfo.getPersonName());
                        houseSourceHome.getInstance().setPowerCardType(houseValidInfo.getPowerCardType());

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

    @Seller
    public Class<? extends ViewConfig> toShowInfo(){
        return cc.coopersoft.house.participant.pages.Seller.Apply.HouseShowInfo.class;
    }

    @Seller
    @Transactional
    public Class<? extends ViewConfig> createHouseSource(){

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



                HouseValidResult result = houseSourceService.validHouseSource(houseSourceHome.getInstance());
                switch (result.getValidStatus()){

                    case SUCCESS:
                        if (result.getLimits().isEmpty()){
                            //TODO contractId
                            HouseSourceCompany hc = new HouseSourceCompany(UUID.randomUUID().toString().replace("-",""),attrUser.getLoginData().getCorpInfo().getId(),houseSourceHome.getInstance());

                            ContractContextMap contractContextMap = new ContractContextMap();
                            contractContextMap.put("seller_name",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getPersonName()));
                            contractContextMap.put("seller_id_type",new ContractContextMap.ContarctContextItem(enumHelper.getLabel(houseSourceHome.getInstance().getCredentialsType())));
                            contractContextMap.put("seller_id_number",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getCredentialsNumber()));
                            contractContextMap.put("seller_address",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getAddress()));
                            contractContextMap.put("seller_tel",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getTel()));

                            if (houseSourceHome.getInstance().getProxyPerson() != null){
                                contractContextMap.put("seller_proxy_name",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getProxyPerson().getPersonName()));
                                contractContextMap.put("seller_proxy_id_type",new ContractContextMap.ContarctContextItem(enumHelper.getLabel(houseSourceHome.getInstance().getProxyPerson().getCredentialsType())));
                                contractContextMap.put("seller_proxy_id_number",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getProxyPerson().getCredentialsNumber()));
                                contractContextMap.put("seller_proxy_tel",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getProxyPerson().getPhone()));
                                contractContextMap.put("seller_proxy_address",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getProxyPerson().getAddress()));
                            }

                            contractContextMap.put("group_name",new ContractContextMap.ContarctContextItem(attrUser.getLoginData().getCorpInfo().getName()));
                            contractContextMap.put("group_owner",new ContractContextMap.ContarctContextItem(attrUser.getLoginData().getCorpInfo().getOwnerName()));
                            contractContextMap.put("group_owner_type",new ContractContextMap.ContarctContextItem(enumHelper.getLabel(attrUser.getLoginData().getCorpInfo().getLegalType())));
                            contractContextMap.put("group_cer_type",new ContractContextMap.ContarctContextItem(enumHelper.getLabel(attrUser.getLoginData().getCorpInfo().getCredentialsType())));
                            contractContextMap.put("group_cer_number",new ContractContextMap.ContarctContextItem(attrUser.getLoginData().getCorpInfo().getCredentialsNumber()));
                            contractContextMap.put("group_id",new ContractContextMap.ContarctContextItem(attrUser.getLoginData().getCorpInfo().getId()));
                            contractContextMap.put("group_address",new ContractContextMap.ContarctContextItem(attrUser.getLoginData().getCorpInfo().getAddress()));
                            contractContextMap.put("group_tel",new ContractContextMap.ContarctContextItem(attrUser.getLoginData().getCorpInfo().getTel()));
                            contractContextMap.put("allow_join", new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().isAllowJoin()?"放弃":"保留"));

                            contractContextMap.put("house_card_type", new ContractContextMap.ContarctContextItem(enumHelper.getLabel(houseSourceHome.getInstance().getPowerCardType())));
                            contractContextMap.put("house_card_number",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getCredentialsNumber()));
                            contractContextMap.put("house_design_type",new ContractContextMap.ContarctContextItem(enumHelper.getLabel(houseSourceHome.getInstance().getHouseSaleInfo().getUseType())));

                            contractContextMap.put("house_address",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getHouseSaleInfo().getAddress()));
                            contractContextMap.put("house_area",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getHouseSaleInfo().getHouseArea()));
                            contractContextMap.put("house_in_floor",new ContractContextMap.ContarctContextItem(String.valueOf(houseSourceHome.getInstance().getHouseSaleInfo().getInFloor())));
                            contractContextMap.put("house_floor_count",new ContractContextMap.ContarctContextItem(String.valueOf(houseSourceHome.getInstance().getHouseSaleInfo().getFloorCount())));
                            contractContextMap.put("house_elevator",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getHouseSaleInfo().isElevator()?"有":"无"));
                            contractContextMap.put("house_living_count",new ContractContextMap.ContarctContextItem(String.valueOf(houseSourceHome.getInstance().getHouseSaleInfo().getLivingRoom())));
                            contractContextMap.put("house_room_count",new ContractContextMap.ContarctContextItem(String.valueOf(houseSourceHome.getInstance().getHouseSaleInfo().getRoomCount())));
                            contractContextMap.put("house_kitchen_count",new ContractContextMap.ContarctContextItem(String.valueOf(houseSourceHome.getInstance().getHouseSaleInfo().getKitchenCount())));
                            contractContextMap.put("house_toilet_count",new ContractContextMap.ContarctContextItem(String.valueOf(houseSourceHome.getInstance().getHouseSaleInfo().getToiletCount())));
                            contractContextMap.put("house_direction",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getHouseSaleInfo().getDirection()));
                            contractContextMap.put("house_price",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getHouseSaleInfo().getSumPrice()));


                            if(HouseSaleInfo.ShowAreaType.TO_END_TIME.equals(houseSourceHome.getInstance().getHouseSaleInfo().getShowAreaType())){
                                contractContextMap.put("time_limit_begin", new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getApplyTime()));
                                contractContextMap.put("time_limit_end",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getHouseSaleInfo().getEndTime()));
                            }



                            try {
                                hc.setContext(contractContextMap.toJson().toString());
                            } catch (JSONException e) {
                                throw new IllegalArgumentException(e.getMessage(),e);
                            }
                            houseSourceHome.getInstance().getHouseSourceCompanies().add(hc);


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

}

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
import javax.persistence.Transient;
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

    @Inject
    private ServerWord serverWord;

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

    private boolean sourceNext(HouseValidResult result){
        HouseSource editHouseSource = houseSourceService.existsHouseSource(result.getHouseSource().getHouseCode(),attrUser.getLoginData().getCorpInfo().getId());
        if (editHouseSource != null){
            houseSourceHome.setId(editHouseSource.getId());
            messages.addInfo().houseSourceToEdit();
            return false;
        }

        HouseSource passHouseSource = houseSourceService.existsPassHouseSource(result.getHouseSource().getHouseCode());
        if (passHouseSource != null ){
            houseSourceHome.setId(passHouseSource.getId());

            if (passHouseSource.isAllowJoin()) {
                houseSourceHome.getInstance().getHouseSourceCompanies().add(new HouseSourceCompany(UUID.randomUUID().toString().replace("-", ""), attrUser.getLoginData().getCorpInfo().getId(), houseSourceHome.getInstance()));
                houseSourceHome.save();
                messages.addInfo().houseSourceExists();
            }else{
                messages.addInfo().houseSourceOnlyExists();
            }

            return false;
        }


        houseSourceHome.setInstance(result.getHouseSource());

        houseSourceHome.getInstance().setId(UUID.randomUUID().toString().replace("-",""));
        houseSourceHome.getInstance().setGroupId(attrUser.getLoginData().getCorpInfo().getId());

        houseSourceHome.getInstance().getHouseSourceCompanies().add(new HouseSourceCompany(UUID.randomUUID().toString().replace("-",""),attrUser.getLoginData().getCorpInfo().getId(),houseSourceHome.getInstance()));

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

        return true;
    }

    @Seller
    @Transient
    public  Class<? extends  ViewConfig> createHouse(){
        houseSourceHome.getInstance().setAddress(serverWord.getDistrictName(houseSourceHome.getInstance().getDistrict()) + "街" + houseSourceHome.getInstance().getWei() + "委" + houseSourceHome.getInstance().getSectionName() + "小区" + houseSourceHome.getInstance().getBuildNumber() + "号楼" + houseSourceHome.getInstance().getHouseOrder() + "室");
        houseSourceHome.getInstance().setBlockNumber("-");
        houseSourceHome.getInstance().setMapNumber("-");
        if (houseSourceHome.getInstance().getDesignUseType() == null || "".equals(houseSourceHome.getInstance().getDesignUseType().trim())){
            houseSourceHome.getInstance().setDesignUseType(enumHelper.getLabel(houseSourceHome.getInstance().getUseType()));
        }
        return cc.coopersoft.house.participant.pages.Seller.Apply.HouseSellInfo.class;

    }

    @Seller
    @Transactional
    public Class<? extends ViewConfig> validHouse(){

        try {
            HouseValidResult result = houseSourceService.validHouseSource(houseValidInfo);
            switch (result.getValidStatus()){
                case LIMIT:
                    addLimitMessages(result.getLimits());

                case SUCCESS:

                    if (sourceNext(result)){
                        return cc.coopersoft.house.participant.pages.Seller.Apply.HouseSellInfo.class;
                    }else{
                        return cc.coopersoft.house.participant.pages.Seller.HouseSourceView.class;
                    }


                case HOUSE_NOT_FOUND:
                    if (result.getHouseSource() != null){
                        if (sourceNext(result)){
                            return cc.coopersoft.house.participant.pages.Seller.Apply.HouseSourceCreate.class;
                        }else{
                            return cc.coopersoft.house.participant.pages.Seller.HouseSourceView.class;
                        }

                    }else
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

    public void saveHouseSourceInfo(){

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
        contractContextMap.put("house_card_number",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getPowerCardNumber()));
        contractContextMap.put("house_design_type",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getDesignUseType()));

        contractContextMap.put("house_address",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getAddress()));
        contractContextMap.put("house_area",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getHouseArea()));
        contractContextMap.put("use_area",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getUseArea()));
        contractContextMap.put("comm_area", new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getHouseArea().subtract(houseSourceHome.getInstance().getUseArea())));

        contractContextMap.put("time_limit_begin", new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getApplyTime()));




        try {
            houseSourceHome.getHouseSourceCompany().setContext(contractContextMap.toJson().toString());
        } catch (JSONException e) {
            throw new IllegalArgumentException(e.getMessage(),e);
        }

        houseSourceHome.saveOrUpdate();
    }

    @Seller
    @Transactional
    public Class<? extends ViewConfig> toShowInfo(){

        saveHouseSourceInfo();
        endConversation();
        return cc.coopersoft.house.participant.pages.Seller.Apply.HouseShowInfo.class;
    }


}

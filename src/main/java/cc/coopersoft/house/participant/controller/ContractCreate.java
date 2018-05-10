package cc.coopersoft.house.participant.controller;


import cc.coopersoft.comm.HttpJsonDataGet;
import cc.coopersoft.comm.exception.HttpApiServerException;
import cc.coopersoft.common.EnumHelper;
import cc.coopersoft.house.SubmitType;
import cc.coopersoft.house.participant.AttrUser;
import cc.coopersoft.house.participant.Messages;
import cc.coopersoft.house.participant.data.ContractContextMap;
import cc.coopersoft.house.participant.pages.Seller;
import cc.coopersoft.house.sale.data.*;
import com.dgsoft.developersale.wsinterface.DESUtil;
import com.dgsoft.house.OwnerShareCalcType;
import com.dgsoft.house.SaleType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.deltaspike.jsf.api.message.JsfMessage;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Default;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cooper on 27/09/2017.
 */
@Named
@ConversationScoped
public class ContractCreate implements java.io.Serializable{

    @Inject
    @Default
    private Conversation conversation;

    @Inject
    protected Logger logger;


    @Inject
    private JsfMessage<Messages> messages;

    @Inject
    private HouseSourceHome houseSourceHome;

    @Inject
    private ContractHome contractHome;

    @Inject
    private RunParam runParam;

    @Inject
    private EnumHelper enumHelper;

    @Inject
    private LocalContractConfig localContractConfig;

    @Inject
    private AttrUser attrUser;

    @Inject
    private ServerToken serverToken;

    @Inject
    private FacesContext facesContext;



    @Inject
    private HouseSourceCreate houseSourceCreate;

    private OwnerShareCalcType ownerShareCalcType;



    @cc.coopersoft.house.participant.annotations.Seller
    @Transactional
    public Class<? extends ViewConfig> deleteContract(){
        if (HouseContract.ContractStatus.PREPARE.equals(houseSourceHome.getHouseSourceCompany().getHouseContract().getStatus())){
            contractHome.setId(houseSourceHome.getHouseSourceCompany().getHouseContract().getId());
            houseSourceHome.getHouseSourceCompany().setHouseContract(null);

            houseSourceHome.save();
            contractHome.remove();


            return Seller.HouseSourceView.class;
        }else {
            throw new IllegalArgumentException("contract status error!");
        }
    }

    @cc.coopersoft.house.participant.annotations.Seller
    @Transactional
    public Class<? extends ViewConfig> submitContract(){

        contractHome.putContractContext();


        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> params = new HashMap<String, String>(1);
        try {

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            localContractConfig.getConfig().pdf(contractHome.getContractContextMap(),buffer,contractHome.getInstance().getContractVersion());

            buffer.close();
            //logger.config("begin upload file;");

            contractHome.getInstance().setFileId (HttpJsonDataGet.putFile(runParam.getStringParam("nginx_address") + "/api/protected/put-media",buffer.toByteArray(),attrUser.getKeyId(),serverToken.getRndData(),serverToken.getDigest()));





            String data = mapper.writeValueAsString(contractHome.getInstance());
            //logger.config(data);
            params.put("data", DESUtil.encrypt(data, attrUser.getLoginData().getToken()));
            SubmitResult result = HttpJsonDataGet.postData(runParam.getStringParam("server_address") + "interfaces/extends/contract/" + SubmitType.SALE_CONTRACT.name() + "/" + attrUser.getLoginData().getKey(),params, SubmitResult.class);



            switch (result.getStatus()){

                case SUCCESS:
                    contractHome.getInstance().setStatus(HouseContract.ContractStatus.SUBMIT);
                    contractHome.getInstance().setCommitTime(new Date());

                    messages.addInfo().contractCommited();

                    houseSourceHome.getInstance().setStatus(HouseSource.HouseSourceStatus.SUBMIT);
                    contractHome.getInstance().setCommitTime(new Date());
                    contractHome.getInstance().setStatus(HouseContract.ContractStatus.SUBMIT);
                    houseSourceHome.save();
                    contractHome.save();

                    return Seller.HouseSourceView.class;
                case FAIL:

                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, result.getMessages(), result.getMessages()));

                    break;
                case ERROR:
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, result.getMessages(), result.getMessages()));

                    break;
            }

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        } catch (HttpApiServerException e) {
            logger.log(Level.WARNING,e.getMessage(),e);
            messages.addError().serverFail();
            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        return null;
    }

    @cc.coopersoft.house.participant.annotations.Seller
    public void printPdf(){
        contractHome.save();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.responseReset();
        externalContext.setResponseContentType("application/pdf");
        externalContext.setResponseHeader("Content-Disposition", "inline; filename=\"" + contractHome.getInstance().getId() + ".pdf\"");

        try {
            localContractConfig.getConfig().pdf(contractHome.getContractContextMap(),externalContext.getResponseOutputStream(),contractHome.getInstance().getContractVersion());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        facesContext.responseComplete();
    }

    @cc.coopersoft.house.participant.annotations.Seller
    public Class<? extends ViewConfig> sourceToContract(){

        if (houseSourceHome.getHouseSourceCompany().getHouseContract() != null){
            contractHome.clearInstance();
            contractHome.setId(houseSourceHome.getHouseSourceCompany().getHouseContract().getId());
            if (HouseContract.ContractStatus.PREPARE.equals(contractHome.getInstance().getStatus())){
                return localContractConfig.getConfig().getEditPath(contractHome.getInstance().getType(),contractHome.getInstance().getContractVersion());
            }else{
                throw new IllegalArgumentException("contract status error!");
            }

        }else{
            beginConversation();

            createAndInitContract();
            return Seller.Apply.ContractBaseInfo.class;
        }

    }

    private void createAndInitContract(){
        contractHome.clearInstance();
        contractHome.getInstance().setHouseArea(houseSourceHome.getInstance().getHouseArea());
        contractHome.getInstance().setHouseCode(houseSourceHome.getInstance().getHouseCode());
        contractHome.getInstance().setHouseDescription(houseSourceHome.getInstance().getAddress());
        contractHome.getInstance().setType(SaleType.OLD_SELL);
        contractHome.getInstance().setContractVersion(SaleType.OLD_SELL.getCurrentVersion());
    }

    @cc.coopersoft.house.participant.annotations.Seller
    public Class<? extends ViewConfig> saveSourceAndCreateContract(){
        houseSourceCreate.saveHouseSourceInfo();
        createAndInitContract();
        return Seller.Apply.ContractBaseInfo.class;

    }

    public void setOwnerShareCalcType(OwnerShareCalcType ownerShareCalcType) {
        this.ownerShareCalcType = ownerShareCalcType;
    }

    public OwnerShareCalcType getOwnerShareCalcType() {
        switch (runParam.getIntegerParam("OWNER_SHARE_CALC_TYPE")) {
            case 1:
                return OwnerShareCalcType.SCALE;
            case 2:
                return OwnerShareCalcType.AREA;
            default:
                return ownerShareCalcType;
        }

    }

//    public Class<? extends ViewConfig> createNew(){
//        beginConversation();
//        return null;
//    }

    public Class<? extends ViewConfig> toSellerInfo(){

        contractHome.createPowerPersonList();
        return Seller.Apply.ContractSellerInfo.class;
    }

    public Class<? extends ViewConfig> toBuyerInfo(){

        return Seller.Apply.ContractBuyerInfo.class;
    }


    public Class<? extends ViewConfig> createContract(){
        //contractHome.save();

        PowerPerson seller = contractHome.getSellerEditList().get(0).getPersonEntity();
        PowerPerson buyer = contractHome.getBuyerEditList().get(0).getPersonEntity();
        contractHome.getContractContextMap().put("contract_number",new ContractContextMap.ContarctContextItem(contractHome.getInstance().getId()));

        contractHome.getContractContextMap().put("seller_name",new ContractContextMap.ContarctContextItem(seller.getPersonName()));
        contractHome.getContractContextMap().put("seller_card_type",new ContractContextMap.ContarctContextItem(enumHelper.getLabel(seller.getCredentialsType())));
        contractHome.getContractContextMap().put("seller_card_number", new ContractContextMap.ContarctContextItem(seller.getCredentialsNumber()));

        contractHome.getContractContextMap().put("buyer_name", new ContractContextMap.ContarctContextItem(buyer.getPersonName()));
        contractHome.getContractContextMap().put("buyer_card_name", new ContractContextMap.ContarctContextItem(enumHelper.getLabel(buyer.getCredentialsType())));
        contractHome.getContractContextMap().put("buyer_card_number",new ContractContextMap.ContarctContextItem(buyer.getCredentialsNumber()));
        contractHome.getContractContextMap().put("money", new ContractContextMap.ContarctContextItem(contractHome.getInstance().getPrice()));

        contractHome.getContractContextMap().put("address", new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getAddress()));
        contractHome.getContractContextMap().put("use_type", new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getDesignUseType()));
        contractHome.getContractContextMap().put("map_number",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getMapNumber()));
        contractHome.getContractContextMap().put("block_number",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getBlockNumber()));
        contractHome.getContractContextMap().put("build_number",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getBuildNumber()));
        contractHome.getContractContextMap().put("house_number",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getHouseOrder()));
        contractHome.getContractContextMap().put("structure", new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getStructure()));
        contractHome.getContractContextMap().put("floor_count",new ContractContextMap.ContarctContextItem(String.valueOf(houseSourceHome.getInstance().getFloorCount())));
        contractHome.getContractContextMap().put("in_floor", new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getInFloorName()));
        contractHome.getContractContextMap().put("power_card_type", new ContractContextMap.ContarctContextItem(enumHelper.getLabel(houseSourceHome.getInstance().getPowerCardType())));
        contractHome.getContractContextMap().put("power_card_number", new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getPowerCardNumber()));
        contractHome.getContractContextMap().put("house_area", new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getHouseArea()));

        if (contractHome.getInstance().getMoneyManager() != null){
            contractHome.getContractContextMap().put("bank_name" , new ContractContextMap.ContarctContextItem(contractHome.getInstance().getMoneyManager().getBankName()));
            contractHome.getContractContextMap().put("bank_account", new ContractContextMap.ContarctContextItem(contractHome.getInstance().getMoneyManager().getAccount()));
            contractHome.getContractContextMap().put("card_bank",new ContractContextMap.ContarctContextItem(contractHome.getInstance().getMoneyManager().getOldHouseMoney().getBankName()));
            contractHome.getContractContextMap().put("card_number",new ContractContextMap.ContarctContextItem(contractHome.getInstance().getMoneyManager().getOldHouseMoney().getCardNumber()));
            contractHome.getContractContextMap().put("card_name", new ContractContextMap.ContarctContextItem(contractHome.getInstance().getMoneyManager().getOldHouseMoney().getCardName()));
            contractHome.getContractContextMap().put("protected_money", new ContractContextMap.ContarctContextItem(contractHome.getInstance().getMoneyManager().getMoney()));
        }else {
            contractHome.getContractContextMap().remove("bank_name");
        }


        contractHome.putContractContext();

        houseSourceHome.getHouseSourceCompany().setHouseContract(contractHome.getInstance());

        houseSourceHome.save();
        endConversation();
        return localContractConfig.getConfig().getEditPath(contractHome.getInstance().getType(),contractHome.getInstance().getContractVersion());

    }

    protected void beginConversation(){
        if ( conversation.isTransient() )
        {
            conversation.begin();
            conversation.setTimeout(1200000);
        }
    }

    protected void endConversation() {
        if ( !conversation.isTransient() )
        {
            conversation.end();
        }
    }

}

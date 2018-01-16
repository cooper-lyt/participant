package cc.coopersoft.house.participant.controller;


import cc.coopersoft.common.EnumHelper;
import cc.coopersoft.house.participant.data.ContractContextMap;
import cc.coopersoft.house.participant.pages.Seller;
import cc.coopersoft.house.sale.data.HouseContract;
import cc.coopersoft.house.sale.data.PowerPerson;
import com.dgsoft.house.OwnerShareCalcType;
import com.dgsoft.house.SaleType;
import org.apache.deltaspike.core.api.config.view.ViewConfig;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Default;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

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
    private FacesContext facesContext;

    private OwnerShareCalcType ownerShareCalcType;

    public Class<? extends ViewConfig> deleteContract(){
        if (HouseContract.ContractStatus.PREPARE.equals(houseSourceHome.getInstance().getHouseContract().getStatus())){
            contractHome.setId(houseSourceHome.getInstance().getHouseContract().getId());
            houseSourceHome.getInstance().setHouseContract(null);

            houseSourceHome.save();
            contractHome.remove();


            return Seller.HouseSourceView.class;
        }else {
            throw new IllegalArgumentException("contract status error!");
        }
    }

    public Class<? extends ViewConfig> submitContract(){
        contractHome.save();

        //TODO submit;
        return Seller.Apply.ContractSubmit.class;
    }

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


    public Class<? extends ViewConfig> sourceToContract(){

        if (houseSourceHome.getInstance().getHouseContract() != null){
            contractHome.clearInstance();
            contractHome.setId(houseSourceHome.getInstance().getHouseContract().getId());
            if (HouseContract.ContractStatus.PREPARE.equals(contractHome.getInstance().getStatus())){
                return localContractConfig.getConfig().getEditPath(contractHome.getInstance().getType(),contractHome.getInstance().getContractVersion());
            }else{
                //TODO print contract
                return null;
            }

        }else{
            beginConversation();

            contractHome.clearInstance();
            contractHome.getInstance().setHouseArea(houseSourceHome.getInstance().getHouseArea());
            contractHome.getInstance().setHouseCode(houseSourceHome.getInstance().getHouseCode());
            contractHome.getInstance().setHouseDescription(houseSourceHome.getInstance().getHouseSaleInfo().getAddress());
            contractHome.getInstance().setType(SaleType.OLD_SELL);
            contractHome.getInstance().setContractVersion(SaleType.OLD_SELL.getCurrentVersion());
            return Seller.Apply.ContractBaseInfo.class;
        }


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

        contractHome.putContractContext();

        houseSourceHome.getInstance().setHouseContract(contractHome.getInstance());

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

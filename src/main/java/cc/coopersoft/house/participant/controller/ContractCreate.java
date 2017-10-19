package cc.coopersoft.house.participant.controller;

import cc.coopersoft.house.participant.pages.Seller;
import com.dgsoft.house.OwnerShareCalcType;
import com.dgsoft.house.PoolType;
import com.dgsoft.house.SaleType;
import org.apache.deltaspike.core.api.config.view.ViewConfig;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;

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

    private OwnerShareCalcType ownerShareCalcType;


    public Class<? extends ViewConfig> createBySource(){

        beginConversation();

        contractHome.clearInstance();
        contractHome.getInstance().setHouseArea(houseSourceHome.getInstance().getHouseSaleInfo().getHouseArea());
        contractHome.getInstance().setHouseCode(houseSourceHome.getInstance().getHouseCode());
        contractHome.getInstance().setHouseDescription(houseSourceHome.getInstance().getHouseSaleInfo().getAddress());
        contractHome.getInstance().setType(SaleType.OLD_SELL);
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
        contractHome.save();

        endConversation();
        return Seller.ContractEdit.class;
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

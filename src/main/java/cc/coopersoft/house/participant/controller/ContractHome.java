package cc.coopersoft.house.participant.controller;

import cc.coopersoft.common.EntityHome;
import cc.coopersoft.common.EnumHelper;
import cc.coopersoft.house.participant.AttrUser;
import cc.coopersoft.house.participant.data.ContractContextMap;
import cc.coopersoft.house.participant.data.ContractPowerPersonHelper;
import cc.coopersoft.house.participant.data.repository.HouseContractRepository;
import cc.coopersoft.house.sale.data.*;
import com.dgsoft.common.system.PersonHelper;
import com.dgsoft.house.PoolType;
import org.apache.deltaspike.data.api.EntityRepository;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * Created by cooper on 10/10/2017.
 */
@Named
@ConversationScoped
public class ContractHome extends EntityHome<HouseContract,String> {

    private static final String ZONE_CODE_PARAM_NAME = "ZONE_CODE";

    private static final String CONTRACT_NUMBER_ID = "CONTRACT_NUMBER";

    @Inject
    private NumberPool numberPool;

    @Inject
    private RunParam runParam;

    @Inject
    private HouseContractRepository houseContractRepository;

    @Inject
    private AttrUser attrUser;

    @Inject
    private FacesContext facesContext;



    private int buyerCount;

    private int sellerCount;

    private List<ContractPowerPersonHelper> buyerEditList;

    private List<ContractPowerPersonHelper> sellerEditList;

    private PersonHelper<SaleProxyPerson> proxyPersonHelper;

    private ContractContextMap contractContextMap;

    public int getBuyerCount() {
        return buyerCount;
    }

    public void setBuyerCount(int buyerCount) {
        this.buyerCount = buyerCount;
    }

    public int getSellerCount() {
        return sellerCount;
    }

    public void setSellerCount(int sellerCount) {
        this.sellerCount = sellerCount;
    }

    public List<ContractPowerPersonHelper> getBuyerEditList() {
        return buyerEditList;
    }

    public void setBuyerEditList(List<ContractPowerPersonHelper> buyerEditList) {
        this.buyerEditList = buyerEditList;
    }

    public List<ContractPowerPersonHelper> getSellerEditList() {
        return sellerEditList;
    }

    public void setSellerEditList(List<ContractPowerPersonHelper> sellerEditList) {
        this.sellerEditList = sellerEditList;
    }

    public PersonHelper<SaleProxyPerson> getProxyPersonHelper() {
        return proxyPersonHelper;
    }

    public void setProxyPersonHelper(PersonHelper<SaleProxyPerson> proxyPersonHelper) {
        this.proxyPersonHelper = proxyPersonHelper;
    }

    public ContractContextMap getContractContextMap() {
        if (contractContextMap == null) {
            try {
                if (getInstance().getContext() == null || "".equals(getInstance().getContext().trim())) {
                    contractContextMap = new ContractContextMap();
                } else
                    contractContextMap = new ContractContextMap(new JSONObject(getInstance().getContext()));
            } catch (JSONException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return contractContextMap;
    }


    public void putContractContext(){
        try {
            getInstance().setContext(getContractContextMap().toJson().toString());
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @PostConstruct
    private void postConstruct(){
        setId(facesContext.getExternalContext().getRequestParameterMap().get("contractId"));
    }

    private List<PowerPerson> getPowerPersonList(PowerPerson.ContractPersonType type){
        List result = new ArrayList();
        for (PowerPerson pp :getInstance ().getPowerPersons()){
            if (type.equals(pp.getContractPersonType())){
                result.add(pp);
            }
        }
        Collections.sort(result, new Comparator<PowerPerson>() {
            public int compare(PowerPerson o1, PowerPerson o2) {
                return Integer.valueOf(o1.getPri()).compareTo(Integer.valueOf(o2.getPri()));
            }
        });
        return result;
    }

    public void save(){
        if (!HouseContract.ContractStatus.PREPARE.equals(getInstance().getStatus()))
            throw new IllegalArgumentException("contract status is error!");
        putContractContext();
        super.save();
    }

    @Override
    protected void initInstance(){
        super.initInstance();
        proxyPersonHelper = new PersonHelper<SaleProxyPerson>(getInstance().getSaleProxyPerson());
        contractContextMap = null;


        buyerCount = 0;
        buyerEditList = new ArrayList<ContractPowerPersonHelper>();
        sellerEditList = new ArrayList<ContractPowerPersonHelper>();
        sellerCount = 0;
        for (PowerPerson pp :getInstance ().getPowerPersons()){
            if(PowerPerson.ContractPersonType.BUYER.equals(pp.getContractPersonType())){
                buyerCount++;
                buyerEditList.add(new ContractPowerPersonHelper(pp,getInstance().getHouseArea()));
            }else{
                sellerCount++;
                sellerEditList.add(new ContractPowerPersonHelper(pp,getInstance().getHouseArea()));
            }
        }
        if (buyerCount == 0){
            buyerCount = 1;
        }
        if (sellerCount == 0){
            sellerCount = 1;
        }
    }

    private void genEditPowerPerson(List<ContractPowerPersonHelper> list, int count){
        while (list.size() != count) {
            if (list.size() > count) {
                getInstance().getPowerPersons().remove(list.remove(0).getPersonEntity());
            } else {
                PowerPerson pool = new PowerPerson(UUID.randomUUID().toString().replace("-",""), getInstance(), PowerPerson.ContractPersonType.BUYER, list.size() + 1);
                list.add(new ContractPowerPersonHelper(pool, getInstance().getHouseArea()));
                getInstance().getPowerPersons().add(pool);
            }
        }
    }

    public void createPowerPersonList(){
        if (PoolType.SINGLE_OWNER.equals(getInstance().getPoolType())){
            buyerCount = 1;
        }
        if (PoolType.SINGLE_OWNER.equals(getInstance().getOldHouseContract().getSellerPoolType())){
            sellerCount = 1;
        }
        genEditPowerPerson(buyerEditList,buyerCount);
        genEditPowerPerson(sellerEditList,sellerCount);
    }

    protected HouseContract createInstance() {

        HouseContract houseContractEntity = new HouseContract("C" +runParam.getStringParam(ZONE_CODE_PARAM_NAME) + numberPool.getNumber(CONTRACT_NUMBER_ID),
                attrUser.getLoginData().getCorpInfo().getId(), new Date(),
                HouseContract.ContractStatus.PREPARE, attrUser.getLoginData().getAttrEmp().getId(), attrUser.getLoginData().getAttrEmp().getName(), PoolType.SINGLE_OWNER);


        OldHouseContract oldHouseContract = new OldHouseContract(houseContractEntity);
        houseContractEntity.setOldHouseContract(oldHouseContract);

        SaleProxyPerson saleProxyPerson = new SaleProxyPerson(houseContractEntity);
        houseContractEntity.setSaleProxyPerson(saleProxyPerson);



        return houseContractEntity;


    }

    protected EntityRepository<HouseContract, String> getEntityRepository() {
        return houseContractRepository;
    }
}

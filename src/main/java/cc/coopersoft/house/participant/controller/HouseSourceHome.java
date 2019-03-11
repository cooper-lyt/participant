package cc.coopersoft.house.participant.controller;

import cc.coopersoft.comm.HttpJsonDataGet;
import cc.coopersoft.common.EntityHome;
import cc.coopersoft.house.participant.AttrUser;
import cc.coopersoft.house.participant.Messages;
import cc.coopersoft.house.participant.data.ContractContextMap;
import cc.coopersoft.house.participant.data.repository.HouseSourceRepository;
import cc.coopersoft.house.participant.service.HouseSourceService;
import cc.coopersoft.house.sale.data.*;
import com.dgsoft.common.system.OwnerPersonHelper;
import com.dgsoft.common.system.ProxyPersonEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.deltaspike.jsf.api.message.JsfMessage;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;

/**
 * Created by cooper on 15/04/2017.
 */
@Named
@ConversationScoped
public class HouseSourceHome extends EntityHome<HouseSource,String> {

    @Inject
    private SaleAreaCache saleAreaCache;

    @Inject
    private HouseSourceRepository houseSourceRepository;

    @Inject
    private HouseSourceService houseSourceService;

    @Inject
    private LocalContractConfig localContractConfig;

    @Inject
    private FacesContext facesContext;

    @Inject
    private RunParam runParam;

    @Inject
    private AttrUser attrUser;

    @Inject
    private JsfMessage<Messages> messages;

    @Override
    protected String getInstaceId(){
        return getInstance().getId();
    }




    private HouseSourceCompany houseSourceCompany;

    private Boolean join;

    //private Boolean contracted;

    private void initHouseSourceCompany(){
        join = false;
        houseSourceCompany = null;
        for(HouseSourceCompany hsc: getInstance().getHouseSourceCompanies()) {
            if (attrUser.getLoginData().getCorpInfo().getId().equals(hsc.getGroupId())) {
                join = true;
                houseSourceCompany = hsc;
                return ;
            }
        }
    }

    public HouseSourceCompany getHouseSourceCompany(){
        if (join == null){
            initHouseSourceCompany();
        }

        return houseSourceCompany;
    }

    public boolean isJoin(){
        if (join == null){
            initHouseSourceCompany();
        }
        return join;
    }

    public boolean isAllowEdit(){
        return isAllowContract() && HouseSource.HouseSourceStatus.PREPARE.equals(getInstance().getStatus())
                && getInstance().getGroupId().equals(attrUser.getLoginData().getCorpInfo().getId());
    }


    public boolean isAllowDelete(){
        return isAllowContract() && HouseSource.HouseSourceStatus.PREPARE.equals(getInstance().getStatus());
    }

    public boolean isAllowContract(){
        return isJoin() &&
                ((getHouseSourceCompany().getHouseContract() == null)
                || HouseContract.ContractStatus.PREPARE.equals(getHouseSourceCompany().getHouseContract().getStatus()));
    }

    public boolean isAllowPrintSellContract(){



        return isJoin() && (getHouseSourceCompany().getHouseContract() != null) &&
                HouseSource.HouseSourceStatus.SUBMIT.equals(getInstance().getStatus()) &&
                HouseContract.ContractStatus.SUBMIT.equals(getHouseSourceCompany().getHouseContract().getStatus()) &&
                (!"TRUE".equals(runParam.getStringParam("print_after")) || houseSourceService.isContractRecorded( getHouseSourceCompany().getHouseContract().getId()));

    }

    public String getContractId(){
        if (getHouseSourceCompany().getHouseContract() != null) {
            return getHouseSourceCompany().getHouseContract().getId();
        }else{
            return null;
        }
    }

    private ContractContextMap getContractContextMap(){

        try {
            if (getHouseSourceCompany().getContext() == null || "".equals(getHouseSourceCompany().getContext().trim())) {
                return new ContractContextMap();
            } else
                return new ContractContextMap(new JSONObject(getHouseSourceCompany().getContext()));
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }


    @cc.coopersoft.house.participant.annotations.Seller
    public void printAgentPdf(){

        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.responseReset();
        externalContext.setResponseContentType("application/pdf");
        externalContext.setResponseHeader("Content-Disposition", "inline; filename=\"" + getInstance().getId() + ".pdf\"");

        try {
            localContractConfig.getConfig().agentPdf(getContractContextMap(),externalContext.getResponseOutputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        facesContext.responseComplete();
    }



    public BigDecimal getLng(){
        if (getInstance().getHouseSaleInfo() != null) {
            BigDecimal lng = getInstance().getHouseSaleInfo().getLng();
            if (lng != null){
                return lng;
            }
        }
        return runParam.getDecimalParam("map.lng");
    }

    public BigDecimal getLat(){
        if (getInstance().getHouseSaleInfo() != null) {
            BigDecimal lat = getInstance().getHouseSaleInfo().getLat();
            if (lat != null){
                return lat;
            }
        }
        return runParam.getDecimalParam("map.lat");
    }

    public List<HouseSalePic> getHouseSalePicList(){
        HouseSaleInfo hsi = getInstance().getHouseSaleInfo();
        if (hsi != null) {
            List<HouseSalePic> pics = new ArrayList<HouseSalePic>(hsi.getHouseSalePics());

            Collections.sort(pics, new Comparator<HouseSalePic>() {
                public int compare(HouseSalePic o1, HouseSalePic o2) {
                    return Integer.valueOf(o2.getPri()).compareTo(o1.getPri());
                }
            });
            return  pics;

        }
        return null;
    }

    public String getPicJsonData(){


           List<HouseSalePic> pics = getHouseSalePicList();
           if (pics != null) {

               ObjectMapper mapper = new ObjectMapper();
               try {
                   return mapper.writeValueAsString(pics);
               } catch (JsonProcessingException e) {
                   throw new IllegalArgumentException(e.getMessage(), e);
               }

           }

        return "[]";
    }

    @PostConstruct
    public void initParam(){
        setId(facesContext.getExternalContext().getRequestParameterMap().get("houseSaleInfoId"));
        logger.config("set HouseSaleInfo Home ID form param:" + getId());
    }


    protected HouseSource createInstance() {
        return new HouseSource();
    }

    protected EntityRepository<HouseSource, String> getEntityRepository() {
        return houseSourceRepository;
    }

    public List<SaleArea> getSchoolAreaList(){
        return saleAreaCache.getSaleAreas(SaleArea.SaleAreaType.SCHOOL,getInstance().getDistrict(),false);
    }

    public List<SaleArea> getSaleLocalAreaList(){
        return saleAreaCache.getSaleAreas(SaleArea.SaleAreaType.SALE,getInstance().getDistrict(),false);
    }



    public void addLimitMessages(SellLimit limit){
        List<SellLimit> result = getSellLimitMessages();
        result.add(limit);
        ObjectMapper mapper = new ObjectMapper();
        try {
            getInstance().setMessages(mapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("add fail");
        }
    }

    public void addLimitMessages(List<SellLimit> limits){
        List<SellLimit> result = getSellLimitMessages();
        result.addAll(limits);
        ObjectMapper mapper = new ObjectMapper();
        try {
            getInstance().setMessages(mapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("add fail");
        }
    }

    public List<SellLimit> getSellLimitMessages(){
        String messages = getInstance().getMessages();
        if (messages == null || "".equals(messages.trim())){
            return new ArrayList<SellLimit>(0);
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(messages,HttpJsonDataGet.getCollectionType(ArrayList.class,SellLimit.class));

        } catch (IOException e) {
            logger.log(Level.WARNING,e.getMessage(),e);
            return new ArrayList<SellLimit>(0);
        }
    }



    @Override
    @Transactional
    public void save(){

        getInstance().setUpdateTime(new Date());
        logger.config("" + getInstance().getProxyPerson());
        logger.config(getInstance().getId());
        if (getInstance().getProxyPerson() != null){
            getInstance().getProxyPerson().setId(getInstance().getId());
        }
        super.save();
    }

    private OwnerPersonHelper<HouseSource> sellPerson;

    public OwnerPersonHelper<HouseSource> getSellPerson() {
        if (sellPerson == null){
            sellPerson = new OwnerPersonHelper<HouseSource>(getInstance()) {
                protected ProxyPersonEntity createProxyPerson() {
                    return new HouseSourceProxyPerson(getInstance().getId());
                }
            };
        }
        return sellPerson;
    }
}

package cc.coopersoft.house.participant.controller;

import cc.coopersoft.comm.HttpJsonDataGet;
import cc.coopersoft.common.EntityHome;
import cc.coopersoft.house.participant.AttrUser;
import cc.coopersoft.house.participant.Messages;
import cc.coopersoft.house.participant.data.ContractContextMap;
import cc.coopersoft.house.participant.data.repository.HouseSourceRepository;
import cc.coopersoft.house.sale.data.*;
import com.dgsoft.common.system.OwnerPersonHelper;
import com.dgsoft.common.system.PersonHelper;
import com.dgsoft.common.system.ProxyPersonEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.deltaspike.jsf.api.message.JsfMessage;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
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
    private FacesContext facesContext;

    @Inject
    private RunParam runParam;

    @Inject
    private AttrUser attrUser;

    @Inject
    private JsfMessage<Messages> messages;

    public HouseSourceCompany getHouseSourceCompany(){
        for(HouseSourceCompany hsc: getInstance().getHouseSourceCompanies()) {
            if (attrUser.getLoginData().getCorpInfo().getId().equals(hsc.getGroupId())) {
                return hsc;
            }
        }
        return null;
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

    public String getPicJsonData(){
        HouseSaleInfo hsi = getInstance().getHouseSaleInfo();
        if (hsi != null){
           List<HouseSalePic> pics = new ArrayList<HouseSalePic>(hsi.getHouseSalePics());
           Collections.sort(pics, new Comparator<HouseSalePic>() {
               public int compare(HouseSalePic o1, HouseSalePic o2) {
                   return Integer.valueOf(o2.getPri()).compareTo(o1.getPri()) ;
               }
           });
           ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writeValueAsString(pics);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException(e.getMessage(),e);
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
        return saleAreaCache.getSaleAreas(SaleArea.SaleAreaType.SCHOOL,getInstance().getHouseSaleInfo().getDistrict(),false);
    }

    public List<SaleArea> getSaleLocalAreaList(){
        return saleAreaCache.getSaleAreas(SaleArea.SaleAreaType.SALE,getInstance().getHouseSaleInfo().getDistrict(),false);
    }

    public void calcPrice(){
        HouseSaleInfo hsi = getInstance().getHouseSaleInfo();
        if (hsi != null){
            if (hsi.getSumPrice() != null) {
                hsi.setPrice(hsi.getSumPrice().divide(hsi.getHouseArea(), 2, BigDecimal.ROUND_HALF_EVEN));
            }else{
                hsi.setPrice(null);
            }
        }
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
        calcPrice();
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

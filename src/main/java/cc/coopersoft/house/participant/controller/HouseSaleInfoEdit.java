package cc.coopersoft.house.participant.controller;

import cc.coopersoft.comm.HttpJsonDataGet;
import cc.coopersoft.comm.exception.HttpApiServerException;
import cc.coopersoft.house.SaleLimitType;
import cc.coopersoft.house.participant.AttrUser;
import cc.coopersoft.house.participant.Messages;
import cc.coopersoft.house.participant.annotations.Seller;
import cc.coopersoft.house.participant.data.ContractContextMap;
import cc.coopersoft.house.participant.service.HouseSourceService;
import cc.coopersoft.house.sale.data.*;
import com.dgsoft.developersale.wsinterface.DESUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.deltaspike.jsf.api.message.JsfMessage;
import org.json.JSONException;
import org.json.JSONObject;
import org.omnifaces.cdi.Param;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cooper on 13/06/2017.
 */
@Named
@RequestScoped
public class HouseSaleInfoEdit implements java.io.Serializable{

    @Inject
    protected Logger logger;

    @Inject
    private HouseSourceHome houseSourceHome;



    @Inject @Param(name = "fid")
    private String fid;

    @Inject @Param(name = "fname")
    private String fname;

    @Inject @Param(name = "lng")
    private BigDecimal lng;

    @Inject @Param(name = "lat")
    private BigDecimal lat;

    @Inject
    private RunParam runParam;

    @Inject
    private AttrUser attrUser;

    @Inject
    private JsfMessage<Messages> messages;

    @Inject
    private FacesContext facesContext;

    @Inject
    private HouseSourceService houseSourceService;


    private ContractContextMap contractContextMap;

    private String orderStr;

    public String getOrderStr() {
        return orderStr;
    }

    public void setOrderStr(String orderStr) {
        this.orderStr = orderStr;
    }



    public ContractContextMap getContractContextMap() {
        if (contractContextMap == null){
            HouseSourceCompany hsc = houseSourceHome.getHouseSourceCompany();
            if (hsc == null){
                throw new IllegalArgumentException("not company info");
            }else {
                try {
                    if (hsc.getContext() == null || hsc.getContext().trim().equals("")) {
                        contractContextMap = new ContractContextMap();
                    } else
                        contractContextMap = new ContractContextMap(new JSONObject(hsc.getContext()));
                } catch (JSONException e) {
                    throw new IllegalArgumentException(e.getMessage(), e);
                }

            }

        }
        return contractContextMap;
    }

    @Seller
    @Transactional
    public void updateLocation(){
        houseSourceHome.getInstance().getHouseSaleInfo().setLng(lng);
        houseSourceHome.getInstance().getHouseSaleInfo().setLat(lat);
        houseSourceHome.save();
    }

    @Seller
    @Transactional
    public void removePic(){
        logger.config("fid is:" + fid);
        for(HouseSalePic pic: houseSourceHome.getInstance().getHouseSaleInfo().getHouseSalePics()){
            if (pic.getId().equals(fid)){
                houseSourceHome.getInstance().getHouseSaleInfo().getHouseSalePics().remove(pic);
                houseSourceHome.save();
                return;
            }
        }
    }

    @Seller
    @Transactional
    public void addPic(){
        logger.config("fid is:" + fid);
        HouseSalePic pic = new HouseSalePic();
        pic.setId(fid);
        pic.setTitle(fname.substring(0,fname.lastIndexOf(".")));
        pic.setPri(0);
        pic.setHouseSaleInfo(houseSourceHome.getInstance().getHouseSaleInfo());

        houseSourceHome.getInstance().getHouseSaleInfo().getHouseSalePics().add(pic);
        houseSourceHome.save();
    }


    private boolean validSource()  {
        if (!HouseSource.HouseSourceStatus.PREPARE.equals(houseSourceHome.getInstance().getStatus()) ){
            throw new IllegalArgumentException("can't edit status");
        }

        HouseSource passHouseSource = houseSourceService.existsPassHouseSource(houseSourceHome.getInstance().getHouseCode());
        if (passHouseSource != null){
            //messages.addInfo().houseSourceExists();
            houseSourceHome.getInstance().setStatus(HouseSource.HouseSourceStatus.PREPARE);
            houseSourceHome.getInstance().setMessageType(HouseSource.MessageType.CANCEL);

            houseSourceHome.addLimitMessages(new SellLimit(SaleLimitType.OTHER_SALE,"",new Date()));
            houseSourceHome.save();
            houseSourceHome.setId(passHouseSource.getId());
            return false;
        }


        try {
            HouseValidResult result = houseSourceService.validHouseSource(houseSourceHome.getInstance());
            switch (result.getValidStatus()){

                case SUCCESS:
                    if (result.getLimits().isEmpty()){
                        return true;
                    }else{
                        houseSourceHome.getInstance().setStatus(HouseSource.HouseSourceStatus.PREPARE);
                        houseSourceHome.getInstance().setMessageType(HouseSource.MessageType.CANCEL);
                        houseSourceHome.addLimitMessages(result.getLimits());
                        houseSourceHome.save();
                        return false;
                    }
                case HOUSE_NOT_FOUND:
                case OWNER_FAIL:
                    houseSourceHome.getInstance().setStatus(HouseSource.HouseSourceStatus.PREPARE);
                    houseSourceHome.getInstance().setMessageType(HouseSource.MessageType.CANCEL);
                    houseSourceHome.addLimitMessages(new SellLimit(SaleLimitType.OWNER_CHANGE,"",new Date()));
                    houseSourceHome.save();
                    return false;
                case ERROR:
                    throw new HttpApiServerException(505);
            }

        } catch (HttpApiServerException e) {
            logger.log(Level.WARNING,e.getMessage(),e);
            messages.addError().serverFail();
            throw new IllegalArgumentException(e.getMessage(),e);
        }



        throw new IllegalArgumentException("unknow valid status");
    }

    private HouseSaleInfo houseSaleInfo;

    public HouseSaleInfo getHouseSaleInfo() {
        if (houseSourceHome.getInstance().getHouseSaleInfo() != null){
            return houseSourceHome.getInstance().getHouseSaleInfo();
        }else if (houseSaleInfo == null){
            houseSaleInfo = new HouseSaleInfo();
            houseSaleInfo.setShowAreaType(HouseSaleInfo.ShowAreaType.TO_SELL);
        }
        return houseSaleInfo;
    }

    public void calcPrice(){
            if (getHouseSaleInfo().getSumPrice() != null) {
                logger.config("calc price area:" + houseSourceHome.getInstance().getHouseArea());
                logger.config("calc price id:" + houseSourceHome.getId());
                getHouseSaleInfo().setPrice(getHouseSaleInfo().getSumPrice().divide(houseSourceHome.getInstance().getHouseArea(), 2, BigDecimal.ROUND_HALF_EVEN));
            }else{
                getHouseSaleInfo().setPrice(null);
            }

    }

    @Seller
    @Transactional
    public Class<? extends ViewConfig> deleteHouseSource(){
        if (houseSourceHome.isAllowDelete()){

            houseSourceHome.remove();
            return cc.coopersoft.house.participant.pages.Seller.HouseList.class;
        }else
            return null;


    }

    @Seller
    @Transactional
    public Class<? extends ViewConfig> saveHouseSource(){
        //houseSaleInfo = houseSellRepository.save(houseSaleInfo);



                if (validSource()){
                    if (houseSourceHome.getInstance().getHouseSaleInfo() == null){
                        houseSaleInfo.setId(houseSourceHome.getInstance().getId());
                        houseSourceHome.getInstance().setHouseSaleInfo(houseSaleInfo);
                    }
                    getContractContextMap().put("house_in_floor",new ContractContextMap.ContarctContextItem(String.valueOf(houseSourceHome.getInstance().getHouseSaleInfo().getInFloor())));
                    getContractContextMap().put("house_floor_count",new ContractContextMap.ContarctContextItem(String.valueOf(houseSourceHome.getInstance().getHouseSaleInfo().getFloorCount())));
                    getContractContextMap().put("house_elevator",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getHouseSaleInfo().isElevator()?"有":"无"));
                    getContractContextMap().put("house_living_count",new ContractContextMap.ContarctContextItem(String.valueOf(houseSourceHome.getInstance().getHouseSaleInfo().getLivingRoom())));
                    getContractContextMap().put("house_room_count",new ContractContextMap.ContarctContextItem(String.valueOf(houseSourceHome.getInstance().getHouseSaleInfo().getRoomCount())));
                    getContractContextMap().put("house_kitchen_count",new ContractContextMap.ContarctContextItem(String.valueOf(houseSourceHome.getInstance().getHouseSaleInfo().getKitchenCount())));
                    getContractContextMap().put("house_toilet_count",new ContractContextMap.ContarctContextItem(String.valueOf(houseSourceHome.getInstance().getHouseSaleInfo().getToiletCount())));
                    getContractContextMap().put("house_direction",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getHouseSaleInfo().getDirection()));
                    getContractContextMap().put("house_price",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getHouseSaleInfo().getSumPrice()));

                    getContractContextMap().put("time_limit_type",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getHouseSaleInfo().getShowAreaType().toString()));

                    if(HouseSaleInfo.ShowAreaType.TO_END_TIME.equals(houseSourceHome.getInstance().getHouseSaleInfo().getShowAreaType())){

                        getContractContextMap().put("time_limit_end",new ContractContextMap.ContarctContextItem(houseSourceHome.getInstance().getHouseSaleInfo().getEndTime()));
                    }

                    try {
                        houseSourceHome.getHouseSourceCompany().setContext(getContractContextMap().toJson().toString());
                    } catch (JSONException e) {
                        throw new IllegalArgumentException(e.getMessage(),e);
                    }


                    calcPrice();
                    houseSourceHome.save();
                    return cc.coopersoft.house.participant.pages.Seller.Apply.HouseSalePicUpload.class;
                }else{
                    return cc.coopersoft.house.participant.pages.Seller.HouseSourceView.class;
                }


    }

    @Seller
    public Class<? extends ViewConfig> saveLocation(){
        return cc.coopersoft.house.participant.pages.Seller.Apply.HouseSourceContract.class;
    }

    @Seller
    public void updateContext(){
        HouseSourceCompany hsc = houseSourceHome.getHouseSourceCompany();

        try {
            hsc.setContext(getContractContextMap().toJson().toString());
            houseSourceHome.save();
        } catch (JSONException e) {
            throw new IllegalArgumentException();
        }

    }

    @Seller
    @Transactional
    public Class<? extends ViewConfig> savePics(){
        logger.config("order str is:" + orderStr);

        if (!validSource()){
            return cc.coopersoft.house.participant.pages.Seller.HouseSourceView.class;
        }



        if (orderStr != null && !orderStr.trim().equals("")){
            int index = 0;
            for (String id :orderStr.split(" ")){
                if (!"".equals(id.trim())){
                    if (index == 0){
                        houseSourceHome.getInstance().getHouseSaleInfo().setCover(id);
                    }

                    for(HouseSalePic pic: houseSourceHome.getInstance().getHouseSaleInfo().getHouseSalePics()){
                        if (id.equals(pic.getId())){
                            pic.setPri(index++);
                            break;
                        }
                    }
                }
            }
            houseSourceHome.save();
        }
        return cc.coopersoft.house.participant.pages.Seller.Apply.HouseSourceContract.class;
    }

    @Seller
    @Transactional
    public Class<? extends ViewConfig> commitHouseSource(){

        if (validSource()){
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map<String,String> params = new HashMap<String, String>(1);


                String data = mapper.writeValueAsString(houseSourceHome.getInstance());
                logger.config(data);
                params.put("data", DESUtil.encrypt(data, attrUser.getLoginData().getToken()));
                SubmitResult result = HttpJsonDataGet.postData(runParam.getStringParam("server_address") + "interfaces/extends/contract/HOUSE_SOURCE/" + attrUser.getLoginData().getKey(),params, SubmitResult.class);


                switch (result.getStatus()){

                    case SUCCESS:
                        houseSourceHome.getInstance().setBusinessId(result.getBusinessId());
                        houseSourceHome.getInstance().setCheckTime(new Date());
                        houseSourceHome.getInstance().setStatus(HouseSource.HouseSourceStatus.CHECK);
                        houseSourceHome.getInstance().setMessages(null);
                        houseSourceHome.save();
                        messages.addInfo().contractCommited();
                        return cc.coopersoft.house.participant.pages.Seller.HouseSourceView.class;
                    case FAIL:

                        facesContext.addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, result.getMessages(), result.getMessages()));

                        return null;
                    case ERROR:
                        facesContext.addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, result.getMessages(), result.getMessages()));

                        return null;
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

            throw new IllegalArgumentException("unknow result status");

        }else{

            return cc.coopersoft.house.participant.pages.Seller.HouseSourceView.class;

        }

    }
}

package cc.coopersoft.house.participant.service;

import cc.coopersoft.comm.exception.HttpApiServerException;
import cc.coopersoft.house.participant.controller.RunParam;
import cc.coopersoft.house.participant.data.repository.HouseSourceRepository;
import cc.coopersoft.house.sale.HouseSellService;
import cc.coopersoft.house.sale.data.HouseSource;
import cc.coopersoft.house.sale.data.HouseValidInfo;
import cc.coopersoft.house.sale.data.HouseValidResult;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by cooper on 05/08/2017.
 */
public class HouseSourceService implements java.io.Serializable{

    @Inject
    private RunParam runParam;

    @Inject
    private HouseSourceRepository houseSourceRepository;

    @Inject
    private Logger logger;


    public HouseValidResult validHouseSource(HouseValidInfo houseValidInfo) throws HttpApiServerException {
        return HouseSellService.houseValid(runParam.getStringParam("nginx_address"),houseValidInfo);
    }


    public HouseValidResult validHouseSource(HouseSource houseSource) throws HttpApiServerException {
        HouseValidInfo houseValidInfo = new HouseValidInfo(HouseValidInfo.ValidType.HOUSE_CODE);
        houseValidInfo.setHouseCode(houseSource.getHouseCode());
        houseValidInfo.setCredentialsType(houseSource.getCredentialsType());
        houseValidInfo.setCredentialsNumber(houseSource.getCredentialsNumber());
        houseValidInfo.setPowerCardNumber(houseSource.getPowerCardNumber());
        houseValidInfo.setPersonName(houseSource.getPersonName());


        return validHouseSource(houseValidInfo);
    }

    public boolean isContractRecorded(String id)  {
        logger.config("status id:" + id );
        try {
            String s = HouseSellService.contractBusinessStatus(runParam.getStringParam("nginx_address"),id);
            logger.config("status:" + s);
            return  "COMPLETE".equals(s);
        } catch (HttpApiServerException e) {
            return false;
        }

    }

    private HouseSource getSingleHouseSource(List<HouseSource> houseSourceList){
        if (houseSourceList.isEmpty()){
            return null;
        }else{
            if (houseSourceList.size() > 1){
                throw new IllegalArgumentException("house code mulit showing");
            }
            return houseSourceList.get(0);
        }
    }

    public HouseSource existsPassHouseSource(String houseCode){
        ArrayList<HouseSource.HouseSourceStatus> allowStatus = new ArrayList<HouseSource.HouseSourceStatus>(
            EnumSet.of(HouseSource.HouseSourceStatus.SHOWING)
        );

        return getSingleHouseSource(houseSourceRepository.houseSourceByStatus(allowStatus,houseCode));
    }

    public HouseSource existsHouseSource(String houseCode, String corpId){
        ArrayList<HouseSource.HouseSourceStatus> allowStatus = new ArrayList<HouseSource.HouseSourceStatus>(
                EnumSet.of(HouseSource.HouseSourceStatus.PREPARE,HouseSource.HouseSourceStatus.SHOWING,HouseSource.HouseSourceStatus.CHECK)
        );
        return getSingleHouseSource(houseSourceRepository.houseSourceByStatus(allowStatus,houseCode,corpId));
    }


}

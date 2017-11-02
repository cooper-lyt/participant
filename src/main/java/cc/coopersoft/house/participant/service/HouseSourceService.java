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

/**
 * Created by cooper on 05/08/2017.
 */
public class HouseSourceService {

    @Inject
    private RunParam runParam;

    @Inject
    private HouseSourceRepository houseSourceRepository;


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
            EnumSet.of(HouseSource.HouseSourceStatus.CHECK,HouseSource.HouseSourceStatus.SUBMIT,
                    HouseSource.HouseSourceStatus.CHECK_PASS,HouseSource.HouseSourceStatus.SHOWING)
        );

        return getSingleHouseSource(houseSourceRepository.houseSourceByStatus(allowStatus,houseCode));
    }

    public HouseSource existsEditHouseSource(String houseCode, String corpId){
        ArrayList<HouseSource.HouseSourceStatus> allowStatus = new ArrayList<HouseSource.HouseSourceStatus>(
                EnumSet.of(HouseSource.HouseSourceStatus.PREPARE,HouseSource.HouseSourceStatus.CHECK_FAIL)
        );
        return getSingleHouseSource(houseSourceRepository.houseSourceByStatus(allowStatus,houseCode,corpId));
    }

    public void updateHouseSourceByHouse(String houseCode){

    }

    public void updateHouseSourceByCorp(String attrCorpId){

    }

    public void updateHouseSourceById(String sourceId){

    }

}

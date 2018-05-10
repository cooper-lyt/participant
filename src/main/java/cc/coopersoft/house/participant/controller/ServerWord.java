package cc.coopersoft.house.participant.controller;

import cc.coopersoft.comm.District;
import cc.coopersoft.comm.exception.HttpApiServerException;
import cc.coopersoft.house.sale.HouseSellService;
import cc.coopersoft.house.sale.data.MoneyProtectedBank;
import cc.coopersoft.house.sale.data.Word;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

/**
 * Created by cooper on 04/03/2017.
 */
@Named
@ApplicationScoped
public class ServerWord implements java.io.Serializable {

    @Inject
    private RunParam runParam;

    private List<District> districts;

    private List<MoneyProtectedBank> oldHouseMoneyProtectedBank;

    //private Map<String ,Map<String,Word>> words;

    public List<District> getDistricts(){
        if (districts == null){
            try {
                districts = HouseSellService.listDistrict(runParam.getStringParam("nginx_address"));
            } catch (HttpApiServerException e) {
                throw new IllegalArgumentException("server fail!" , e);
            }
        }
        return districts;
    }

    public String getDistrictName(String id){
        for (District d: getDistricts()){
            if (d.getId().equals(id)){
                return d.getName();
            }
        }
        return null;
    }

    public List<MoneyProtectedBank> getOldHouseMoneyProtectedBank() {
        if (oldHouseMoneyProtectedBank == null){
            try {
                oldHouseMoneyProtectedBank = HouseSellService.getOldHouseMoneyProtectedAccountList(runParam.getStringParam("nginx_address"));
            } catch (HttpApiServerException e) {
                throw new IllegalArgumentException("server fail!" , e);
            }
        }
        return oldHouseMoneyProtectedBank;
    }

    public MoneyProtectedBank getOldHouseMoneyProtectedBankById(String id){
        for(MoneyProtectedBank bank : getOldHouseMoneyProtectedBank()){
            if (bank.getId().equals(id)){
                return bank;
            }
        }
        return null;
    }
}

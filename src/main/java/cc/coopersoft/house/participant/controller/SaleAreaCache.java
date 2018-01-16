package cc.coopersoft.house.participant.controller;

import cc.coopersoft.comm.exception.HttpApiServerException;
import cc.coopersoft.house.sale.HouseSellService;
import cc.coopersoft.house.sale.data.SaleArea;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cooper on 04/03/2017.
 */
@Named
@SessionScoped
public class SaleAreaCache implements java.io.Serializable{

    @Inject
    private RunParam runParam;

    private List<SaleArea> saleAreas;

    private void initSaleArea(){
        try {
            saleAreas = HouseSellService.listAllSaleArea(runParam.getStringParam("nginx_address"));
        } catch (HttpApiServerException e) {
            throw new IllegalArgumentException("server fail",e);
        }
    }

    private List<SaleArea> getSaleAreas(){
        if (saleAreas == null){
            initSaleArea();
        }
        return saleAreas;
    }

    public List<SaleArea> getSaleAreas(SaleArea.SaleAreaType type, String district, boolean isAll){

        List<SaleArea> result = new ArrayList<SaleArea>();
        for (SaleArea area: getSaleAreas()){
            if (area.getType().equals(type) && area.getDistrict().equals(district) && (isAll || area.isEnable())){
                result.add(area);
            }
        }
        return result;
    }

    public SaleArea getSaleAreaById(String id){
        for (SaleArea saleArea : getSaleAreas()){
            if (saleArea.getId().equals(id)){
                return saleArea;
            }
        }
        return null;
    }

    public String getSaleAreaNameById(String id){
        SaleArea saleArea = getSaleAreaById(id);
        if (saleArea == null){
            return "";
        }else{
            return saleArea.getName();
        }
    }

}

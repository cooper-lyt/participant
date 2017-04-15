package cc.coopersoft.house.participant.controller;

import cc.coopersoft.comm.District;
import cc.coopersoft.comm.exception.HttpApiServerException;
import cc.coopersoft.house.sale.HouseSellService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by cooper on 04/03/2017.
 */
@Named
@ApplicationScoped
public class ServerWord implements java.io.Serializable {

    @Inject
    private RunParam runParam;

    private List<District> districts;

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
        for (District d: districts){
            if (d.getId().equals(id)){
                return d.getName();
            }
        }
        return null;
    }


}

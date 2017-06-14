package cc.coopersoft.house.participant.controller;

import cc.coopersoft.common.EntityHome;
import cc.coopersoft.house.participant.data.repository.HouseSourceRepository;
import cc.coopersoft.house.sale.data.HouseSaleInfo;
import cc.coopersoft.house.sale.data.HouseSource;
import cc.coopersoft.house.sale.data.SaleArea;
import org.apache.deltaspike.data.api.EntityRepository;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

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
}

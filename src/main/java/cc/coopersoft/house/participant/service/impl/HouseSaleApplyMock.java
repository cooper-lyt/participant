package cc.coopersoft.house.participant.service.impl;

import cc.coopersoft.house.participant.AttrUser;
import cc.coopersoft.house.participant.annotations.Seller;
import cc.coopersoft.house.participant.controller.HouseValidInfo;
import cc.coopersoft.house.participant.data.model.HouseSaleInfo;
import cc.coopersoft.house.participant.data.model.HouseSource;
import cc.coopersoft.house.participant.service.HouseSaleApply;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by cooper on 26/02/2017.
 */
public class HouseSaleApplyMock implements HouseSaleApply{

    @Inject
    private Logger logger;

    @Inject
    private EntityManager entityManager;

    @Inject
    private AttrUser attrUser;

    @Seller
    @Transactional
    public ValidResult validSaleHouse(HouseValidInfo info) {
        if ("1".equals(info.getHouseCode())) {

            logger.config("type : " + info.getValidType().name());
            logger.config("value: " + info.getHouseCode());

            logger.config("mbbh: " + info.getMapNumber() + " - " + info.getBlockNumber() + " - " + info.getBuildNumber() + " - " + info.getHouseNumber());

            HouseSaleInfo houseSaleInfo = new HouseSaleInfo();
            houseSaleInfo.setTitle("test");
            houseSaleInfo.setDescription("setDescription");
            houseSaleInfo.setRoomCount(3);
            houseSaleInfo.setLivingRoom(1);
            houseSaleInfo.setLocalArea("test setLocalArea");
            houseSaleInfo.setDecorate("test setLocalArea");
            houseSaleInfo.setLocalArea("test setLocalArea");
            houseSaleInfo.setCreateYear(2001);
            houseSaleInfo.setInFloor(3);
            houseSaleInfo.setHouseCode("666");

            houseSaleInfo.setSourceId("7777");

            //---  true
            houseSaleInfo.setStatus(HouseSaleInfo.HouseSourceStatus.PREPARE);
            houseSaleInfo.setSaleType(HouseSaleInfo.SaleType.SELLER);
            houseSaleInfo.setApplyTime(new Date());
            houseSaleInfo.setCheckTime(new Date());



            HouseSource houseSource = new HouseSource(UUID.randomUUID().toString().replace("-", ""), attrUser.getGroupId(), HouseSource.ShowType.TOW, info, houseSaleInfo);
            houseSaleInfo.setHouseSource(houseSource);
            entityManager.persist(houseSource);

            return new ValidResult(ValidStatus.SUCCESS,houseSource);
        }else{
            return new ValidResult(ValidStatus.HOUSE_NOT_FOUND,null);
        }
    }
}

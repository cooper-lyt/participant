package cc.coopersoft.house.participant.service.impl;

import cc.coopersoft.house.participant.data.HouseValidInfo;
import cc.coopersoft.house.participant.data.model.HouseSaleInfo;
import cc.coopersoft.house.participant.service.HouseSaleApply;

/**
 * Created by cooper on 26/02/2017.
 */
public class HouseSaleApplyMock implements HouseSaleApply{


    public HouseSaleInfo validSaleHouse(HouseValidInfo info) {
        HouseSaleInfo houseSaleInfo = new HouseSaleInfo() ;
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
        return houseSaleInfo;
    }
}

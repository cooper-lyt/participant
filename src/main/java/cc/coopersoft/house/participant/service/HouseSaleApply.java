package cc.coopersoft.house.participant.service;

import cc.coopersoft.house.participant.data.HouseValidInfo;
import cc.coopersoft.house.participant.data.model.HouseSaleInfo;

/**
 * Created by cooper on 22/02/2017.
 */
public interface HouseSaleApply {

    HouseSaleInfo validSaleHouse(HouseValidInfo info);

}

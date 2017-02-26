package cc.coopersoft.house.participant.service;

import cc.coopersoft.house.participant.controller.HouseValidInfo;
import cc.coopersoft.house.participant.data.model.HouseSource;

/**
 * Created by cooper on 22/02/2017.
 */
public interface HouseSaleApply {

    enum ValidStatus{
        SUCCESS, HOUSE_NOT_FOUND, OWNER_FAIL, ERROR
    }

    class ValidResult {
        private ValidStatus validStatus;
        private HouseSource houseSource;

        public ValidResult(ValidStatus validStatus, HouseSource houseSource) {
            this.validStatus = validStatus;
            this.houseSource = houseSource;
        }

        public ValidStatus getValidStatus() {
            return validStatus;
        }

        public void setValidStatus(ValidStatus validStatus) {
            this.validStatus = validStatus;
        }

        public HouseSource getHouseSource() {
            return houseSource;
        }

        public void setHouseSource(HouseSource houseSource) {
            this.houseSource = houseSource;
        }
    }


    ValidResult validSaleHouse(HouseValidInfo info);

}

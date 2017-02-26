package cc.coopersoft.house.participant.service.impl;

import cc.coopersoft.house.participant.data.model.HouseSaleInfo;
import cc.coopersoft.house.participant.data.model.HouseSource;
import cc.coopersoft.house.participant.service.HouseSaleApply;

/**
 * Created by cooper on 26/02/2017.
 */
public class ServerHouseSaleApply {

    class ValidInfo {

        private HouseSaleApply.ValidStatus validStatus;
        private HouseSaleInfo houseSaleInfo;
        private HouseSource.ShowType showType;

        public ValidInfo() {
        }

        public HouseSaleApply.ValidStatus getValidStatus() {
            return validStatus;
        }

        public void setValidStatus(HouseSaleApply.ValidStatus validStatus) {
            this.validStatus = validStatus;
        }

        public HouseSaleInfo getHouseSaleInfo() {
            return houseSaleInfo;
        }

        public void setHouseSaleInfo(HouseSaleInfo houseSaleInfo) {
            this.houseSaleInfo = houseSaleInfo;
        }

        public HouseSource.ShowType getShowType() {
            return showType;
        }

        public void setShowType(HouseSource.ShowType showType) {
            this.showType = showType;
        }
    }


}

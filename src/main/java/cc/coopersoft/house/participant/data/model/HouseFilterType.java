package cc.coopersoft.house.participant.data.model;

import cc.coopersoft.house.sale.data.HouseSource;

/**
 * Created by cooper on 24/09/2017.
 */
public class HouseFilterType {

    private HouseSource.HouseSourceStatus filterType;

    private long count;

    public HouseFilterType(HouseSource.HouseSourceStatus filterType, long count) {
        this.filterType = filterType;
        this.count = count;
    }

    public HouseSource.HouseSourceStatus getFilterType() {
        return filterType;
    }

    public void setFilterType(HouseSource.HouseSourceStatus filterType) {
        this.filterType = filterType;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}

package cc.coopersoft.house.participant.controller;

import cc.coopersoft.common.I18n;
import cc.coopersoft.common.PageResultData;
import cc.coopersoft.common.util.ConditionAdapter;
import cc.coopersoft.house.participant.data.repository.HouseSourceRepository;
import cc.coopersoft.house.sale.data.HouseSaleInfo;
import cc.coopersoft.house.sale.data.HouseSource;
import org.omnifaces.cdi.Param;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * Created by cooper on 22/04/2017.
 */
@Named
public class HouseSourceList {

    private static final int PAGE_SIZE = 15;

    @Inject
    private I18n i18n;

    @Inject
    @Param(name = "condition")
    private String condition;


    @Inject @Param(name = "filterType")
    private HouseSource.HouseSourceStatus filterType;

    @Inject @Param(name = "dateFrom")
    private Date dateFrom;

    @Inject @Param(name = "dateTo")
    private Date dateTo;

    @Inject @Param(name = "firstResult")
    private Integer firstResult;

    @Inject
    private HouseSourceRepository houseSourceRepository;

    public int getFirstResult() {
        if (firstResult == null){
            return 0;
        }
        return firstResult;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public HouseSource.HouseSourceStatus getFilterType() {
        return filterType;
    }

    public void setFilterType(HouseSource.HouseSourceStatus filterType) {
        this.filterType = filterType;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public ConditionAdapter getConditionAdapter() {
        return ConditionAdapter.instance(getCondition());
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = i18n.getDayBeginTime(dateFrom);
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = i18n.getDayEndTime(dateTo);
    }

    private PageResultData<HouseSource> resultData;

    public PageResultData<HouseSource> getResultData() {
        if (resultData == null){
            resultData = findResultData();
            this.firstResult = resultData.getFirstResult();
        }
        return resultData;
    }

    protected PageResultData<HouseSource> findResultData(){

        return new PageResultData<HouseSource>(houseSourceRepository.searchResultData(getConditionAdapter().getCondition(),getConditionAdapter().getContains(),getConditionAdapter().isEmpty(),getDateFrom(),getDateFrom() != null, getDateTo(),getDateTo() != null , filterType == null ? EnumSet.allOf(HouseSource.HouseSourceStatus.class) : EnumSet.of(filterType),getFirstResult(),PAGE_SIZE), getFirstResult(),
                houseSourceRepository.searchResultCount(getConditionAdapter().getCondition(),getConditionAdapter().getContains(),getConditionAdapter().isEmpty(),getDateFrom(),getDateFrom() != null, getDateTo(),getDateTo() != null , filterType == null ? EnumSet.allOf(HouseSource.HouseSourceStatus.class) : EnumSet.of(filterType)),PAGE_SIZE);

    }
}

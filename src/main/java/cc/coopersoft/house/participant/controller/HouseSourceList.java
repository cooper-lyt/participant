package cc.coopersoft.house.participant.controller;

import cc.coopersoft.common.I18n;
import cc.coopersoft.common.PageResultData;
import cc.coopersoft.common.util.ConditionAdapter;
import cc.coopersoft.house.participant.AttrUser;
import cc.coopersoft.house.participant.data.model.HouseFilterType;
import cc.coopersoft.house.participant.data.repository.HouseSourceRepository;
import cc.coopersoft.house.sale.data.HouseSource;
import org.omnifaces.cdi.Param;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by cooper on 22/04/2017.
 */
@Named
@RequestScoped
public class HouseSourceList {

    private static final int PAGE_SIZE = 15;

    @Inject
    private I18n i18n;

    @Inject @Param(name = "condition")
    private String condition;

    @Inject
    private Logger logger;

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

    @Inject
    private AttrUser attrUser;

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

    private List<HouseFilterType> houseSourceFilterTypeList;

    public List<HouseFilterType> getHouseSourceFilterTypeList() {
        if (houseSourceFilterTypeList == null){
            findResultData();
        }
        return houseSourceFilterTypeList;
    }

    public PageResultData<HouseSource> getResultData() {
        if (resultData == null){
            findResultData();
        }
        return resultData;
    }



    protected void findResultData(){

        resultData =  new PageResultData<HouseSource>(houseSourceRepository.searchResultData(attrUser.getLoginData().getCorpInfo().getId(),  getConditionAdapter().getCondition(),getConditionAdapter().getContains(),getConditionAdapter().isEmpty(),getDateFrom(),getDateFrom() != null, getDateTo(),getDateTo() != null , filterType == null ? EnumSet.allOf(HouseSource.HouseSourceStatus.class) : EnumSet.of(filterType),getFirstResult(),PAGE_SIZE), getFirstResult(),
                houseSourceRepository.searchResultCount(attrUser.getLoginData().getCorpInfo().getId(),getConditionAdapter().getCondition(),getConditionAdapter().getContains(),getConditionAdapter().isEmpty(),getDateFrom(),getDateFrom() != null, getDateTo(),getDateTo() != null , filterType == null ? EnumSet.allOf(HouseSource.HouseSourceStatus.class) : EnumSet.of(filterType)),PAGE_SIZE);
        this.firstResult = resultData.getFirstResult();

        houseSourceFilterTypeList = houseSourceRepository.searchHouseSourceFilterGroup(attrUser.getLoginData().getCorpInfo().getId(),getConditionAdapter().getCondition(),getConditionAdapter().getContains(),getConditionAdapter().isEmpty(),getDateFrom(),getDateFrom() != null, getDateTo(),getDateTo() != null );
    }

}

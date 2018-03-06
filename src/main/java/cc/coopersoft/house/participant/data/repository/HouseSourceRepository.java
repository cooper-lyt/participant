package cc.coopersoft.house.participant.data.repository;

import cc.coopersoft.house.participant.data.model.HouseFilterType;
import cc.coopersoft.house.sale.data.HouseSource;
import cc.coopersoft.house.sale.data.HouseSourceCompany;
import org.apache.deltaspike.data.api.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by cooper on 03/03/2017.
 */
@Repository
public interface HouseSourceRepository extends EntityRepository<HouseSource,String> {

    @Query("SELECT s FROM HouseSource s where s.status in (?1) and s.houseCode = ?2")
    List<HouseSource> houseSourceByStatus(List<HouseSource.HouseSourceStatus> statuses, String houseCode);

    @Query("SELECT s FROM HouseSourceCompany hsc left join hsc.houseSource s where s.status in (?1) and s.houseCode = ?2 and hsc.groupId = ?3")
    List<HouseSource> houseSourceByStatus(List<HouseSource.HouseSourceStatus> statuses, String houseCode, String attrCorpId);

    @Query("select hsc from HouseSourceCompany hsc left join fetch hsc.houseSource hs left join fetch hs.houseSaleInfo hsi left join fetch hs.proxyPerson pp  where hsc.groupId = :groupId and (hs.id = :condition or hs.houseCode = :condition or hs.sourceId = :condition  or hsc.title like :likeCondition or false = :hasCondition) and hs.status in (:statuses) and (hs.applyTime <= :endDate or false = :hasEndDate) and (hs.applyTime >= :startDate or false = :hasStartDate) order by hs.updateTime desc, hs.sourceId")
    List<HouseSourceCompany> searchResultData(@QueryParam("groupId") String groupId , @QueryParam("condition") String condition, @QueryParam("likeCondition") String likeCondition, @QueryParam("hasCondition") boolean hasCondition, @QueryParam("startDate") Date startDate, @QueryParam("hasStartDate") boolean hasStartDate , @QueryParam("endDate") Date endDate, @QueryParam("hasEndDate") boolean hasEndDate, @QueryParam("statuses") Set<HouseSource.HouseSourceStatus> statuses, @FirstResult int firstResult, @MaxResults int maxResults);

    @Query("select count(hsc) from HouseSourceCompany hsc left join hsc.houseSource hs left join hs.proxyPerson pp  where hsc.groupId = :groupId and (hs.id = :condition or hs.houseCode = :condition or hs.sourceId = :condition or hsc.title like :likeCondition or false = :hasCondition) and hs.status in (:statuses) and (hs.applyTime <= :endDate or false = :hasEndDate) and (hs.applyTime >= :startDate or false = :hasStartDate) order by hs.updateTime desc")
    Long searchResultCount(@QueryParam("groupId") String groupId, @QueryParam("condition") String condition, @QueryParam("likeCondition") String likeCondition, @QueryParam("hasCondition") boolean hasCondition, @QueryParam("startDate") Date startDate, @QueryParam("hasStartDate") boolean hasStartDate , @QueryParam("endDate") Date endDate, @QueryParam("hasEndDate") boolean hasEndDate,@QueryParam("statuses") Set<HouseSource.HouseSourceStatus> statuses);

    @Query("select new cc.coopersoft.house.participant.data.model.HouseFilterType(hs.status,count(hs.id)) from HouseSourceCompany hsc left join hsc.houseSource hs left join hs.proxyPerson pp  where hsc.groupId = :groupId and (hs.id = :condition or hs.houseCode = :condition or hs.sourceId = :condition  or hsc.title like :likeCondition or false = :hasCondition) and (hs.applyTime <= :endDate or false = :hasEndDate) and (hs.applyTime >= :startDate or false = :hasStartDate) group by hs.status")
    List<HouseFilterType> searchHouseSourceFilterGroup(@QueryParam("groupId") String groupId, @QueryParam("condition") String condition, @QueryParam("likeCondition") String likeCondition, @QueryParam("hasCondition") boolean hasCondition, @QueryParam("startDate") Date startDate, @QueryParam("hasStartDate") boolean hasStartDate , @QueryParam("endDate") Date endDate, @QueryParam("hasEndDate") boolean hasEndDate);

}

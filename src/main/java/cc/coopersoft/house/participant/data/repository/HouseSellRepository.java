package cc.coopersoft.house.participant.data.repository;

import cc.coopersoft.house.sale.data.HouseSaleInfo;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Created by cooper on 03/03/2017.
 */
@Repository
public interface HouseSellRepository extends EntityRepository<HouseSaleInfo,String> {

    @Query("SELECT s FROM HouseSaleInfo s where s.status in (?1) and s.houseCode = ?2")
    List<HouseSaleInfo> houseSalebyStatus(List<HouseSaleInfo.HouseSourceStatus> statuses, String houseCode);

}

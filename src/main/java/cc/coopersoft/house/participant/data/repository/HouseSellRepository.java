package cc.coopersoft.house.participant.data.repository;

import cc.coopersoft.house.sale.data.HouseSaleInfo;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

/**
 * Created by cooper on 03/03/2017.
 */
@Repository
public interface HouseSellRepository extends EntityRepository<HouseSaleInfo,String> {

    @Query("SELECT COUNT(s) FROM HouseSaleInfo s where s.status = 'CHECK' and s.houseCode = ?1")
    Long houseSaleingCount(String houseCode);

}

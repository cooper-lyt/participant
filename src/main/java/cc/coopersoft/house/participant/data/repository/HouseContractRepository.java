package cc.coopersoft.house.participant.data.repository;

import cc.coopersoft.house.sale.data.HouseContract;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

/**
 * Created by cooper on 10/10/2017.
 */
@Repository
public interface HouseContractRepository extends EntityRepository<HouseContract,String> {


}

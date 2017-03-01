package cc.coopersoft.house.participant.data.repository;

import cc.coopersoft.house.participant.data.model.SystemParam;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

/**
 * Created by cooper on 01/03/2017.
 */
@Repository
public interface RunParamRepository extends EntityRepository<SystemParam,String> {

}

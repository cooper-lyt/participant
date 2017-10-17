package cc.coopersoft.house.participant.data.repository;

import cc.coopersoft.house.participant.data.model.NumberSequence;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

/**
 * Created by cooper on 13/10/2017.
 */
@Repository
public interface NumberSequenceRepository extends EntityRepository<NumberSequence,String> {
}

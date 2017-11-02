package cc.coopersoft.house.participant.contract;

import com.dgsoft.house.SaleType;
import org.apache.deltaspike.core.api.config.view.ViewConfig;

/**
 * Created by cooper on 19/10/2017.
 */
public interface ContractPath {

    String getConfigName();

    Class<? extends ViewConfig> getEditPath(SaleType saleType);
}

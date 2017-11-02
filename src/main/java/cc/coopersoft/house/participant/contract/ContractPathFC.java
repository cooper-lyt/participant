package cc.coopersoft.house.participant.contract;

import com.dgsoft.house.SaleType;
import org.apache.deltaspike.core.api.config.view.ViewConfig;

import javax.enterprise.inject.Any;

/**
 * Created by cooper on 19/10/2017.
 */
@Any
public class ContractPathFC implements ContractPath {

    public String getConfigName() {
        return "";
    }

    public Class<? extends ViewConfig> getEditPath(SaleType saleType) {
        return null;
    }
}

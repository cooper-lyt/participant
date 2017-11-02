package cc.coopersoft.house.participant.contract;

import cc.coopersoft.house.participant.pages.Seller;
import com.dgsoft.house.SaleType;
import org.apache.deltaspike.core.api.config.view.ViewConfig;

import javax.enterprise.inject.Any;

/**
 * Created by cooper on 19/10/2017.
 */
@Any
public class ContractPathDG implements ContractPath {

    public String getConfigName() {
        return "donggang";
    }

    public Class<? extends ViewConfig> getEditPath(SaleType saleType) {
        return Seller.Contract.Dg.OldEdit.class;
    }
}

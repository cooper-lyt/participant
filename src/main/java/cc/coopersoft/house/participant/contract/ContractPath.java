package cc.coopersoft.house.participant.contract;

import cc.coopersoft.house.participant.data.ContractContextMap;
import com.dgsoft.house.SaleType;
import org.apache.deltaspike.core.api.config.view.ViewConfig;

import java.io.OutputStream;

/**
 * Created by cooper on 19/10/2017.
 */
public interface ContractPath {

    String getConfigName();


    Class<? extends ViewConfig> getEditPath(SaleType saleType,int ver);

    void pdf(ContractContextMap contractContextMap, OutputStream outputStream,int ver);


}

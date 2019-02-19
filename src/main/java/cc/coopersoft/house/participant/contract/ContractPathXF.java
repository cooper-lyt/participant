package cc.coopersoft.house.participant.contract;

import cc.coopersoft.house.participant.data.ContractContextMap;
import cc.coopersoft.house.participant.pages.Seller;
import com.dgsoft.house.SaleType;
import org.apache.deltaspike.core.api.config.view.ViewConfig;

import javax.enterprise.inject.Any;
import java.io.OutputStream;

@Any
public class ContractPathXF implements ContractPath {

    public String getConfigName() {
        return "xf";
    }

    public Class<? extends ViewConfig> getEditPath(SaleType saleType, int ver) {
        switch (ver) {

            case 1:
                return Seller.Contract.Xf.OldEdit.class;
            default:
                throw new IllegalArgumentException("unknow ver:" + ver);

        }
    }

    public void pdf(ContractContextMap contractContextMap, OutputStream outputStream, int ver) {
        switch (ver) {

            case 1:
                ContractPdfXf1.pdf(contractContextMap,outputStream);
                break;
            default:
                throw new IllegalArgumentException("unknow ver:" + ver);

        }
    }
}

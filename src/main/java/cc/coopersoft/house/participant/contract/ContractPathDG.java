package cc.coopersoft.house.participant.contract;

import cc.coopersoft.house.participant.data.ContractContextMap;
import cc.coopersoft.house.participant.pages.Seller;
import com.dgsoft.house.SaleType;
import org.apache.deltaspike.core.api.config.view.ViewConfig;

import javax.enterprise.inject.Any;
import java.io.OutputStream;

/**
 * Created by cooper on 19/10/2017.
 */
@Any
public class ContractPathDG implements ContractPath {


    public String getConfigName() {
        return "donggang";
    }


    public Class<? extends ViewConfig> getEditPath(SaleType saleType, int ver) {

        switch (ver) {

            case 1:
                return Seller.Contract.Dg.OldEdit.class;
            default:
                throw new IllegalArgumentException("unknow ver:" + ver);

        }

    }

    public void pdf(ContractContextMap contractContextMap, OutputStream outputStream, int ver) {

        switch (ver) {

            case 1:
                ContractPdfDG1.pdf(contractContextMap,outputStream);
                break;
            default:
                throw new IllegalArgumentException("unknow ver:" + ver);

        }


    }


}

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

    public Class<? extends ViewConfig> getEditPath(SaleType saleType) {

                return Seller.Contract.Xf.OldEdit.class;

    }

    public void pdf(ContractContextMap contractContextMap, OutputStream outputStream) {

                ContractPdfXf1.pdf(contractContextMap,outputStream);

    }

    public void agentPdf(ContractContextMap contractContextMap, OutputStream outputStream) {

                ContractPdfXf1.agentPdf(contractContextMap,outputStream);

    }

    public void seePdf(ContractContextMap contractContextMap, OutputStream outputStream) {
        ContractPdfXf1.seePdf(contractContextMap,outputStream);
    }

    public Class<? extends ViewConfig> getAgentEditPath() {
        return null;
    }
}

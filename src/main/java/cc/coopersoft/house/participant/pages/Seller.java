package cc.coopersoft.house.participant.pages;

import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.navigation.NavigationParameter;

/**
 * Created by cooper on 26/02/2017.
 */
public interface Seller {

    class Home implements ViewConfig {}

    class HouseSourceView implements ViewConfig{}

    class HouseList implements ViewConfig{}


    interface Contract extends ViewConfig{

        interface Dg extends Contract{

            class OldEdit implements Dg {}


        }

    }

    interface Apply extends ViewConfig{

        class HouseValid implements Apply {}

        class HouseSellInfo implements Apply {}

        class HouseShowInfo implements Apply{}

        class HouseSalePicUpload implements Apply{}


        class HouseSourceContract implements Apply{}

        class HouseSourceSubmit implements Apply{}

        class HouseSourceCommitted implements Apply{}

        class ContractBaseInfo implements Apply{}

        class ContractBuyerInfo implements Apply{}

        class ContractSellerInfo implements Apply{}

    }
}

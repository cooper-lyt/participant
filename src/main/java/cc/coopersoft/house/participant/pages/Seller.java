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

        class SubmitContract implements Contract{}

        interface Dg extends Contract{

            class OldEdit implements Dg {}


        }

        interface Fc extends Contract{
            class OldEdit implements Fc{}
        }

        interface Xf extends Contract{
            class OldEdit implements Xf {}
        }
    }

    interface Apply extends ViewConfig{

        class HouseValid implements Apply {}

        class HouseSellInfo implements Apply {}

        class HouseShowInfo implements Apply{}

        class HouseSalePicUpload implements Apply{}

        class HouseSourceCreate implements Apply{}


        class HouseSourceContract implements Apply{}

        class HouseSourceSubmit implements Apply{}

        class HouseSourceCommitted implements Apply{}

        class ContractBaseInfo implements Apply{}

        class ContractBuyerInfo implements Apply{}

        class ContractSellerInfo implements Apply{}

    }
}

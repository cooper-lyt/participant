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

    interface Apply extends ViewConfig{

        class HouseValid implements Apply {}

        class HouseSellInfo implements Apply {}

        class HouseSalePicUpload implements Apply{}

        class HouseSourceSubmit implements Apply{}

        class HouseSourceComited implements Apply{}
    }
}

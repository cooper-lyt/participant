package cc.coopersoft.house.participant.pages;

import org.apache.deltaspike.core.api.config.view.ViewConfig;

/**
 * Created by cooper on 26/02/2017.
 */
public interface Seller {

    class Home implements ViewConfig {}

    interface Apply extends ViewConfig{

        class HouseValid implements Apply {}

        class HouseSellInfo implements Apply {}

        class HouseSaleJoin implements Apply {}

        class HouseSalePicUpload implements Apply{}
    }
}

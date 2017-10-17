package cc.coopersoft.house.participant.data;

import cc.coopersoft.house.sale.data.PowerPerson;
import cc.coopersoft.house.sale.data.PowerProxyPerson;
import com.dgsoft.common.system.PowerPersonHelper;
import com.dgsoft.common.system.ProxyPersonEntity;

import java.math.BigDecimal;

/**
 * Created by cooper on 24/11/2016.
 */
public class ContractPowerPersonHelper extends PowerPersonHelper<PowerPerson> {

    public ContractPowerPersonHelper(PowerPerson entity, BigDecimal houseArea) {
        super(entity, houseArea);
    }

    @Override
    protected ProxyPersonEntity createProxyPerson() {
        return new PowerProxyPerson(getPersonEntity());
    }

    public PowerProxyPerson getProxyPerson(){
        return (PowerProxyPerson)getPersonEntity().getPowerProxyPerson();
    }

}

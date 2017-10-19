package cc.coopersoft.common;

import cc.coopersoft.house.ProxyType;
import com.dgsoft.common.system.PersonEntity;
import com.dgsoft.common.system.PowerPersonEntity;
import com.dgsoft.common.system.Sex;
import com.dgsoft.house.PoolType;
import com.dgsoft.house.SalePayType;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cooper on 25/02/2017.
 */
public class DataProduces {

    @Named
    @Produces
    @ApplicationScoped
    public PersonEntity.CredentialsType[] allCredentialsTypes(){
        return PersonEntity.CredentialsType.values();
    }

    @Named
    @Produces
    @ApplicationScoped
    public List<PersonEntity.CredentialsType> personCredentialsTypes(){
        List<PersonEntity.CredentialsType> result = new ArrayList<PersonEntity.CredentialsType>();
        for(PersonEntity.CredentialsType ct: allCredentialsTypes()){
            if (!ct.isCorp()){
                result.add(ct);
            }
        }
        return result;
    }

    @Named
    @Produces
    @ApplicationScoped
    public PoolType[] poolTypes(){
        return PoolType.values();
    }

    @Named
    @Produces
    @ApplicationScoped
    public SalePayType[] salePayTypeValues(){
        return SalePayType.values();
    }

    @Named
    @Produces
    @ApplicationScoped
    public Sex[] sexValues(){
        return Sex.values();
    }

    @Named
    @Produces
    @ApplicationScoped
    public ProxyType[] proxyTypeValues(){
        return ProxyType.values();
    }

    @Named
    @Produces
    @ApplicationScoped
    public PowerPersonEntity.LegalType[] legalTypeValues(){
        return PowerPersonEntity.LegalType.values();
    }
}

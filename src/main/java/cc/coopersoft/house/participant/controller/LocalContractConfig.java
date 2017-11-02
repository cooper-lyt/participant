package cc.coopersoft.house.participant.controller;

import cc.coopersoft.house.participant.contract.ContractPath;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * Created by cooper on 23/10/2017.
 */
@ApplicationScoped
public class LocalContractConfig implements java.io.Serializable{

    private static final String PATH_CONFIG_NAME = "CONTRACT_LOCATION";

    private ContractPath contractPath;

    public LocalContractConfig() {
    }

    @Inject
    public LocalContractConfig(@Default RunParam runParam , @Any Instance<ContractPath> contractPath) {
        for(ContractPath cp: contractPath){
            if( runParam.getStringParam(PATH_CONFIG_NAME).equals(cp.getConfigName())){
                this.contractPath = cp;
                break;
            }

        }
    }

    public ContractPath getConfig() {
        return contractPath;
    }

}

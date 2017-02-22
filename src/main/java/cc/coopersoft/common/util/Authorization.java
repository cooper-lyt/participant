package cc.coopersoft.common.util;

import org.picketlink.Identity;
import org.picketlink.authorization.util.AuthorizationUtil;
import org.picketlink.idm.PartitionManager;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by cooper on 22/02/2017.
 */
@Named("auth")
public class Authorization {

    @Inject
    private PartitionManager partitionManager;

    @Inject
    private Identity identity;


    public boolean hasRole(String roleName){
        //BasicModel.hasRole()
        return AuthorizationUtil.hasRole(identity,partitionManager,roleName);
    }


}

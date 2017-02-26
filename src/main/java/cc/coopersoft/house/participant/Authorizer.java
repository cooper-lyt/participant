package cc.coopersoft.house.participant;

import cc.coopersoft.house.participant.annotations.AttrCorp;
import cc.coopersoft.house.participant.annotations.Seller;
import org.apache.deltaspike.security.api.authorization.Secures;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.basic.BasicModel;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by cooper on 26/02/2017.
 */
@ApplicationScoped
public class Authorizer {

    @Secures
    @Seller
    public boolean doSellerCheck(Identity identity, IdentityManager identityManager, RelationshipManager relationshipManager) throws Exception{
        return BasicModel.hasRole(relationshipManager, identity.getAccount(), BasicModel.getRole(identityManager, "seller"));
    }

    @Secures
    @AttrCorp
    public boolean doAttrCorpCheck(Identity identity, IdentityManager identityManager, RelationshipManager relationshipManager) throws Exception{
        return BasicModel.hasRole(relationshipManager, identity.getAccount(), BasicModel.getRole(identityManager, "attrCorp"));
    }





}

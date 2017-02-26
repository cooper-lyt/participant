package cc.coopersoft.house.participant;

import net.bootsfaces.utils.FacesMessages;
import org.apache.deltaspike.jsf.api.message.JsfMessage;
import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.BaseAuthenticator;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.User;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Created by cooper on 19/02/2017.
 */
@PicketLink
public class ParticipantAuthenticator extends BaseAuthenticator{

    @Inject
    private DefaultLoginCredentials credentials;

    @Inject
    private JsfMessage<Messages> messages;

    @Inject
    private  RelationshipManager relationshipManager;

    @Inject
    private Logger logger;

    @Inject
    private IdentityManager identityManager;

    public void authenticate() {

        if ("root".equals(credentials.getUserId()) && "coopersoft".equals(credentials.getPassword())){
            setStatus(AuthenticationStatus.SUCCESS);

            User root = BasicModel.getUser(identityManager,"root");
            if (root == null){
                root = new User("root");
                identityManager.add(root);
            }

            BasicModel.grantRole(relationshipManager,root,BasicModel.getRole(identityManager,"seller"));
            BasicModel.grantRole(relationshipManager,root,BasicModel.getRole(identityManager,"attrCorp"));


            setAccount(root);

        } else {
            setStatus(AuthenticationStatus.FAILURE);
        }
    }
}

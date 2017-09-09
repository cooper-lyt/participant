package cc.coopersoft.house.participant;

import cc.coopersoft.comm.exception.HttpApiServerException;
import cc.coopersoft.house.participant.controller.RunParam;
import cc.coopersoft.house.participant.service.HouseSourceService;
import cc.coopersoft.house.sale.HouseSellService;
import cc.coopersoft.house.sale.data.Developer;
import cc.coopersoft.house.sale.data.Seller;
import com.dgsoft.developersale.LogonStatus;
import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.BaseAuthenticator;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.User;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cooper on 19/02/2017.
 */
@PicketLink
public class ParticipantAuthenticator extends BaseAuthenticator{

    @Inject
    private DefaultLoginCredentials credentials;

    @Inject
    private RunParam runParam;

    @Inject
    private RelationshipManager relationshipManager;

    @Inject
    private Logger logger;

    @Inject
    private IdentityManager identityManager;

    @Inject
    private AttrUser attrUser;

    @Inject
    private HouseSourceService houseSourceService;

    public void authenticate() {



        try {
            attrUser.setLoginData(HouseSellService.login(runParam.getStringParam("nginx_address"),credentials.getUserId(),credentials.getPassword(),attrUser.getRndData()));
            if (LogonStatus.LOGON.equals(attrUser.getLogonStatus())){
                User user = BasicModel.getUser(identityManager,attrUser.getLoginData().getAttrEmp().getId());
                if (user == null){
                    user = new User(attrUser.getLoginData().getAttrEmp().getId());
                    user.setFirstName(attrUser.getLoginData().getAttrEmp().getName());
                    identityManager.add(user);
                }
                attrUser.setKeyId(credentials.getUserId());
                if (attrUser.getLoginData().getCorpInfo() instanceof Seller){
                    //TODO 异步
                    houseSourceService.updateHouseSourceByCorp(attrUser.getLoginData().getCorpInfo().getId());
                    BasicModel.grantRole(relationshipManager,user,BasicModel.getRole(identityManager,"seller"));
                }else if (attrUser.getLoginData().getCorpInfo() instanceof Developer){
                    BasicModel.grantRole(relationshipManager,user,BasicModel.getRole(identityManager,"developer"));
                }else{
                    throw new IllegalArgumentException("unknow logon type:" + attrUser.getLoginData().getCorpInfo().getClass());
                }

                setAccount(user);
                setStatus(AuthenticationStatus.SUCCESS);


                return;
            }
        } catch (HttpApiServerException e) {
            logger.log(Level.WARNING,e.getMessage(),e);
        }

        setStatus(AuthenticationStatus.FAILURE);


    }
}

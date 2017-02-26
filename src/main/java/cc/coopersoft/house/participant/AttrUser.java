package cc.coopersoft.house.participant;

import org.picketlink.idm.model.annotation.IdentityStereotype;
import org.picketlink.idm.model.basic.User;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import static org.picketlink.idm.model.annotation.IdentityStereotype.Stereotype.USER;

/**
 * Created by cooper on 26/02/2017.
 */
@Named
@SessionScoped
public class AttrUser implements java.io.Serializable{

    public AttrUser() {
    }

    private String GroupId;
    private String GroupName;


    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

}

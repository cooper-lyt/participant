package cc.coopersoft.house.participant;

import cc.coopersoft.house.sale.data.LoginResult;
import com.dgsoft.developersale.LogonStatus;
import org.picketlink.idm.model.annotation.IdentityStereotype;
import org.picketlink.idm.model.basic.User;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import java.security.SecureRandom;

import static org.picketlink.idm.model.annotation.IdentityStereotype.Stereotype.USER;

/**
 * Created by cooper on 26/02/2017.
 */
@Named
@SessionScoped
public class AttrUser implements java.io.Serializable{

    public AttrUser() {
    }

    private LoginResult loginData;

    private String rndData;

    public String getRndData() {
        if (rndData == null){
            rndData = "";
            int b ;
            int a ;
            SecureRandom r = new SecureRandom();
            for (int i = 0; i < 32; i++) {
                a = r.nextInt(26);
                b = (char) (a + 65);
                rndData += new Character((char) b).toString();
            }
        }
        return rndData;
    }

    public LogonStatus getLogonStatus(){
        if (loginData == null){
            return LogonStatus.SERVER_ERROR;
        }else{
            return loginData.getLogonStatus();
        }
    }

    public LoginResult getLoginData() {
        return loginData;
    }

    public void setLoginData(LoginResult loginData) {
        this.loginData = loginData;
    }
}

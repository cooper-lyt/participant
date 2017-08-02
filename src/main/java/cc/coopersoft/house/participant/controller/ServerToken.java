package cc.coopersoft.house.participant.controller;

import cc.coopersoft.common.tools.Base64;
import cc.coopersoft.house.participant.AttrUser;
import cc.coopersoft.house.participant.Tools;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by cooper on 15/06/2017.
 */
@Named
@RequestScoped
public class ServerToken {

    @Inject
    private AttrUser attrUser;

    private String rndData;

    public String getRndData() {
        if (rndData == null){
            rndData = Tools.validRandomData();
        }
        return rndData;
    }

    public String getDigest(){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            String token = getRndData() + attrUser.getLoginData().getToken();

            messageDigest.update(token.getBytes());
            return new String(Base64.encode(messageDigest.digest()));

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.getMessage(),e);
        }
    }



}

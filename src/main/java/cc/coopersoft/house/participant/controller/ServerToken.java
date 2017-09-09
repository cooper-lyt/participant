package cc.coopersoft.house.participant.controller;

import cc.coopersoft.common.tools.Base64;
import cc.coopersoft.house.participant.AttrUser;
import cc.coopersoft.house.participant.Tools;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cooper on 15/06/2017.
 */
@Named
@RequestScoped
public class ServerToken {

    @Inject
    protected Logger logger;

    @Inject
    private AttrUser attrUser;

    private String rndData;

    private String digest;

    private void createToken(){
        rndData = Tools.validRandomData();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            String token = getRndData() + attrUser.getLoginData().getToken();

            messageDigest.update(token.getBytes());
            digest = URLEncoder.encode(new String(Base64.encode(messageDigest.digest())),"UTF-8");


        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.getMessage(),e);
        }catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage(),e);
        }
        logger.log(Level.CONFIG,"token: " + rndData + "|" + digest);
    }

    public String getRndData() {
        if (rndData == null){
            createToken();
        }
        return rndData;
    }

    public String getDigest(){
        if (digest == null){
            createToken();
        }
        return digest;
    }



}

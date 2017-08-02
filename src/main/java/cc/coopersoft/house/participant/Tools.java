package cc.coopersoft.house.participant;

import java.security.SecureRandom;

/**
 * Created by cooper on 15/06/2017.
 */
public class Tools {

    public static String validRandomData(){
        String rndData = "";
        int b ;
        int a ;
        SecureRandom r = new SecureRandom();
        for (int i = 0; i < 32; i++) {
            a = r.nextInt(26);
            b = (char) (a + 65);
            rndData += new Character((char) b).toString();
        }
        return rndData;
    }
}

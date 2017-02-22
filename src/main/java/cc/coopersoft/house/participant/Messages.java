package cc.coopersoft.house.participant;

import org.apache.deltaspike.core.api.message.MessageBundle;
import org.apache.deltaspike.core.api.message.MessageTemplate;

/**
 * Created by cooper on 6/8/16.
 */
@MessageBundle
public interface Messages{

    @MessageTemplate("{authenticate_fail}")
    String authenticateFail();




}

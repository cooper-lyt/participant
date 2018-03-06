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

    @MessageTemplate("{valid_house_not_fount}")
    String validHouseNotFound();

    @MessageTemplate("{valid_house_error}")
    String validHouseError();

    @MessageTemplate("{valid_house_owner_fail}")
    String validHouseOwnerFail();

    @MessageTemplate("{api_server_fail}")
    String serverFail();

    @MessageTemplate("{house_source_exists}")
    String houseSourceExists();

    @MessageTemplate("{house_source_only_exists}")
    String houseSourceOnlyExists();

    @MessageTemplate("{house_exists_to_edit}")
    String houseSourceToEdit();

    @MessageTemplate("{contract_commited}")
    String contractCommited();

    @MessageTemplate("{house_source_committed}")
    String houseSourceCommitted();

}

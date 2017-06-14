package cc.coopersoft.house.participant.faces;

import cc.coopersoft.house.sale.data.HouseValidInfo;

import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

/**
 * Created by cooper on 14/06/2017.
 */
@FacesConverter("validTypeConverter")
public class ValidTypeConverter extends EnumConverter {

    public ValidTypeConverter() {
        super(HouseValidInfo.ValidType.class);
    }
}

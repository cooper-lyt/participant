package cc.coopersoft.house.participant.controller;

import com.dgsoft.common.system.PersonEntity;

import javax.faces.bean.ViewScoped;
import javax.inject.Named;

/**
 * Created by cooper on 22/02/2017.
 */


@Named
@ViewScoped
public class HouseValidInfo implements PersonEntity, java.io.Serializable {

    public enum ValidType {
        MBBH_NUMBER,HOUSE_CODE,UNIT_NUMBER
    }

    public HouseValidInfo() {
        validType = ValidType.MBBH_NUMBER;
    }

    private CredentialsType credentialsType;
    private String credentialsNumber;
    private String powerCardNumber;
    private String personName;
    private String tel;


    private ValidType validType;

    private String mapNumber;
    private String blockNumber;
    private String buildNumber;
    private String houseNumber;

    private String unitNumber;

    private String houseCode;


    public String getCredentialsNumber() {
        return credentialsNumber;
    }

    public void setCredentialsNumber(String credentialsNumber) {
        this.credentialsNumber = credentialsNumber;
    }

    public String getPowerCardNumber() {
        return powerCardNumber;
    }

    public void setPowerCardNumber(String powerCardNumber) {
        this.powerCardNumber = powerCardNumber;
    }

    public ValidType getValidType() {
        return validType;
    }

    public void setValidType(ValidType validType) {
        this.validType = validType;
    }

    public String getMapNumber() {
        return mapNumber;
    }

    public void setMapNumber(String mapNumber) {
        this.mapNumber = mapNumber;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public CredentialsType getCredentialsType() {
        return credentialsType;
    }

    public void setCredentialsType(CredentialsType credentialsType) {
        this.credentialsType = credentialsType;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}

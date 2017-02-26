package cc.coopersoft.house.participant.data.model;

import cc.coopersoft.house.participant.controller.HouseValidInfo;
import com.dgsoft.common.system.PersonEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by cooper on 23/02/2017.
 */
@Entity
@Table(name = "HOUSE_SOURCE", catalog = "CONTRACT")
public class HouseSource implements PersonEntity , java.io.Serializable{

    public enum ShowType{
        TOW,SELF
    }

    public HouseSource() {
    }

    public HouseSource(String id, String groupId, ShowType showType, HouseValidInfo validInfo,HouseSaleInfo houseSaleInfo ){
        this.id = id;
        this.showType = showType;
        this.groupId = groupId;
        this.powerCardNumber = validInfo.getPowerCardNumber();
        this.credentialsNumber = validInfo.getCredentialsNumber();
        this.credentialsType = validInfo.getCredentialsType();
        this.personName = validInfo.getPersonName();
        this.tel = validInfo.getTel();
        this.houseSaleInfo = houseSaleInfo;
    }


    private String id;

    private String powerCardNumber;
    private CredentialsType credentialsType;
    private String credentialsNumber;
    private String personName;
    private String tel;
    private String groupId;
    private ShowType showType;


    private Long version;

    private HouseSaleInfo houseSaleInfo;

    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Column(name = "POWER_CARD_NUMBER",nullable = false, length = 50)
    @NotNull
    @Size(max = 50)
    public String getPowerCardNumber() {
        return powerCardNumber;
    }

    public void setPowerCardNumber(String powerCardNumber) {
        this.powerCardNumber = powerCardNumber;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CREDENTIALS_TYPE", nullable = false, length = 32)
    @NotNull
    public CredentialsType getCredentialsType() {
        return credentialsType;
    }

    public void setCredentialsType(CredentialsType credentialsType) {
        this.credentialsType = credentialsType;
    }

    @Column(name = "CREDENTIALS_NUMBER", nullable = false, length = 100)
    @NotNull
    @Size(max = 100)
    public String getCredentialsNumber() {
        return credentialsNumber;
    }

    public void setCredentialsNumber(String credentialsNumber) {
        this.credentialsNumber = credentialsNumber;
    }

    @Column(name = "OWNER_NAME",nullable = false, length = 50)
    @NotNull
    @Size(max = 50)
    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    @Column(name = "TEL", nullable = false, length = 16)
    @NotNull
    @Size(max = 16)
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Version
    @Column(name = "VERSION", nullable = false)
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "SHOW_TYPE",nullable = false,length = 20)
    @NotNull
    public ShowType getShowType() {
        return showType;
    }

    public void setShowType(ShowType showType) {
        this.showType = showType;
    }

    @Column(name = "MASTER_GROUP_ID",nullable = false,length = 32)
    @NotNull
    @Size(max = 32)
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public HouseSaleInfo getHouseSaleInfo() {
        return houseSaleInfo;
    }

    public void setHouseSaleInfo(HouseSaleInfo houseSaleInfo) {
        this.houseSaleInfo = houseSaleInfo;
    }
}

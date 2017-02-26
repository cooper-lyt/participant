package cc.coopersoft.house.participant.data.model;

import cc.coopersoft.house.participant.data.HouseValidInfo;
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

    public enum HouseSourceStatus{
        SUBMIT,PREPARE,CHECK,CANCEL,SELL
    }

    public HouseSource() {
    }

    public HouseSource(String id, String sourceId, HouseValidInfo vaildInfo){
        this.id = id;
        this.sourceId = sourceId;
        this.applyTime = new Date();
        this.powerCardNumber = vaildInfo.getPowerCardNumber();
        this.credentialsNumber = vaildInfo.getCredentialsNumber();
        this.credentialsType = vaildInfo.getCredentialsType();
        this.personName = vaildInfo.getPersonName();
        this.tel = vaildInfo.getTel();
        this.status = HouseSourceStatus.PREPARE;

    }


    private String id;
    private String sourceId;
    private Date applyTime;
    private String powerCardNumber;
    private CredentialsType credentialsType;
    private String credentialsNumber;
    private String personName;
    private String tel;
    private HouseSourceStatus status;
    private Date checkTime;
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

    @Column(name = "SOURCE_ID" , nullable = false, length = 32, unique = true)
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPLY_TIME", nullable = false, length = 19, columnDefinition = "DATETIME")
    @NotNull
    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    @NotNull
    public HouseSourceStatus getStatus() {
        return status;
    }

    public void setStatus(HouseSourceStatus status) {
        this.status = status;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPLY_TIME", length = 19, columnDefinition = "DATETIME")
    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    @Version
    @Column(name = "VERSION", nullable = false)
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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

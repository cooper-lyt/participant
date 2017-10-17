package cc.coopersoft.house.participant.data.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by cooper on 13/10/2017.
 */
@Entity
@Table(name = "NUMBER_SEQUENCE", catalog = "CONTRACT")
public class NumberSequence implements java.io.Serializable{

    public enum  SequenceType{
        DAY,MONTH,YEAR,LONG,APP
    }


    private String id;
    private SequenceType type;
    private long number;
    private long version;
    private Date updateTime;
    private int minLength;

    public NumberSequence() {
    }

    public NumberSequence(String id) {
        this.id = id;
        this.type = SequenceType.DAY;
        this.number = 1l;
        this.updateTime = new Date();
        this.minLength = 0;
    }

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

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 16,nullable = false)
    public SequenceType getType() {
        return type;
    }

    public void setType(SequenceType type) {
        this.type = type;
    }

    @Column(name = "NUMBER", nullable = false)
    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    @Version
    @Column(name = "VERSION",nullable = false)
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Column(name = "UPDATE_TIME",nullable = false)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "MIN_LEN",nullable = false)
    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }
}

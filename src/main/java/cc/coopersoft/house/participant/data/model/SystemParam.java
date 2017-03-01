package cc.coopersoft.house.participant.data.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by cooper on 11/4/15.
 */
@Entity
@Table(name = "SYSTEM_PARAM", catalog = "CONTRACT")
public class SystemParam {

    public enum ParamType{
        STRING,BOOLEAN,INTEGER,IMG
    }

    private String id;

    private String value;

    private String description;

    private ParamType type;


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

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "VALUE", columnDefinition = "LONGTEXT")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name="DESCRIPTION", length = 512, nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 16,nullable = false)
    public ParamType getType() {
        return type;
    }

    public void setType(ParamType type) {
        this.type = type;
    }
}

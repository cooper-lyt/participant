package cc.coopersoft.house.participant.data.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Created by cooper on 26/02/2017.
 */

@Entity
@Table(name = "HOUSE_SELL_INFO", catalog = "CONTRACT")
public class HouseSaleInfo implements java.io.Serializable{

    private String id;

    private String title;
    private String tags;
    private String description;
    private String environment;
    private BigDecimal lat;
    private BigDecimal lng;
    private Integer zoom;
    private int roomCount;
    private int livingRoom;
    private String localArea;
    private String schoolArea;
    private String metroArea;
    private String direction;
    private String decorate;
    private int createYear;
    private boolean elevator;
    private String cover;
    private int inFloor;
    private String houseCode;

    private HouseSource houseSource;

    public HouseSaleInfo() {
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

    @Column(name = "TITLE",nullable = false,length = 64)
    @NotNull
    @Size(max = 64)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "TAGS",length = 512)
    @Size(max = 512)
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Column(name = "DESCRIPTION",length = 512)
    @Size(max = 512)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "ENVIRONMENT", length = 512)
    @Size(max = 512)
    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @Column(name = "LAT", precision = 18,scale = 14)
    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    @Column(name = "LNG", precision = 18,scale = 14)
    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    @Column(name = "ZOOM")
    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    @Column(name = "ROOM_COUNT", nullable = false)
    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    @Column(name = "LIVING_ROOM", nullable = false)
    public int getLivingRoom() {
        return livingRoom;
    }

    public void setLivingRoom(int livingRoom) {
        this.livingRoom = livingRoom;
    }

    @Column(name = "LOCAL_AREA", length = 32)
    @Size(max = 32)
    public String getLocalArea() {
        return localArea;
    }

    public void setLocalArea(String localArea) {
        this.localArea = localArea;
    }

    @Column(name = "SCHOOL_AREA", length = 32)
    @Size(max = 32)
    public String getSchoolArea() {
        return schoolArea;
    }

    public void setSchoolArea(String schoolArea) {
        this.schoolArea = schoolArea;
    }

    @Column(name = "SCHOOL_AREA", length = 32)
    @Size(max = 32)
    public String getMetroArea() {
        return metroArea;
    }

    public void setMetroArea(String metroArea) {
        this.metroArea = metroArea;
    }

    @Column(name = "DIRECTION", length = 32)
    @Size(max = 32)
    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Column(name = "DECORATE", length = 512)
    public String getDecorate() {
        return decorate;
    }

    public void setDecorate(String decorate) {
        this.decorate = decorate;
    }

    @Column(name = "CREATE_YEAR" , nullable = false)
    public int getCreateYear() {
        return createYear;
    }

    public void setCreateYear(int createYear) {
        this.createYear = createYear;
    }

    @Column(name = "ELEVATOR" , nullable = false)
    public boolean isElevator() {
        return elevator;
    }

    public void setElevator(boolean elevator) {
        this.elevator = elevator;
    }

    @Column(name = "COVER",length = 32)
    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Column(name = "IN_FLOOR", nullable = false)
    public int getInFloor() {
        return inFloor;
    }

    public void setInFloor(int inFloor) {
        this.inFloor = inFloor;
    }

    @Column(name = "HOUSE_CODE" , nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "houseSaleInfo", optional = false)
    @PrimaryKeyJoinColumn
    @NotNull
    public HouseSource getHouseSource() {
        return houseSource;
    }

    public void setHouseSource(HouseSource houseSource) {
        this.houseSource = houseSource;
    }

    @PrePersist
    public void ensureId(){
        this.id = getHouseSource().getId();
    }
}

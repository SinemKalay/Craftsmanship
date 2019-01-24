package com.xebialabs.craftsmanship.domainObject;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.xebialabs.craftsmanship.domainObject.spaceships.Spaceship;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "player",
        uniqueConstraints = @UniqueConstraint(name = "userID", columnNames = {"userID"}))
public class PlayerDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column
    @NotNull(message = "User id of the player can not be null!")
    private String userID;

    @Column(nullable = false)
    @NotNull(message = "Full name of the player can not be null!")
    private String fullName;

    @ManyToOne
    @JoinColumn(name = "game_ID")
    @JsonBackReference
    @Nullable
    private GameDO gameDO;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Spaceship> spaceShips=new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @Nullable
    @JsonBackReference
    private List<SalvoDO> salvoDOs=new ArrayList<>();

    @Column
    private int shotRight;

    public PlayerDO(){}

    public PlayerDO(String userId, String fullName) {
        this.userID=userId;
        this.fullName=fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Nullable
    public GameDO getGameDO() {
        return gameDO;
    }

    public void setGameDO(@Nullable GameDO gameDO) {
        this.gameDO = gameDO;
    }

    public List<Spaceship> getSpaceShips() {
        return spaceShips;
    }

    public void setSpaceShips(List<Spaceship> spaceShips) {
        this.spaceShips = spaceShips;
    }

    public int getShotRight() {
        return shotRight;
    }

    public void setShotRight(int shotRight) {
        this.shotRight = shotRight;
    }

    public List<SalvoDO> getSalvoDOs() {
        return salvoDOs;
    }

    public void setSalvoDOs(List<SalvoDO> salvoDOs) {
        this.salvoDOs = salvoDOs;
    }
}

package com.xebialabs.craftsmanship.domainObject;

import com.xebialabs.craftsmanship.helper.SalvoTypeEnum;

import javax.persistence.*;

@Entity
@Table(name = "salvo")
public class SalvoDO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "coordinate_ID")
    private Coordinate coordinate;

    @Column(nullable = false)
    private SalvoTypeEnum salvoType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_ID")
    private PlayerDO playerDO;

    @Column
    private boolean hitOwnSpaceship=false;

    public SalvoDO() {
    }

    public SalvoDO(Coordinate coordinate, SalvoTypeEnum salvoType) {
        this.coordinate = coordinate;
        this.salvoType = salvoType;
    }

    public void setSalvoType(SalvoTypeEnum salvoType) {
        this.salvoType = salvoType;
    }

    public SalvoTypeEnum getSalvoType() {
        return salvoType;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public PlayerDO getPlayerDO() {
        return playerDO;
    }

    public void setPlayerDO(PlayerDO playerDO) {
        this.playerDO = playerDO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isHitOwnSpaceship() {
        return hitOwnSpaceship;
    }

    public void setHitOwnSpaceship(boolean hitOwnSpaceship) {
        this.hitOwnSpaceship = hitOwnSpaceship;
    }
}

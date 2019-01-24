package com.xebialabs.craftsmanship.domainObject.spaceships;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.xebialabs.craftsmanship.domainObject.Coordinate;
import com.xebialabs.craftsmanship.domainObject.GridDO;
import com.xebialabs.craftsmanship.domainObject.PlayerDO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "spaceship")
@Inheritance(
        strategy = InheritanceType.JOINED
)
public abstract class Spaceship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_ID")
    private PlayerDO playerDO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grid_ID")
    @JsonBackReference
    private GridDO gridDO;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Coordinate> coordinates = new HashSet<>();

    @Column(nullable = false)
    private int life;


    @Transient
    private int width;

    @Transient
    private int height;

    @Transient
    private String type;

    @Transient
    private boolean placed=false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerDO getPlayerDO() {
        return playerDO;
    }

    public void setPlayerDO(PlayerDO playerDO) {
        this.playerDO = playerDO;
    }

    public Set<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Set<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public GridDO getGridDO() {   return gridDO;    }

    public void setGridDO(GridDO gridDO) {  this.gridDO = gridDO;  }

    public int getWidth() {        return width;    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {        return height;    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPlaced() {
        return placed;
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

    public abstract void build(Coordinate initialCoordinate);

    public abstract void rotate(Coordinate initialCoordinate);

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }
}

package com.xebialabs.craftsmanship.domainObject;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.xebialabs.craftsmanship.domainObject.spaceships.Spaceship;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Table(name = "coordinate")
public class Coordinate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private int row;

    @Column(nullable = false)
    private int column;

    @Column(nullable = false)
    private boolean isTaken;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "grid_ID")
    @JsonBackReference
    @Nullable
    private GridDO gridDO;

    @ManyToOne
    @JoinColumn(name = "spaceship_ID")
    @Nullable
    private Spaceship spaceshipDO;

    @OneToOne
    @JoinColumn(name = "salvo_ID")
    private SalvoDO salvoDO;


    public Coordinate() {
    }

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Coordinate(int row, int column, String content) {
        this.row = row;
        this.column = column;
        this.content = content;
    }

    public Coordinate(int row, int column, String content, Spaceship spaceshipDO) {
        this.row = row;
        this.column = column;
        this.content = content;
        this.spaceshipDO = spaceshipDO;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void setTaken(boolean taken) {
        isTaken = taken;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SalvoDO getSalvoDO() {
        return salvoDO;
    }

    public void setSalvoDO(SalvoDO salvoDO) {
        this.salvoDO = salvoDO;
    }

    @Nullable
    public Spaceship getSpaceshipDO() {
        return spaceshipDO;
    }

    public void setSpaceshipDO(@Nullable Spaceship spaceshipDO) {
        this.spaceshipDO = spaceshipDO;
    }

    @Nullable
    public GridDO getGridDO() {
        return gridDO;
    }

    public void setGridDO(@Nullable GridDO gridDO) {
        this.gridDO = gridDO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;

        Coordinate that = (Coordinate) o;

        if (getRow() != that.getRow()) return false;
        return getColumn() == that.getColumn();
    }


    @Override
    public int hashCode() {
        int result = getRow();
        result = 31 * result + getColumn();
        return result;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }

    public String toHexDec() {
        return Integer.toHexString(row) + "x" + Integer.toHexString(column);
    }
}

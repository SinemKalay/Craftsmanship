package com.xebialabs.craftsmanship.domainObject;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.xebialabs.craftsmanship.domainObject.spaceships.Spaceship;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.xebialabs.craftsmanship.helper.ConstantValues.*;

@Entity
@Table(name = "grid")
public class GridDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

//    @Column(name = "board")
    @Transient
    private String[][] board;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonBackReference
//    @Transient
    private List<Coordinate> taken = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JsonBackReference
//    @Transient
    private List<Coordinate> free = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "gridDO")
    @JsonIgnore
    private GameDO gameDO;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Spaceship> spaceships = new ArrayList<>();


    public GridDO() {
        board = initBoard();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }

    public List<Coordinate> getTaken() {
        return taken;
    }

    public void setTaken(List<Coordinate> taken) {
        this.taken = taken;
    }

    public List<Coordinate> getFree() {
        return free;
    }

    public void setFree(List<Coordinate> free) {
        this.free = free;
    }

    public GameDO getGameDO() {
        return gameDO;
    }

    public void setGameDO(GameDO gameDO) {
        this.gameDO = gameDO;
    }

    public List<Spaceship> getSpaceships() {
        return spaceships;
    }

    public void setSpaceships(List<Spaceship> spaceships) {
        this.spaceships = spaceships;
    }

    private String[][] initBoard() {
        this.board = new String[GRID_HEIGHT][GRID_WIDTH];
        for (int i = 0; i < GRID_HEIGHT; i++)
            for (int j = 0; j < GRID_WIDTH; j++) {
                board[i][j] = EMPTY_QUADRANT;
                Coordinate coordinate=new Coordinate(i, j);
                coordinate.setGridDO(this);
                coordinate.setTaken(false);
                coordinate.setContent(EMPTY_QUADRANT);
                free.add(coordinate);
            }
        return board;
    }

    public String render() {
        StringBuilder board = new StringBuilder();

        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_HEIGHT; j++) {
                board.append(this.board[i][j]);
                if (j == (GRID_WIDTH - 1)) {
                    board.append("\n");
                    continue;
                }
            }
        }

        return board.toString();
    }

}

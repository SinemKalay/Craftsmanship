package com.xebialabs.craftsmanship.domainObject;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "game")
public class GameDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String gameID;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PlayerDO> players;

    @Column
    @Nullable
    private String starting;

    @Column
    @Nullable
    private String playerTurn;

    @Column
    @Nullable
    private String playerWon;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "grid_ID")
    private GridDO gridDO;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "protocol_ID")
    private SpaceshipProtocolDO spaceshipProtocolDO;

    //TODO: player turn field add

    public GameDO(){}
    public GameDO(String gameID, PlayerDO self, PlayerDO opponent, SpaceshipProtocolDO spaceshipProtocolDO) {

        this.gameID = gameID;
        this.spaceshipProtocolDO = spaceshipProtocolDO;
        this.players= new ArrayList<>();
        players.addAll(Arrays.asList(new PlayerDO[]{self, opponent}));

//        players=new HashMap<>();
//        players.put(SELF,self);
//        players.put(OPPONENT,opponent);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public List<PlayerDO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDO> players) {
        this.players = players;
    }

    @Nullable
    public String getStarting() {
        return starting;
    }

    public void setStarting(@Nullable String starting) {
        this.starting = starting;
    }

    @Nullable
    public String getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(@Nullable String playerTurn) {
        this.playerTurn = playerTurn;
    }

    @Nullable
    public String getPlayerWon() {
        return playerWon;
    }

    public void setPlayerWon(@Nullable String playerWon) {
        this.playerWon = playerWon;
    }

    public GridDO getGridDO() {
        return gridDO;
    }

    public void setGridDO(GridDO gridDO) {
        this.gridDO = gridDO;
    }

    public SpaceshipProtocolDO getSpaceshipProtocolDO() {
        return spaceshipProtocolDO;
    }

    public void setSpaceshipProtocolDO(SpaceshipProtocolDO spaceshipProtocolDO) {
        this.spaceshipProtocolDO = spaceshipProtocolDO;
    }
}

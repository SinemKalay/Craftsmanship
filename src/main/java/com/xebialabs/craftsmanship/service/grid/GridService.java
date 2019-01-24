package com.xebialabs.craftsmanship.service.grid;

import com.xebialabs.craftsmanship.domainObject.Coordinate;
import com.xebialabs.craftsmanship.domainObject.GridDO;
import com.xebialabs.craftsmanship.domainObject.PlayerDO;
import com.xebialabs.craftsmanship.domainObject.spaceships.*;
import com.xebialabs.craftsmanship.helper.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.xebialabs.craftsmanship.helper.ConstantValues.*;

@Service
public class GridService implements IGridService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public GridDO putSpaceshipsOnBoard(PlayerDO self, PlayerDO opponent) {
        GridDO gridDO = new GridDO();
        placeSpaceshipsOfPlayers(gridDO, self, opponent);

        return gridDO;
    }

    private void placeSpaceshipsOfPlayers(GridDO gridDO, PlayerDO self, PlayerDO opponent) {

        placeSpaceshipsOfPlayer(gridDO, self);
        placeSpaceshipsOfPlayer(gridDO, opponent);
    }

    private void placeSpaceshipsOfPlayer(GridDO gridDO, PlayerDO playerDO) {

        List<Spaceship> spaceships = new ArrayList<>();

        Spaceship aClassSpaceship = placeSpaceship(gridDO, new AClassSpaceship(), false);
        aClassSpaceship.setPlayerDO(playerDO);
        spaceships.add(aClassSpaceship);

        Spaceship bClassSpaceship = placeSpaceship(gridDO, new BClassSpaceship(), false);
        bClassSpaceship.setPlayerDO(playerDO);
        spaceships.add(bClassSpaceship);

        Spaceship sClassSpaceship = placeSpaceship(gridDO, new SClassSpaceship(), false);
        sClassSpaceship.setPlayerDO(playerDO);
        spaceships.add(sClassSpaceship);

        Spaceship wingerSpaceship = placeSpaceship(gridDO, new WingerSpaceship(), false);
        wingerSpaceship.setPlayerDO(playerDO);
        spaceships.add(wingerSpaceship);

        Spaceship angleSpaceship = placeSpaceship(gridDO, new AngleSpaceship(), false);
        angleSpaceship.setPlayerDO(playerDO);
        spaceships.add(angleSpaceship);

        //try to place unplaced spaceships
        spaceships = tryToPlaceSpaceships(gridDO, spaceships);
        playerDO.getSpaceShips().addAll(spaceships);
    }

    private List<Spaceship> tryToPlaceSpaceships(GridDO gridDO, List<Spaceship> spaceships) {
        int index = 0;
        boolean notPlaced = true;
        do {
            for (Spaceship spaceship : spaceships) {
                if (!spaceship.isPlaced()) {
                    spaceship = placeSpaceship(gridDO, spaceship, true);
                    spaceships.set(index, spaceship);
                }
                index++;
            }
            for (Spaceship spaceship : spaceships) {
                if (!spaceship.isPlaced()) {
                    notPlaced = false;
                }
            }
        } while (!notPlaced);
        return spaceships;
    }

    /**
     * Checked if the coordinates are available in the Grid.
     *
     * @param gridDO
     * @param spaceship
     *
     * @return
     */
    private boolean areGridCoordinatesAvailableForSpaceship(GridDO gridDO, Spaceship spaceship) {

        for (Coordinate coordinate : spaceship.getCoordinates()) {
            if (gridDO.getTaken().contains(coordinate)) {
                return false;
            }
        }

        return true;
    }

    public boolean isInitialCoordinateValid(Coordinate initialCoordinate, Spaceship spaceShip) {
        if ((initialCoordinate.getRow() + spaceShip.getHeight() >= GRID_HEIGHT) || (initialCoordinate.getColumn() + spaceShip.getWidth() >= GRID_WIDTH)) {
            return false;
        }

//        if ((initialCoordinate.getRow() - spaceShip.getHeight() < 0) || (initialCoordinate.getColumn() - spaceShip.getWidth() < 0)) {
//            return false;
//        }

        return true;
    }

    /**
     * The grid knows where to place the spaceship.
     *
     * @param spaceship
     */
    public Spaceship placeSpaceship(GridDO gridDO, Spaceship spaceship, boolean forceToPlace) {

        int counterPlaced = 0;
        boolean found = false;
        log.debug("PLACING SHIP ON GRID (FREE {}, TAKEN {})", gridDO.getFree().size(), gridDO.getTaken().size());
        // Random random = new Random();
        boolean rotate;

        while (!found) {
            Coordinate initialCoordinate;
            if (!forceToPlace) {
                initialCoordinate = gridDO.getFree().get(Utils.getRandomInt(0, gridDO.getFree().size()));
            } else {
                initialCoordinate = gridDO.getFree().get(counterPlaced);
                counterPlaced++;
            }

            if (!isInitialCoordinateValid(initialCoordinate, spaceship)) {
                continue;
            }

            spaceship.build(initialCoordinate);
            // rotate = random.nextBoolean();

//            if (rotate) {
//                spaceship.rotate(initialCoordinate);
//            }

            if (areGridCoordinatesAvailableForSpaceship(gridDO, spaceship)) {
                found = true;
                //TODO: check whether it works fine
                assignGridDOSpaceship(gridDO, spaceship.getCoordinates());
                for (Coordinate coordinate : spaceship.getCoordinates()) {
                    if (coordinate.getColumn() < 0 || coordinate.getRow() < 0) {
                        System.out.println("spaceship.getType(): " + spaceship.getType());
                    }
                }
                gridDO.getTaken().addAll(spaceship.getCoordinates());
                gridDO.getSpaceships().add(spaceship);
                spaceship.setGridDO(gridDO);
                break;
            }
            if (counterPlaced == gridDO.getFree().size()) {
                break;
            }
            if (!forceToPlace) {
                counterPlaced++;
            }
        }

        if (found) {
            spaceship.setPlaced(true);
            gridDO.getFree().removeAll(spaceship.getCoordinates());
        }


        return spaceship;

    }

    private void assignGridDOSpaceship(GridDO gridDO, Set<Coordinate> coordinates) {
        for (Coordinate coordinate : coordinates) {
            coordinate.setTaken(true);
            coordinate.setGridDO(gridDO);
        }

    }

    public String setBoard(GridDO gridDO) {

        String[][] board = new String[GRID_HEIGHT][GRID_WIDTH];

        setQuadrantToCoordinates(gridDO.getTaken(),board);
        setQuadrantToCoordinates(gridDO.getFree(),board);
        gridDO.setBoard(board);

        return gridDO.render();
    }

    private void setQuadrantToCoordinates(List<Coordinate> coordinates,String[][] board ) {

        for (Coordinate coordinate : coordinates) {
            System.out.println("coordinate.getRow()" + coordinate.getRow() + " coordinate.getColumn(): " + coordinate.getColumn());
            board[coordinate.getRow()][coordinate.getColumn()] = coordinate.getContent();
            System.out.println("ok");
        }
    }
}

package com.xebialabs.craftsmanship.domainObject.spaceships;

import com.xebialabs.craftsmanship.domainObject.Coordinate;
import com.xebialabs.craftsmanship.helper.Utils;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

import static com.xebialabs.craftsmanship.helper.ConstantValues.A_CLASS_SPACESHIP;
import static com.xebialabs.craftsmanship.helper.ConstantValues.NOT_HIT_QUADRANT;

/**
 *    *
 *   * *
 *   ***
 *   * *
 */

@Entity
@Table(name = "a_class_spaceship")
public class AClassSpaceship extends Spaceship {

    public AClassSpaceship() {
        super();
        this.setWidth(3);
        this.setHeight(4);
        this.setType(A_CLASS_SPACESHIP);
    }


    @Override
    public void build(Coordinate initialCoordinate) {

        Set<Coordinate> coordinates = new HashSet<>();

        initialCoordinate.setContent(NOT_HIT_QUADRANT);
        initialCoordinate.setSpaceshipDO(this);

        Coordinate secondPoint = new Coordinate(initialCoordinate.getRow() + 1, initialCoordinate.getColumn() - 1,NOT_HIT_QUADRANT,this);
        Coordinate thirdPoint = new Coordinate(initialCoordinate.getRow() + 1, initialCoordinate.getColumn() + 1,NOT_HIT_QUADRANT,this);

        Coordinate fourthPoint = new Coordinate(initialCoordinate.getRow() + 2, initialCoordinate.getColumn() - 1,NOT_HIT_QUADRANT,this);
        Coordinate fifthPoint = new Coordinate(initialCoordinate.getRow() + 2, initialCoordinate.getColumn(),NOT_HIT_QUADRANT,this);
        Coordinate sixthPoint = new Coordinate(initialCoordinate.getRow() + 2, initialCoordinate.getColumn() + 1,NOT_HIT_QUADRANT,this);

        Coordinate eightPoint = new Coordinate(initialCoordinate.getRow() + 3, initialCoordinate.getColumn() - 1,NOT_HIT_QUADRANT,this);
        Coordinate sevenPoint = new Coordinate(initialCoordinate.getRow() + 3, initialCoordinate.getColumn() + 1,NOT_HIT_QUADRANT,this);

        coordinates.add(initialCoordinate);
        coordinates.add(secondPoint);
        coordinates.add(thirdPoint);
        coordinates.add(fourthPoint);
        coordinates.add(fifthPoint);
        coordinates.add(sixthPoint);
        coordinates.add(sevenPoint);
        coordinates.add(eightPoint);

        this.setCoordinates(coordinates);
        this.setLife(coordinates.size());

    }

    @Override
    public void rotate(Coordinate initialCoordinate){

        Coordinate centralPoint = new Coordinate(initialCoordinate.getRow() + 2, initialCoordinate.getColumn(),NOT_HIT_QUADRANT);
        Set<Coordinate> newCoordinateSet = new HashSet<>();

        for (Coordinate coordinate : getCoordinates()) {
            Coordinate rotatedCoordinate = Utils.rotate(centralPoint, coordinate, 90);
            newCoordinateSet.add(rotatedCoordinate);
        }

        this.setCoordinates(newCoordinateSet);

    }

}

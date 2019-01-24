package com.xebialabs.craftsmanship.domainObject.spaceships;

import com.xebialabs.craftsmanship.domainObject.Coordinate;
import com.xebialabs.craftsmanship.helper.Utils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static com.xebialabs.craftsmanship.helper.ConstantValues.NOT_HIT_QUADRANT;
import static com.xebialabs.craftsmanship.helper.ConstantValues.WINGER_SPACESHIP;

/**
 *     * *
 *     * *
 *      *
 *     * *
 *     * *
 */

@Entity
@Table(name = "winger_spaceship")
public class WingerSpaceship extends Spaceship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;


    public WingerSpaceship() {
        super();
        this.setWidth(2);
        this.setHeight(5);
        this.setType(WINGER_SPACESHIP);
    }

    @Override
    public void build(Coordinate initialCoordinate) {

        Set<Coordinate> coordinates = new HashSet<>();

        initialCoordinate.setContent(NOT_HIT_QUADRANT);
        initialCoordinate.setSpaceshipDO(this);

        // Row 1
        Coordinate point2 = new Coordinate(initialCoordinate.getRow(), initialCoordinate.getColumn() + 2,NOT_HIT_QUADRANT,this);
        // Row 2
        Coordinate point3 = new Coordinate(initialCoordinate.getRow() + 1 , initialCoordinate.getColumn(),NOT_HIT_QUADRANT,this);
        Coordinate point4 = new Coordinate(initialCoordinate.getRow() + 1, initialCoordinate.getColumn() + 2,NOT_HIT_QUADRANT,this);
        // Row 3
        Coordinate point5 = new Coordinate(initialCoordinate.getRow() + 2, initialCoordinate.getColumn() + 1,NOT_HIT_QUADRANT,this);
        //Row 4
        Coordinate point6 = new Coordinate(initialCoordinate.getRow() + 3, initialCoordinate.getColumn(),NOT_HIT_QUADRANT,this);
        Coordinate point7 = new Coordinate(initialCoordinate.getRow() + 3, initialCoordinate.getColumn() + 2,NOT_HIT_QUADRANT,this);
        // Row 5
        Coordinate point8 = new Coordinate(initialCoordinate.getRow() + 4, initialCoordinate.getColumn(),NOT_HIT_QUADRANT,this);
        Coordinate point9 = new Coordinate(initialCoordinate.getRow() + 4, initialCoordinate.getColumn() + 2,NOT_HIT_QUADRANT,this);


        coordinates.add(initialCoordinate);
        coordinates.add(point2);
        coordinates.add(point3);
        coordinates.add(point4);
        coordinates.add(point5);
        coordinates.add(point6);
        coordinates.add(point7);
        coordinates.add(point8);
        coordinates.add(point9);

        this.setCoordinates(coordinates);
        this.setLife(coordinates.size());

    }

    public void rotate(Coordinate initialCoordinate){

        Coordinate centralPoint = new Coordinate(initialCoordinate.getRow() + 2, initialCoordinate.getColumn() +1,NOT_HIT_QUADRANT);
        Set<Coordinate> newCoordinateSet = new HashSet<>();

        for (Coordinate coordinate : getCoordinates()) {
            Coordinate rotatedCoordinate = Utils.rotate(centralPoint, coordinate, 90);
            newCoordinateSet.add(rotatedCoordinate);
        }

        this.setCoordinates(newCoordinateSet);
    }
}

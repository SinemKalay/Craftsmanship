package com.xebialabs.craftsmanship.helper;

import com.xebialabs.craftsmanship.domainObject.Coordinate;

import java.util.Random;

public class Utils {

    public static Coordinate rotate(Coordinate centralCoordinate, Coordinate coordinate, int angle) {
        double radians = (Math.PI / 180) * angle;
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        Double row = (cos * (coordinate.getRow() - centralCoordinate.getRow())) + (sin * (coordinate.getColumn() - centralCoordinate.getColumn())) + centralCoordinate.getRow();
        Double column = (cos * (coordinate.getColumn() - centralCoordinate.getColumn())) - (sin * (coordinate.getRow() - centralCoordinate.getRow())) + centralCoordinate.getColumn();
        Coordinate rotatedCoord = new Coordinate(row.intValue(), column.intValue());
        return rotatedCoord;
    }

    public static int getRandomInt(int low, int high) {
        Random r = new Random();
        int result = r.nextInt(high - low) + low;
        return result;
    }
}

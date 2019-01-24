package com.xebialabs.craftsmanship.helper.mapper;

import com.xebialabs.craftsmanship.dataTransferObjects.response.SalvoResponseDTO;
import com.xebialabs.craftsmanship.domainObject.Coordinate;
import com.xebialabs.craftsmanship.domainObject.SalvoDO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalvoMapper {

    public static Map<String, String> createSalvoMap(List<SalvoDO> salvoList) {
        Map<String, String> salvoMap = new HashMap<>();

        for (SalvoDO salvoDO : salvoList) {
            Coordinate coordinate = salvoDO.getCoordinate();
            String key = Integer.toHexString(coordinate.getRow()) + "x" + Integer.toHexString(coordinate.getColumn()).toUpperCase();
            salvoMap.put(key, salvoDO.getSalvoType().getDesc());
        }
        return salvoMap;
    }

    public static SalvoResponseDTO createSalvoResponseDTO(List<SalvoDO> salvoList, String playerTurn) {
        Map<String, String> salvoMap = createSalvoMap(salvoList);
        Map<String, String> game = new HashMap<>();
        game.put("player_turn", playerTurn);

        return new SalvoResponseDTO(salvoMap, game);
    }
}

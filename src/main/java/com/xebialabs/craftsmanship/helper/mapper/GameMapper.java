package com.xebialabs.craftsmanship.helper.mapper;

import com.xebialabs.craftsmanship.dataTransferObjects.response.GetGameResponseDTO;
import com.xebialabs.craftsmanship.domainObject.Coordinate;
import com.xebialabs.craftsmanship.domainObject.PlayerDO;

import java.util.List;

import static com.xebialabs.craftsmanship.helper.ConstantValues.*;

public class GameMapper {
    public static GetGameResponseDTO getGameResponseDTOMapper(List<PlayerDO> players, List<Coordinate> placesPlayerSpaceships , List<Coordinate> knownPlaces) {

        String[][] boardSpaceship = convertListToBoard(placesPlayerSpaceships);
        String[][] boardKnownCoordinates = convertListToBoard(knownPlaces);

        GetGameResponseDTO.GetGameDTOBuilder gameDTOBuilder = GetGameResponseDTO.newBuilder()
                .setSelfPlayer(PlayerMapper.playerDTOMapper(players.get(0), render(boardSpaceship)))
                .setOpponentPlayer(PlayerMapper.playerDTOMapper(players.get(1), render(boardKnownCoordinates)));

        return gameDTOBuilder.createGetGameDTOBuilder();
    }

    private static String[][] convertListToBoard(List<Coordinate> knownPlaces) {
        String[][] board = new String[GRID_HEIGHT][GRID_WIDTH];
        if(knownPlaces!=null) {
            knownPlaces.stream().forEach(p -> board[p.getRow()][p.getColumn()] = p.getContent());
        }
        return board;
    }

    public static String render(String[][] boardArray) {
        StringBuilder board = new StringBuilder();

        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_HEIGHT; j++) {
                if(boardArray[i][j]!=null) {
                    board.append(boardArray[i][j]);
                }
                else{
                    board.append(EMPTY_QUADRANT);
                }
                if (j == (GRID_WIDTH - 1)) {
                    board.append("\n");
                    continue;
                }
            }
        }

        return board.toString();
    }
}

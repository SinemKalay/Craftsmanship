package com.xebialabs.craftsmanship.helper.mapper;

import com.xebialabs.craftsmanship.dataTransferObjects.response.PlayerDTO;
import com.xebialabs.craftsmanship.domainObject.PlayerDO;

public class PlayerMapper {
    public static PlayerDTO playerDTOMapper(PlayerDO playerDO,String board) {

        PlayerDTO.PlayerDTOBuilder playerDTOBuilder=PlayerDTO.newBuilder()
                .setUserID(playerDO.getUserID())
                .setBoard(board);

        return playerDTOBuilder.createPlayerDTOBuilder();
    }
}

package com.xebialabs.craftsmanship.helper.mapper;

import com.xebialabs.craftsmanship.dataTransferObjects.response.GetGameResponseDTO;
import com.xebialabs.craftsmanship.domainObject.GameDO;

public class GameMapper {
    public static GetGameResponseDTO getGameResponseDTOMapper(GameDO gameDO, String board) {

    GetGameResponseDTO.GetGameDTOBuilder gameDTOBuilder=GetGameResponseDTO.newBuilder()
            .setSelfPlayer(PlayerMapper.playerDTOMapper(gameDO.getPlayers().get(0),board))
            .setOpponentPlayer(PlayerMapper.playerDTOMapper(gameDO.getPlayers().get(1),board));

    return gameDTOBuilder.createGetGameDTOBuilder();
    }
}

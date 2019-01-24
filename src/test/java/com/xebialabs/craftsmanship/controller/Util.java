package com.xebialabs.craftsmanship.controller;

import com.xebialabs.craftsmanship.dataTransferObjects.request.CreateGameRequestDTO;
import com.xebialabs.craftsmanship.domainObject.SpaceshipProtocolDO;

public class Util {

    protected static CreateGameRequestDTO getCreateGameRequestDTO(){
        CreateGameRequestDTO createGameRequestDTO = new CreateGameRequestDTO();
        createGameRequestDTO.setUserId("xebialabs-1");
        createGameRequestDTO.setFullName("XebiaLabs Opponent");
        createGameRequestDTO.setSpaceshipProtocolDO(new SpaceshipProtocolDO("127.0.0.1", 9001));

        return createGameRequestDTO;

    }
}

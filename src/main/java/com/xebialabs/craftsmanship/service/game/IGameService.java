package com.xebialabs.craftsmanship.service.game;

import com.xebialabs.craftsmanship.dataTransferObjects.request.CreateGameRequestDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.request.SalvoRequestDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.response.CreateGameResponseDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.response.GetGameResponseDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.response.SalvoResponseDTO;
import com.xebialabs.craftsmanship.domainObject.GameDO;
import com.xebialabs.craftsmanship.domainObject.PlayerDO;
import com.xebialabs.craftsmanship.domainObject.SpaceshipProtocolDO;
import com.xebialabs.craftsmanship.helper.exception.*;

public interface IGameService {

    CreateGameResponseDTO createGame(CreateGameRequestDTO createGameRequestDTO) throws UniqueConstraintException;

    GameDO saveGameInstance(String generateGameID, PlayerDO self, PlayerDO opponent, SpaceshipProtocolDO spaceshipProtocolDO);

    GetGameResponseDTO getGame(String gameID, String id) throws EntityNotFoundException, NotFoundUserException;

    SalvoResponseDTO fire(String gameID, SalvoRequestDTO salvoRequestDTO, boolean isSelfUser) throws EntityNotFoundException, TooMuchShotException, NotYourTurnException, GameOverException;
}

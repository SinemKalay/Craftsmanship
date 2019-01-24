package com.xebialabs.craftsmanship.service.game;

import com.xebialabs.craftsmanship.dataAccessObject.GameRepository;
import com.xebialabs.craftsmanship.dataTransferObjects.request.CreateGameRequestDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.request.SalvoRequestDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.response.CreateGameResponseDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.response.GetGameResponseDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.response.SalvoResponseDTO;
import com.xebialabs.craftsmanship.domainObject.*;
import com.xebialabs.craftsmanship.domainObject.spaceships.Spaceship;
import com.xebialabs.craftsmanship.helper.ConstantValues;
import com.xebialabs.craftsmanship.helper.SalvoTypeEnum;
import com.xebialabs.craftsmanship.helper.exception.EntityNotFoundException;
import com.xebialabs.craftsmanship.helper.exception.TooMuchShotException;
import com.xebialabs.craftsmanship.helper.mapper.GameMapper;
import com.xebialabs.craftsmanship.helper.mapper.SalvoMapper;
import com.xebialabs.craftsmanship.service.grid.GridService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.xebialabs.craftsmanship.helper.ConstantValues.HIT_QUADRANT;
import static com.xebialabs.craftsmanship.helper.ConstantValues.MISSED_QUADRANT;
import static com.xebialabs.craftsmanship.helper.ConstantValues.NOT_HIT_QUADRANT;

@Service
public class GameService implements IGameService {

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(GameService.class);

    private static HashMap<String, GameDO> gameMap = new HashMap<>();

    @Autowired
    GameRepository gameRepository;

    @Autowired
    GridService gridService;

    public GameService(final GameRepository gameRepository, final GridService gridService) {
        this.gameRepository = gameRepository;
        this.gridService = gridService;
    }

    //TODO: spaceships can be placed as rotated

    /**
     * Save game instance which is created with all spaceships and grid components
     *
     * @param createGameRequestDTO coming DTO to mapped game
     * @return CreateGameResponseDTO will be returned appropriate type in requirements doc.
     */
    @Override
    public CreateGameResponseDTO createGame(CreateGameRequestDTO createGameRequestDTO) {

        PlayerDO self = new PlayerDO("player", "Assessment PlayerDO");
        PlayerDO opponent = new PlayerDO(createGameRequestDTO.getUserId(), createGameRequestDTO.getFullName());

        GameDO gameDO = saveGameInstance(generateGameID(), self, opponent, createGameRequestDTO.getSpaceshipProtocolDO());

        return this.generateCreatedGameResponse(gameDO);
    }

    /**
     * returns saved game instance as GameDO
     *
     * @param generateGameID      should be unique
     * @param self                player
     * @param opponent            player
     * @param spaceshipProtocolDO SpaceshipProtocolDO
     * @return gameDO saved to DB
     */
    @Override
    public GameDO saveGameInstance(String generateGameID, PlayerDO self, PlayerDO opponent, SpaceshipProtocolDO spaceshipProtocolDO) {
        GameDO gameDO = new GameDO(generateGameID(), self, opponent, spaceshipProtocolDO);
        gameDO.setGridDO(gridService.putSpaceshipsOnBoard(self, opponent));
        return gameRepository.save(this.setupRelations(gameDO, self, opponent));
    }

    /**
     * returns CreateGameResponseDTO created by gameDO coming from DB
     *
     * @param gameDO coming from DB
     * @return CreateGameResponseDTO
     */
    private CreateGameResponseDTO generateCreatedGameResponse(GameDO gameDO) {

        CreateGameResponseDTO createGameResponseDTO = new CreateGameResponseDTO(gameDO.getPlayers().get(0).getUserID(), gameDO.getPlayers().get(1).getFullName(), gameDO.getGameID(), gameDO.getStarting());

        return createGameResponseDTO;
    }

    //TODO: user is suppossed to not able to see other player board

    /**
     * Get game by gameId and userID.
     *
     * @param gameID String to get match grid
     * @param userID to specify whose board can be seen.
     * @return GetGameResponseDTO will be returned appropriate type in requirements doc.
     */
    @Override
    public GetGameResponseDTO getGame(String gameID, String userID) throws EntityNotFoundException {

        GameDO gameDO = gameRepository.findByGameID(gameID).orElseThrow(() -> new EntityNotFoundException("Couldn't find any game with given game id"));
        String board = gridService.setBoard(gameDO.getGridDO());

        LOG.info("Game found with received game id:" + gameID);


        return GameMapper.getGameResponseDTOMapper(gameDO, board);
    }

    /**
     * Get shot by opponent or shot to opponent
     *
     * @param gameID          String to get match grid
     * @param salvoRequestDTO SalvoRequestDTO to get salvo coordinates
     * @return SalvoResponseDTO
     * @throws EntityNotFoundException if there is no game has gameID equals to received gameID
     * @throws TooMuchShotException    if requested number bigger from shot right
     */
    @Override
    public SalvoResponseDTO fire(String gameID, SalvoRequestDTO salvoRequestDTO, boolean isSelfUser) throws EntityNotFoundException, TooMuchShotException {

        GameDO gameDO = gameRepository.findByGameID(gameID).orElseThrow(() -> new EntityNotFoundException("Couldn't find any game with given game id"));
        PlayerDO playerDO = isSelfUser ? gameDO.getPlayers().get(0) : gameDO.getPlayers().get(1);

        if (canFire(playerDO.getShotRight(), salvoRequestDTO.getSalvo().size())) {
            List<SalvoDO> salvoList = createSalvoList(gameDO.getGridDO().getTaken(), gameDO.getGridDO().getFree(), salvoRequestDTO.getSalvo(), playerDO);
            gameDO.getPlayers().get(isSelfUser?0:1).setSalvoDOs(salvoList);
            gameDO.setPlayerTurn((!isSelfUser ? gameDO.getPlayers().get(0) : gameDO.getPlayers().get(1)).getUserID());

            gameDO = gameRepository.save(gameDO);

            return SalvoMapper.createSalvoResponseDTO(salvoList, gameDO.getPlayerTurn());
        }

        return null;
    }

    /**
     * Checks whether player has enough shot as number of shots in request
     * Shot right equals to number of survived spaceships
     *
     * @param shotRightOfPlayer String to get match grid
     * @param numberOfShots     SalvoRequestDTO to get salvo coordinates
     * @return boolean for can fire or not
     * @throws TooMuchShotException if requested number bigger from shot right
     */
    public boolean canFire(int shotRightOfPlayer, int numberOfShots) throws TooMuchShotException {

        if (numberOfShots > shotRightOfPlayer) {
            throw new TooMuchShotException("Number of shots can not be more than spaceships you have!");
        }
        return true;
    }


    /**
     * Creates salvo list by checking every coordinate belongs to shot
     * If salvo shot run into attacker player spaceship it counted as miss
     *
     * @param taken      field in grid
     * @param shotsInHex come from DTO to be parse into coordinates
     * @param playerDO attacker   player userID
     * @return salvo which contains list of shots
     */
    private List<SalvoDO> createSalvoList(List<Coordinate> taken, List<Coordinate> free, List<String> shotsInHex, PlayerDO playerDO) {
        List<Coordinate> shots = parseFiredCoordinates(shotsInHex);
        List<SalvoDO> salvoDOS = new ArrayList<>();
        SalvoDO salvoDO;
        int indexInList;

        for (Coordinate coordinate : shots) {
            if (taken.contains(coordinate)) {
                indexInList = indexInList(taken, coordinate);
                if (!isOwner(taken, indexInList, coordinate, playerDO.getUserID())) {
                    salvoDO = checkSalvoType(taken, taken.get(indexInList));
                    updateListField(taken, indexInList, HIT_QUADRANT);

                } else {
                    salvoDO = new SalvoDO(coordinate, SalvoTypeEnum.MISS);
                }
            } else {
                salvoDO = new SalvoDO(coordinate, SalvoTypeEnum.MISS);
                indexInList = indexInList(free, coordinate);
                updateListField(free, indexInList, MISSED_QUADRANT);
            }
            coordinate.setSalvoDO(salvoDO);
            salvoDO.setPlayerDO(playerDO);
            salvoDOS.add(salvoDO);
        }
        return salvoDOS;
    }

    private void updateListField(List<Coordinate> takenFreeFieldList, int index, String content) {
        takenFreeFieldList.get(index).setContent(content);
    }


    private int indexInList(List<Coordinate> taken, Coordinate coordinate) {
        for (int i = 0; i < taken.size(); i++) {
            if (taken.get(i).equals(coordinate)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isOwner(List<Coordinate> taken, int indexInTaken, Coordinate coordinate, String attacker) {

        if (taken.get(indexInTaken).equals(coordinate)) {
            if (taken.get(indexInTaken).getSpaceshipDO().getPlayerDO().getUserID().equals(attacker)) {
                return true;
            }
        }

        return false;
    }

    /**
     * creates a salvo according to circumcitances
     *
     * @param coordinate of shot
     * @return salvoDO
     */
    private SalvoDO checkSalvoType(List<Coordinate> taken, Coordinate coordinate) {
        SalvoDO salvoDO = new SalvoDO(coordinate, SalvoTypeEnum.MISS);
        if (coordinate.getContent().equals(NOT_HIT_QUADRANT)) {
            Spaceship spaceship = coordinate.getSpaceshipDO();
            spaceship.setLife(spaceship.getLife() - 1);
            if (spaceship.getLife() == 0) {
                salvoDO = new SalvoDO(coordinate, SalvoTypeEnum.KILL);
                return salvoDO;
            }
            salvoDO.setSalvoType(SalvoTypeEnum.HIT);
        }
        return salvoDO;
    }

    /**
     * Coordinates extracting from salvo coming as["0x0", "8x4", "DxA", "AxA", "7xF"]
     *
     * @param salvo List<String>
     * @return shots List<Coordinate>
     */
    private List<Coordinate> parseFiredCoordinates(List<String> salvo) {

        List<Coordinate> shots = new ArrayList<>();

        for (String hexCoordinates : salvo) {
            String[] hexCoordinate = hexCoordinates.split("x");
            int x = Integer.parseInt(hexCoordinate[0], 16);
            int y = Integer.parseInt(hexCoordinate[1], 16);

            shots.add(new Coordinate(x, y));
        }
        return shots;
    }

    /**
     * returns generated game id which starts with "match-"
     *
     * @return generated game id
     */
    private String generateGameID() {

        return ConstantValues.GAME_ID + gameMap.size() + 1;

    }

    /**
     * returns userID of  a random player between players who start to match
     *
     * @param players List<PlayerDO>
     * @return String userID of picked player
     */
    private String pickStartingPlayer(List<PlayerDO> players) {
        Random rand = new Random();
        PlayerDO pickedPlayerDO = players.get(rand.nextInt(players.size()));

        return pickedPlayerDO.getUserID();
    }

    /**
     * returns a random player between players who start to match
     *
     * @param gameDO   GameDO
     * @param self     PlayerDO
     * @param opponent PlayerDO
     * @return String
     */
    private GameDO setupRelations(GameDO gameDO, PlayerDO self, PlayerDO opponent) {
        self.setGameDO(gameDO);
        self.setShotRight(self.getSpaceShips().size());
        opponent.setGameDO(gameDO);
        opponent.setShotRight(opponent.getSpaceShips().size());
        gameDO.setStarting(pickStartingPlayer(gameDO.getPlayers()));

        return gameDO;

    }
}



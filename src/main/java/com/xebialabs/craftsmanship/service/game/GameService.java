package com.xebialabs.craftsmanship.service.game;

import com.xebialabs.craftsmanship.dataAccessObject.GameRepository;
import com.xebialabs.craftsmanship.dataAccessObject.PlayerRepository;
import com.xebialabs.craftsmanship.dataTransferObjects.request.CreateGameRequestDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.request.SalvoRequestDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.response.CreateGameResponseDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.response.GetGameResponseDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.response.SalvoResponseDTO;
import com.xebialabs.craftsmanship.domainObject.*;
import com.xebialabs.craftsmanship.domainObject.spaceships.Spaceship;
import com.xebialabs.craftsmanship.helper.ConstantValues;
import com.xebialabs.craftsmanship.helper.SalvoTypeEnum;
import com.xebialabs.craftsmanship.helper.exception.*;
import com.xebialabs.craftsmanship.helper.mapper.GameMapper;
import com.xebialabs.craftsmanship.helper.mapper.SalvoMapper;
import com.xebialabs.craftsmanship.service.grid.GridService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.xebialabs.craftsmanship.helper.ConstantValues.*;


//TODO: check player turn fire service

@Service
public class GameService implements IGameService {

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(GameService.class);

    private static HashMap<String, GameDO> gameMap = new HashMap<>();

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GridService gridService;

    public GameService(final GameRepository gameRepository, final GridService gridService) {
        this.gameRepository = gameRepository;
        this.gridService = gridService;
    }

    /**
     * Save game instance which is created with all spaceships and grid components
     *
     * @param createGameRequestDTO coming DTO to mapped game
     * @return CreateGameResponseDTO will be returned appropriate type in requirements doc.
     */
    @Override
    public CreateGameResponseDTO createGame(CreateGameRequestDTO createGameRequestDTO) throws UniqueConstraintException {

        PlayerDO self = lookForPlayer("player", "Assessment PlayerDO");

        PlayerDO opponent = lookForPlayer(createGameRequestDTO.getUserId(), createGameRequestDTO.getFullName());
        GameDO gameDO = saveGameInstance(generateGameID(), self, opponent, createGameRequestDTO.getSpaceshipProtocolDO());
        gameMap.put(gameDO.getGameID(), gameDO);

        return this.generateCreatedGameResponse(gameDO);

    }

    /**
     * returns player if exist in DB if not returns new player instance
     *
     * @param userId   search param
     * @param fullName search param
     * @return player instance
     * @throws UniqueConstraintException if received json contains existing player
     */
    private PlayerDO lookForPlayer(String userId, String fullName) throws UniqueConstraintException {
        Optional<PlayerDO> playerDO = playerRepository.findByUserIDAndFullName(userId, fullName);
        if (!playerDO.isPresent()) {
            return new PlayerDO(userId, fullName);
        } else {
            throw new UniqueConstraintException("Please try with different player information");
        }
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

        CreateGameResponseDTO createGameResponseDTO = new CreateGameResponseDTO(gameDO.getPlayers().get(0).getUserID(), gameDO.getPlayers().get(0).getFullName(), gameDO.getGameID(), gameDO.getStarting());

        return createGameResponseDTO;
    }

    /**
     * Get game by gameId and userID.
     *
     * @param gameID String to get match grid
     * @param userID to specify whose board can be seen.
     * @return GetGameResponseDTO will be returned appropriate type in requirements doc.
     */
    @Override
    public GetGameResponseDTO getGame(String gameID, String userID) throws EntityNotFoundException, NotFoundUserException {

        GameDO gameDO = gameRepository.findByGameID(gameID).orElseThrow(() -> new EntityNotFoundException("Couldn't find any game with given game id"));
        PlayerDO playerDO = getPlayer(gameDO, userID);
        List<Coordinate> placesOfSpaceships = placesOfSpaceships(playerDO.getSpaceShips());
        List<Coordinate> knownPlaces = placesOfPlayerSalvo(playerDO.getSalvoDOs());

        return GameMapper.getGameResponseDTOMapper(gameDO.getPlayers(), placesOfSpaceships, knownPlaces);
    }

    /**
     * returns coordinates of all spaceships that player has
     *
     * @param spaceships which belongs to the player
     * @return places of all spaceships
     */
    private List<Coordinate> placesOfSpaceships(List<Spaceship> spaceships) {
        List<Coordinate> knownPlaces = new ArrayList<>();
        spaceships.stream().forEach(s -> knownPlaces.addAll(s.getCoordinates()));

        return knownPlaces;
    }

    /**
     * returns coordinates of all salvos which done by the player
     * if salvo is hit own ship it not counted as known place.
     *
     * @param salvoDOS which belongs to the player
     * @return List<Coordinate>
     */
    private List<Coordinate> placesOfPlayerSalvo(List<SalvoDO> salvoDOS) {
        List<Coordinate> knownPlaces = new ArrayList<>();

        salvoDOS.stream().filter(s -> !s.isHitOwnSpaceship()).forEach(s -> knownPlaces.add(s.getCoordinate()));

        return knownPlaces;
    }

    /**
     * returns player of game who has received userID
     *
     * @param gameDO Game
     * @param userID of wanted player
     * @return player appropriate to criterias
     */
    private PlayerDO getPlayer(GameDO gameDO, String userID) throws NotFoundUserException {
        return gameDO.getPlayers().stream().filter(p -> p.getUserID().equals(userID)).findFirst().orElseThrow(() -> new NotFoundUserException("User not name has user id as " + userID));
    }

    /**
     * Get shot by opponent or shot to opponent
     *
     * @param gameID          String to get match grid
     * @param salvoRequestDTO SalvoRequestDTO to get salvo coordinates
     * @return SalvoResponseDTO
     * @return SalvoResponseDTO
     * @throws EntityNotFoundException if there is no game has gameID equals to received gameID
     * @throws TooMuchShotException    if requested number bigger from shot right
     */
    @Override
    public SalvoResponseDTO fire(String gameID, SalvoRequestDTO salvoRequestDTO, boolean isSelfUser) throws EntityNotFoundException, TooMuchShotException, NotYourTurnException, GameOverException {
        GameDO gameDO = gameRepository.findByGameID(gameID).orElseThrow(() -> new EntityNotFoundException("Couldn't find any game with given game id"));
        SalvoResponseDTO salvoResponseDTO = null;

        if (gameDO.getPlayerWon() == null) {
            int playerIndex = isSelfUser ? 0 : 1;
            PlayerDO playerDO = gameDO.getPlayers().get(playerIndex);

            if (isPlayerTurn(gameDO.getPlayerTurn(), playerDO.getUserID())) {
                salvoResponseDTO = firingUp(gameDO, salvoRequestDTO.getSalvo(), playerIndex, playerDO);
            }
        }
        else{
            throw new GameOverException("Game over! Could not shot anymore!");
        }

        return salvoResponseDTO;

    }

    private boolean isPlayerTurn(String playerTurn, String userID) throws NotYourTurnException {

        if (!playerTurn.equals(userID)) {
            throw new NotYourTurnException("Please wait for opponent move! It is not your turn.");
        }
        return true;

    }

    private SalvoResponseDTO firingUp(GameDO gameDO, List<String> salvoHex, int playerIndex, PlayerDO playerDO) throws TooMuchShotException {
        if (isShotNumberOk(playerDO.getShotRight(), salvoHex.size())) {
            List<SalvoDO> salvoList = createSalvoList(gameDO.getGridDO().getTaken(), gameDO.getGridDO().getFree(), salvoHex, playerDO);
            List<SalvoDO> salvoDOS = updateSalvoList(gameDO.getPlayers().get(playerIndex), salvoList);

            PlayerDO attackerPlayerDO = gameDO.getPlayers().get(playerIndex);
            attackerPlayerDO.setSalvoDOs(salvoDOS);

            PlayerDO attackedPlayerDO = playerIndex == 0 ? gameDO.getPlayers().get(1) : gameDO.getPlayers().get(0);

            gameDO.setPlayerTurn(attackedPlayerDO.getUserID());
            gameDO.setPlayerWon(isPlayerWin(attackedPlayerDO) ? attackerPlayerDO.getUserID() : null);
            gameDO = gameRepository.save(gameDO);

            return SalvoMapper.createSalvoResponseDTO(salvoList, gameDO.getPlayerTurn());
        }

        throw new TooMuchShotException("Number of shots can not be more than spaceships you have!");

    }

    private boolean isPlayerWin(PlayerDO attackedPlayerDO) {
        List<Spaceship> spaceships = attackedPlayerDO.getSpaceShips();
        return spaceships.stream().filter(s -> s.getLife() == 0).count() > 0 ? true : false;

    }

    private List<SalvoDO> updateSalvoList(PlayerDO playerDO, List<SalvoDO> salvoList) {
        List<SalvoDO> salvoDOS = playerDO.getSalvoDOs();
        if (salvoDOS != null) {
            salvoDOS.addAll(salvoList);
        } else {
            salvoDOS = salvoList;
        }

        return salvoDOS;
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
    public boolean isShotNumberOk(int shotRightOfPlayer, int numberOfShots) throws TooMuchShotException {

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
     * @param playerDO   attacker   player userID
     * @return salvo which contains list of shots
     */
    private List<SalvoDO> createSalvoList
    (List<Coordinate> taken, List<Coordinate> free, List<String> shotsInHex, PlayerDO playerDO) {
        List<Coordinate> shots = parseFiredCoordinates(shotsInHex);
        List<SalvoDO> salvoDOS = new ArrayList<>();

        for (Coordinate coordinate : shots) {
            SalvoDO salvoDO = createSalvo(taken, free, coordinate, playerDO);
            salvoDOS.add(salvoDO);
        }
        return salvoDOS;
    }

    private SalvoDO createSalvo(List<Coordinate> taken, List<Coordinate> free, Coordinate
            coordinate, PlayerDO playerDO) {
        SalvoDO salvoDO;
        if (taken.contains(coordinate)) {
            salvoDO = shotHitATakenPlace(taken, coordinate, playerDO);
        } else {
            salvoDO = shotHitFreePlace(free, coordinate, playerDO);
        }
        salvoDO.setPlayerDO(playerDO);

        return salvoDO;
    }

    private SalvoDO shotHitFreePlace(List<Coordinate> free, Coordinate coordinate, PlayerDO playerDO) {
        SalvoDO salvoDO;
        int indexInList = indexInList(free, coordinate);
        updateListField(free, indexInList, MISSED_QUADRANT);
        salvoDO = new SalvoDO(free.get(indexInList), SalvoTypeEnum.MISS);
        salvoDO.setCoordinate(free.get(indexInList));
        free.get(indexInList).setSalvoDO(salvoDO);

        return salvoDO;
    }

    private SalvoDO shotHitATakenPlace(List<Coordinate> taken, Coordinate coordinate, PlayerDO playerDO) {
        SalvoDO salvoDO;
        int indexInList = indexInList(taken, coordinate);
        if (!isOwner(taken, indexInList, coordinate, playerDO.getUserID())) {
            salvoDO = checkSalvoType(taken.get(indexInList));
            updateListField(taken, indexInList, HIT_QUADRANT);

        } else {
            salvoDO = new SalvoDO(taken.get(indexInList), SalvoTypeEnum.MISS);
            salvoDO.setHitOwnSpaceship(true);
        }
        salvoDO.setCoordinate(taken.get(indexInList));
        taken.get(indexInList).setSalvoDO(salvoDO);

        return salvoDO;
    }

    private void updateListField(List<Coordinate> takenFreeFieldList, int index, String content) {
        takenFreeFieldList.get(index).setContent(content);
    }

    /**
     * checks whether attacker is the same person who has the spaceship on the coordinate of the shot
     *
     * @param taken fields
     * @return index of shot's coordinate
     */
    private int indexInList(List<Coordinate> taken, Coordinate coordinate) {
        for (int i = 0; i < taken.size(); i++) {
            if (taken.get(i).equals(coordinate)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * checks whether attacker is the same person who has the spaceship on the coordinate of the shot
     *
     * @param taken        fields
     * @param indexInTaken to find correct coordinate in list
     * @param coordinate   of a shot
     * @param attacker     - userID of attacker player
     * @return true if attacker is the same person who does the shot
     */
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
    private SalvoDO checkSalvoType(Coordinate coordinate) {
        SalvoDO salvoDO = new SalvoDO(coordinate, SalvoTypeEnum.MISS);
        if (coordinate.getContent().equals(NOT_HIT_QUADRANT)) {
            Spaceship spaceship = coordinate.getSpaceshipDO();
            spaceship.setLife(spaceship.getLife() - 1);
            if (spaceship.getLife() == 0) {
                salvoDO = new SalvoDO(coordinate, SalvoTypeEnum.KILL);
                decreaseOtherPlayerShotRight(coordinate);
                return salvoDO;
            }
            salvoDO.setSalvoType(SalvoTypeEnum.HIT);
        }
        return salvoDO;
    }

    /**
     * decreases shot right of other player if attacker player kill a spaceship of other player
     *
     * @param coordinate of shot
     */
    private void decreaseOtherPlayerShotRight(Coordinate coordinate) {
        PlayerDO playerDO = coordinate.getSpaceshipDO().getPlayerDO();
        playerDO.setShotRight(playerDO.getShotRight() - 1);
        coordinate.getSpaceshipDO().setPlayerDO(playerDO);
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

        String playerTurn = pickStartingPlayer(gameDO.getPlayers());
        gameDO.setStarting(playerTurn);
        gameDO.setPlayerTurn(playerTurn);
        return gameDO;

    }
}



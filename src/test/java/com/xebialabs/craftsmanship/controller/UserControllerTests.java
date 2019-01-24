package com.xebialabs.craftsmanship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebialabs.craftsmanship.dataTransferObjects.request.CreateGameRequestDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.response.GetGameResponseDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.response.PlayerDTO;
import com.xebialabs.craftsmanship.domainObject.GameDO;
import com.xebialabs.craftsmanship.domainObject.PlayerDO;
import com.xebialabs.craftsmanship.service.game.IGameService;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.xebialabs.craftsmanship.controller.Util.getCreateGameRequestDTO;
import static com.xebialabs.craftsmanship.helper.ConstantValues.GET_EXTENSION;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTests {

    private MockMvc mockMvc;

    @MockBean
    private IGameService iGameService;

    @Autowired
    WebApplicationContext wac;


    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        System.out.println("SETUUUUUUUUUUUUUUUUUUUUP");

        CreateGameRequestDTO createGameRequestDTO = getCreateGameRequestDTO();
        PlayerDO self = new PlayerDO("player", "Assessment PlayerDO");
        PlayerDO opponent = new PlayerDO(createGameRequestDTO.getUserId(), createGameRequestDTO.getFullName());

        GameDO gameDo = new GameDO("match-01", self,opponent, createGameRequestDTO.getSpaceshipProtocolDO());
        Mockito.when(iGameService.saveGameInstance("match-01", self,opponent, createGameRequestDTO.getSpaceshipProtocolDO())).thenReturn(gameDo);
        //GameDO gameDO=iGameService.saveGameInstance("match-01", self,opponent, createGameRequestDTO.getSpaceshipProtocolDO());
        System.out.println(gameDo.getGameID()+"  "+gameDo.getStarting());
    }

     @Test
    public void getGame() throws Exception {

        String gameID="match-01", userID="player";
         CreateGameRequestDTO createGameRequestDTO = getCreateGameRequestDTO();


        Mockito.when(iGameService.getGame(gameID,userID)).thenReturn(this.getMockGameResponseDTO());

        PlayerDTO playerDTO = new PlayerDTO("player-1","");

        //response is retrieved as MvcResult
        MvcResult mvcResult = mockMvc.perform(get(GET_EXTENSION.concat(gameID+"?userID="+userID))
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.self", CoreMatchers.any(PlayerDTO.class)))
                .andExpect(jsonPath("$.opponent", CoreMatchers.any(PlayerDTO.class)))
                .andReturn();

        //json response body is converted/mapped to the Java Object
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        GetGameResponseDTO gameResponse = new ObjectMapper().readValue(jsonResponse, GetGameResponseDTO.class);
        assertNotNull(gameResponse);

    }

    public GetGameResponseDTO getMockGameResponseDTO(){

        String board="";
        PlayerDTO self=new PlayerDTO("player-1", board);
        PlayerDTO opponent=new PlayerDTO("xebialabs-1", board);
        GetGameResponseDTO getGameResponseDTO=new GetGameResponseDTO(self,opponent);
        return getGameResponseDTO;
    }
}

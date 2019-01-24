package com.xebialabs.craftsmanship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebialabs.craftsmanship.dataTransferObjects.request.CreateGameRequestDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.response.CreateGameResponseDTO;
import com.xebialabs.craftsmanship.helper.ConstantValues;
import com.xebialabs.craftsmanship.service.game.IGameService;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
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
import static com.xebialabs.craftsmanship.helper.ConstantValues.POST_EXTENSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProtocolController.class)
public class ProtocolControllerTests {

    private MockMvc mockMvc;

    @MockBean
    private IGameService iGameService;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;


    @Captor
    private ArgumentCaptor<CreateGameRequestDTO> createGameRequestDTOArgumentCaptor;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

    }

    @Test
    public void createGame() throws Exception {

        CreateGameRequestDTO createGameRequestDTO = getCreateGameRequestDTO();
        CreateGameResponseDTO createGameResponseDTO = new CreateGameResponseDTO("xebialabs-1", "XebiaLabs Opponent", "match-1", "xebialabs-1");

        Mockito.when(iGameService.createGame(ArgumentMatchers.any(CreateGameRequestDTO.class))).thenReturn(createGameResponseDTO);

        //response is retrieved as MvcResult
        MvcResult mvcResult = mockMvc.perform(post(POST_EXTENSION)
                .content(objectMapper.writeValueAsString(createGameRequestDTO))
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user_id", CoreMatchers.is(createGameResponseDTO.getUserId())))
                .andExpect(jsonPath("$.full_name", CoreMatchers.is(createGameResponseDTO.getFullName())))
                .andExpect(jsonPath("$.game_id", CoreMatchers.startsWith(ConstantValues.GAME_ID)))
                .andExpect(jsonPath("$.starting", CoreMatchers.anyOf(CoreMatchers.is("xebialabs-1"), CoreMatchers.is("player"))))
                .andReturn();


        //json response body is converted/mapped to the Java Object
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CreateGameResponseDTO gameCreated = new ObjectMapper().readValue(jsonResponse, CreateGameResponseDTO.class);

        assertNotNull(gameCreated);
        assertEquals(gameCreated.getUserId(), createGameResponseDTO.getUserId());
        assertEquals(gameCreated.getFullName(), createGameResponseDTO.getFullName());
        assertEquals(gameCreated.getGameID(), createGameResponseDTO.getGameID());
        assertEquals(gameCreated.getStarting(), createGameResponseDTO.getStarting());

    }

}
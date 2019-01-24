package com.xebialabs.craftsmanship.dataTransferObjects.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class GetGameResponseDTO implements Serializable {

    @JsonProperty("self")
    @NotNull(message = "user_id can't be null")
    private PlayerDTO self;

    @JsonProperty("opponent")
    @NotNull(message = "user_id can't be null")
    private PlayerDTO opponent;

    public GetGameResponseDTO(PlayerDTO self, PlayerDTO opponent){
        this.self=self;
        this.opponent=opponent;
    }

    public static GetGameDTOBuilder newBuilder()
    {
        return new GetGameDTOBuilder();
    }


    public static class GetGameDTOBuilder{

        private PlayerDTO selfDTO;

        private PlayerDTO opponentDTO;

        public GetGameDTOBuilder setSelfPlayer(PlayerDTO selfDTO)
        {
            this.selfDTO=selfDTO;
            return this;
        }

        public GetGameDTOBuilder setOpponentPlayer(PlayerDTO opponentDTO)
        {
            this.opponentDTO=opponentDTO;
            return this;
        }

        public GetGameResponseDTO createGetGameDTOBuilder(){
            return new GetGameResponseDTO(selfDTO, opponentDTO);
        }
    }
}

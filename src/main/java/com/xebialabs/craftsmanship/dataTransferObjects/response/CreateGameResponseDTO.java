package com.xebialabs.craftsmanship.dataTransferObjects.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CreateGameResponseDTO implements Serializable {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("game_id")
    private String gameID;

    @JsonProperty("starting")
    private String starting;

    public CreateGameResponseDTO(){}

    public CreateGameResponseDTO(String userId, String fullName, String gameID, String starting) {
        this.userId = userId;
        this.fullName = fullName;
        this.gameID = gameID;
        this.starting = starting;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getStarting() {
        return starting;
    }

    public void setStarting(String starting) {
        this.starting = starting;
    }
}

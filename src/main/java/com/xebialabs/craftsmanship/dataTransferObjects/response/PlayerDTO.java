package com.xebialabs.craftsmanship.dataTransferObjects.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class PlayerDTO {

    @JsonProperty("user_id")
    @NotNull(message = "user_id can't be null")
    private String userID;

    @JsonProperty("board")
    private String board;

    public PlayerDTO(String userID, String board) {
        this.userID = userID;
        this.board = board;

    }

    public static PlayerDTOBuilder newBuilder() {
        return new PlayerDTOBuilder();
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public static class PlayerDTOBuilder {

        private String userID;

        private String board;

        public PlayerDTOBuilder setUserID(String userID) {
            this.userID = userID;
            return this;
        }

        public PlayerDTOBuilder setBoard(String board) {
            this.board = board;
            return this;
        }

        public PlayerDTO createPlayerDTOBuilder() {
            return new PlayerDTO(userID, board);
        }
    }
}

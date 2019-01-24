package com.xebialabs.craftsmanship.dataTransferObjects.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xebialabs.craftsmanship.domainObject.SpaceshipProtocolDO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class CreateGameRequestDTO implements Serializable {

    @JsonProperty("user_id")
    @NotNull(message = "user_id can't be null")
    private String userId;

    @JsonProperty("full_name")
    @NotNull(message = "full_name can't be null")
    private String fullName;

    @JsonProperty("spaceship_protocol")
    @NotNull
    @Valid
    private SpaceshipProtocolDO spaceshipProtocolDO;

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

    public SpaceshipProtocolDO getSpaceshipProtocolDO() {
        return spaceshipProtocolDO;
    }

    public void setSpaceshipProtocolDO(SpaceshipProtocolDO spaceshipProtocolDO) {
        this.spaceshipProtocolDO = spaceshipProtocolDO;
    }
}

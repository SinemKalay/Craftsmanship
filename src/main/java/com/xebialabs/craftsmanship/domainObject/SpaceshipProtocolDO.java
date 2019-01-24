package com.xebialabs.craftsmanship.domainObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "spaceship_protocol")
public class SpaceshipProtocolDO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    @JsonIgnore
    private Long id;

    @JsonProperty("hostname")
    @NotNull(message = "hostname can't be null")
    private String hostname;

    @JsonProperty("port")
    @NotNull(message = "port can't be null")
    private int port;

    @OneToOne( mappedBy = "gridDO")
    @JsonIgnore
    private GameDO gameDO;

    public SpaceshipProtocolDO() {}

    public SpaceshipProtocolDO(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;

    }

    public SpaceshipProtocolDO(Long id,String hostname, int port) {
        this.id=id;
        this.hostname = hostname;
        this.port = port;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

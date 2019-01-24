package com.xebialabs.craftsmanship.dataTransferObjects.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SalvoRequestDTO {


    @JsonProperty("salvo")
    private List<String> salvo;

    public List<String> getSalvo() {
        return salvo;
    }

    public void setSalvo(List<String> salvo) {
        this.salvo = salvo;
    }

}

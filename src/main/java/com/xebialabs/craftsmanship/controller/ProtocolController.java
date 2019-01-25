package com.xebialabs.craftsmanship.controller;

import com.xebialabs.craftsmanship.dataTransferObjects.request.CreateGameRequestDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.request.SalvoRequestDTO;
import com.xebialabs.craftsmanship.helper.exception.EntityNotFoundException;
import com.xebialabs.craftsmanship.helper.exception.NotYourTurnException;
import com.xebialabs.craftsmanship.helper.exception.TooMuchShotException;
import com.xebialabs.craftsmanship.helper.exception.UniqueConstraintException;
import com.xebialabs.craftsmanship.service.game.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("xl-spaceship/protocol")
@SessionAttributes("protocol")
public class ProtocolController {


    @Autowired
    private  IGameService iGameService;

    @PostMapping("/game/new")
    public ResponseEntity<Object> createGame(@Valid @RequestBody CreateGameRequestDTO createGameRequestDTO) throws UniqueConstraintException {
        return new ResponseEntity<>(iGameService.createGame(createGameRequestDTO),HttpStatus.CREATED);
    }

    @PutMapping("/game/{gameID}")
    public ResponseEntity<Object> fireByOpponent( @PathVariable String gameID, @Valid @RequestBody SalvoRequestDTO salvoRequestDTO) throws EntityNotFoundException, TooMuchShotException, NotYourTurnException {
        return new ResponseEntity<>(iGameService.fire(gameID, salvoRequestDTO,false),HttpStatus.OK);
    }

}

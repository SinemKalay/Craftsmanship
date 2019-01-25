package com.xebialabs.craftsmanship.controller;

import com.xebialabs.craftsmanship.dataTransferObjects.request.SalvoRequestDTO;
import com.xebialabs.craftsmanship.helper.exception.*;
import com.xebialabs.craftsmanship.service.game.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("xl-spaceship/user")
@SessionAttributes("user")
public class UserController {

    @Autowired
    private IGameService iGameService;

    @GetMapping("/game/{gameID}")
    public ResponseEntity<Object> getGame(@PathVariable String gameID, @RequestParam String userID) throws NotFoundUserException, EntityNotFoundException {
        return new ResponseEntity<>(iGameService.getGame(gameID, userID), HttpStatus.OK);
    }

    @PutMapping("/game/{gameID}/fire")
    public ResponseEntity<Object> fireToOpponent(@PathVariable String gameID, @Valid @RequestBody SalvoRequestDTO salvoRequestDTO) throws EntityNotFoundException, TooMuchShotException, NotYourTurnException, GameOverException {
        return new ResponseEntity<>(iGameService.fire(gameID, salvoRequestDTO, true), HttpStatus.OK);
    }
}

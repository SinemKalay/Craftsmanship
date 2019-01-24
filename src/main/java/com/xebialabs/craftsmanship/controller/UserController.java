package com.xebialabs.craftsmanship.controller;

import com.xebialabs.craftsmanship.dataTransferObjects.request.SalvoRequestDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.response.GetGameResponseDTO;
import com.xebialabs.craftsmanship.dataTransferObjects.response.SalvoResponseDTO;
import com.xebialabs.craftsmanship.helper.exception.EntityNotFoundException;
import com.xebialabs.craftsmanship.helper.exception.TooMuchShotException;
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
    public ResponseEntity<GetGameResponseDTO> getGame(@PathVariable String gameID, @RequestParam String userID) throws EntityNotFoundException
    {
        return new ResponseEntity<>(iGameService.getGame(gameID,userID), HttpStatus.OK);
    }

    @PutMapping( "/game/{gameID}/fire")
    public ResponseEntity<SalvoResponseDTO> fireToOpponent( @PathVariable String gameID, @Valid @RequestBody SalvoRequestDTO salvoRequestDTO) throws EntityNotFoundException, TooMuchShotException {
        return new ResponseEntity<>(iGameService.fire(gameID, salvoRequestDTO,true),HttpStatus.OK);
    }
}

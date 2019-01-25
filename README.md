# Craftsmanship

## Description
   Craftsmanship is an application that runs on JVM. So there is no need further set up for run it.
   Basically, it provides apis to players for let them playing each other. Each player has 5 spaceship
   and at the beginning of the game coordinates of all spaceships are assigned. Then player can start to 
   sent salvos their opponent's. Player who can hit all points of opponent's spaceships will be won at
   the end of the game.   

## Overall Approach
	The most important part of this solution is how relations between entitties were set up at the beginning.
      
    We can think craftmanship Game is roof of the all relations. Under that hightlight;
      
    - Each Game has gameID, starting player, player turn, winner player(if exists) fields.
    - A game  has to have a Grid (one-to-one relation with Grid entity)
    - A game needs 2 players (one-to-many relation with Player entity)
    - And there is a spaceship protocol object in each game (one-to-one relation with SpaceshipProtocol entity) 
      
    - Each Player has userID, full name, number of shot right fields.
	- Each player has 5 spaceships at the beginning which contains one of each type of Spaceships (one-to-many relation with Spaceship entity)
	- Each player has list of Salvo (one-to-many relation with Salvo entity)
	
	- Each Salvo has a Coordinate (one-to-one relation with Coordinate) plus relational object with other entities		
      
    - Each Grid contains list of free coordinates, list of taken coordinates by spaceships, 16*16 board fields 
	- Each Grid has 10 spaceship on it (one-to-many relation with Spaceship entity)
  
	- Each spaceship extends Spaceship abstract class. Types: a class, b class, s class,angle, winger 
	- Each spaceship contains list of coordinates and also it equals to spaceship
	life (one-to-many relation with Coordinate entity)
	
  After this relation set up, the only thing it matters is rules of the game. According to rules corner cases
    should be covered up,too. 
    
 ## Functionalities
    - Players can create a new game as much as they can. After the game created, player starts to shot each other 
    - Self player can shot to opponent player as well as opponent player can shot to self player. Each salvo can have multiple shots.
    - If shot hit to part of opponent's spaceship, the spaceship will lose life. After a spaceship losing of the
    last life, it will be destroyed. 
    - If spaceships of one of player, he/she will be lose the game.
	
	- Player can view the status of the game by UI. the view containes all coordinates that player spaceships and known places about opponent's user. Known places means the coordinates of every salvo taht player did. That way player partially knows what is on those points.	
    
 ### Rules
    - ** Creating Game: ** While create a game, player information(userId, full name) needs to be different, otherwise it will throw an exception about trying againg with different information.
	
	- ** View the status of the game: **  Player can not see all points of opponent's grid. Salvo by salvo player will be known about the opponent's grid.
	
	- ** Fired by/ Fire To Opponent: **  there are different criterias while make a this functionality. 
		First rule of all, whether game over or not should be checked. If game is over, neither of players can not sent a salvo. 
		
		Second rule, player turn will be observed, if it is not turn of the player who
		sent salvo, game will give a warning about waiting for opponent's move.  
	
		Third rule,  player can sent a salvo which has a number of shots equals to number of living spaceships that player has.
		
		Fourth rule, if shot hit to a point which hit before, the shot counted as MISS shot. 
		
		Fifth rule, if shot hits the last point of a spaceship, it will counted as KILL shot and the spaceship will be destroyed.

 ### Custom exceptions
 
	- GameOverException
	- TooMuchShotException
	- NotYourTurnException
	- UniqueConstraintException
	- NotFoundUserException
	- EntityNotFoundException
  		
 ### Additional Assumptions
    ** If shot hit to part of own spaceship, salvo type counted as MISS shot. **
    ** For viewing the status of the game,  userID also needed in get request for responsing more sensible way. **
 

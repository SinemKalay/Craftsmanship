package com.xebialabs.craftsmanship.dataAccessObject;

import com.xebialabs.craftsmanship.domainObject.GameDO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends CrudRepository<GameDO,Long> {

    Optional<GameDO> findByGameID(String gameID);
    GameDO save(GameDO gameDO);

}

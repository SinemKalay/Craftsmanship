package com.xebialabs.craftsmanship.dataAccessObject;

import com.xebialabs.craftsmanship.domainObject.PlayerDO;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PlayerRepository extends CrudRepository<PlayerDO,Long> {

    @Override
    Optional<PlayerDO> findById(Long aLong);

    Optional<PlayerDO> findByUserIDAndFullName(String userID, String fullName);
}

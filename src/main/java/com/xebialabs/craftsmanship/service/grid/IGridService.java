package com.xebialabs.craftsmanship.service.grid;

import com.xebialabs.craftsmanship.domainObject.GridDO;
import com.xebialabs.craftsmanship.domainObject.PlayerDO;

public interface IGridService {

    GridDO putSpaceshipsOnBoard(PlayerDO self, PlayerDO opponent);

}

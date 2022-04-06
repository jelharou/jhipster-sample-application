import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu" data-cy="entity" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/account-ra">
      Account RA
    </MenuItem>
    <MenuItem icon="asterisk" to="/account-ref">
      Account Ref
    </MenuItem>
    <MenuItem icon="asterisk" to="/balance">
      Balance
    </MenuItem>
    <MenuItem icon="asterisk" to="/beneficiary">
      Beneficiary
    </MenuItem>
    <MenuItem icon="asterisk" to="/party">
      Party
    </MenuItem>
    <MenuItem icon="asterisk" to="/party-id">
      Party Id
    </MenuItem>
    <MenuItem icon="asterisk" to="/transaction">
      Transaction
    </MenuItem>
    <MenuItem icon="asterisk" to="/address">
      Address
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);

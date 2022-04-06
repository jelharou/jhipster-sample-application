import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AccountRA from './account-ra';
import AccountRef from './account-ref';
import Balance from './balance';
import Beneficiary from './beneficiary';
import Party from './party';
import PartyId from './party-id';
import Transaction from './transaction';
import Address from './address';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}account-ra`} component={AccountRA} />
      <ErrorBoundaryRoute path={`${match.url}account-ref`} component={AccountRef} />
      <ErrorBoundaryRoute path={`${match.url}balance`} component={Balance} />
      <ErrorBoundaryRoute path={`${match.url}beneficiary`} component={Beneficiary} />
      <ErrorBoundaryRoute path={`${match.url}party`} component={Party} />
      <ErrorBoundaryRoute path={`${match.url}party-id`} component={PartyId} />
      <ErrorBoundaryRoute path={`${match.url}transaction`} component={Transaction} />
      <ErrorBoundaryRoute path={`${match.url}address`} component={Address} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;

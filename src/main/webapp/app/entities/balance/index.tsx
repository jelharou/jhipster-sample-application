import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Balance from './balance';
import BalanceDetail from './balance-detail';
import BalanceUpdate from './balance-update';
import BalanceDeleteDialog from './balance-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BalanceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BalanceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BalanceDetail} />
      <ErrorBoundaryRoute path={match.url} component={Balance} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BalanceDeleteDialog} />
  </>
);

export default Routes;

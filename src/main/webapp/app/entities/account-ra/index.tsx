import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AccountRA from './account-ra';
import AccountRADetail from './account-ra-detail';
import AccountRAUpdate from './account-ra-update';
import AccountRADeleteDialog from './account-ra-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AccountRAUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AccountRAUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AccountRADetail} />
      <ErrorBoundaryRoute path={match.url} component={AccountRA} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AccountRADeleteDialog} />
  </>
);

export default Routes;

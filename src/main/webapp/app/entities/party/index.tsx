import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Party from './party';
import PartyDetail from './party-detail';
import PartyUpdate from './party-update';
import PartyDeleteDialog from './party-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PartyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PartyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PartyDetail} />
      <ErrorBoundaryRoute path={match.url} component={Party} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PartyDeleteDialog} />
  </>
);

export default Routes;

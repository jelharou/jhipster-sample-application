import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PartyId from './party-id';
import PartyIdDetail from './party-id-detail';
import PartyIdUpdate from './party-id-update';
import PartyIdDeleteDialog from './party-id-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PartyIdUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PartyIdUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PartyIdDetail} />
      <ErrorBoundaryRoute path={match.url} component={PartyId} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PartyIdDeleteDialog} />
  </>
);

export default Routes;

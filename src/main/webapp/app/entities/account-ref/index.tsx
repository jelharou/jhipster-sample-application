import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AccountRef from './account-ref';
import AccountRefDetail from './account-ref-detail';
import AccountRefUpdate from './account-ref-update';
import AccountRefDeleteDialog from './account-ref-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AccountRefUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AccountRefUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AccountRefDetail} />
      <ErrorBoundaryRoute path={match.url} component={AccountRef} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AccountRefDeleteDialog} />
  </>
);

export default Routes;

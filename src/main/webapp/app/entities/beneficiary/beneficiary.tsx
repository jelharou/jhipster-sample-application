import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './beneficiary.reducer';
import { IBeneficiary } from 'app/shared/model/beneficiary.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Beneficiary = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const beneficiaryList = useAppSelector(state => state.beneficiary.entities);
  const loading = useAppSelector(state => state.beneficiary.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="beneficiary-heading" data-cy="BeneficiaryHeading">
        Beneficiaries
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Beneficiary
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {beneficiaryList && beneficiaryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Account Ref</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {beneficiaryList.map((beneficiary, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${beneficiary.id}`} color="link" size="sm">
                      {beneficiary.id}
                    </Button>
                  </td>
                  <td>
                    {beneficiary.accountRef ? <Link to={`account-ref/${beneficiary.accountRef.id}`}>{beneficiary.accountRef.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${beneficiary.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${beneficiary.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${beneficiary.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Beneficiaries found</div>
        )}
      </div>
    </div>
  );
};

export default Beneficiary;

import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IAccountRef } from 'app/shared/model/account-ref.model';
import { getEntities as getAccountRefs } from 'app/entities/account-ref/account-ref.reducer';
import { IParty } from 'app/shared/model/party.model';
import { getEntities as getParties } from 'app/entities/party/party.reducer';
import { getEntity, updateEntity, createEntity, reset } from './party-id.reducer';
import { IPartyId } from 'app/shared/model/party-id.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PartyIdUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const accountRefs = useAppSelector(state => state.accountRef.entities);
  const parties = useAppSelector(state => state.party.entities);
  const partyIdEntity = useAppSelector(state => state.partyId.entity);
  const loading = useAppSelector(state => state.partyId.loading);
  const updating = useAppSelector(state => state.partyId.updating);
  const updateSuccess = useAppSelector(state => state.partyId.updateSuccess);
  const handleClose = () => {
    props.history.push('/party-id');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getAccountRefs({}));
    dispatch(getParties({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...partyIdEntity,
      ...values,
      accountRef: accountRefs.find(it => it.id.toString() === values.accountRef.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...partyIdEntity,
          accountRef: partyIdEntity?.accountRef?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.partyId.home.createOrEditLabel" data-cy="PartyIdCreateUpdateHeading">
            Create or edit a PartyId
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="party-id-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField id="party-id-accountRef" name="accountRef" data-cy="accountRef" label="Account Ref" type="select">
                <option value="" key="0" />
                {accountRefs
                  ? accountRefs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/party-id" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PartyIdUpdate;

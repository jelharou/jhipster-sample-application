import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IAccountRef } from 'app/shared/model/account-ref.model';
import { getEntities as getAccountRefs } from 'app/entities/account-ref/account-ref.reducer';
import { getEntity, updateEntity, createEntity, reset } from './account-ra.reducer';
import { IAccountRA } from 'app/shared/model/account-ra.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AccountRAUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const accountRefs = useAppSelector(state => state.accountRef.entities);
  const accountRAEntity = useAppSelector(state => state.accountRA.entity);
  const loading = useAppSelector(state => state.accountRA.loading);
  const updating = useAppSelector(state => state.accountRA.updating);
  const updateSuccess = useAppSelector(state => state.accountRA.updateSuccess);
  const handleClose = () => {
    props.history.push('/account-ra');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getAccountRefs({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...accountRAEntity,
      ...values,
      ref: accountRefs.find(it => it.id.toString() === values.ref.toString()),
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
          ...accountRAEntity,
          ref: accountRAEntity?.ref?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.accountRA.home.createOrEditLabel" data-cy="AccountRACreateUpdateHeading">
            Create or edit a AccountRA
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="account-ra-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField id="account-ra-ref" name="ref" data-cy="ref" label="Ref" type="select">
                <option value="" key="0" />
                {accountRefs
                  ? accountRefs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/account-ra" replace color="info">
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

export default AccountRAUpdate;

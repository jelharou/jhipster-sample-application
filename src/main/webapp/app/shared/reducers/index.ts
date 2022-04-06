import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication from './authentication';
import applicationProfile from './application-profile';

import administration from 'app/modules/administration/administration.reducer';
import userManagement from 'app/modules/administration/user-management/user-management.reducer';
import register from 'app/modules/account/register/register.reducer';
import activate from 'app/modules/account/activate/activate.reducer';
import password from 'app/modules/account/password/password.reducer';
import settings from 'app/modules/account/settings/settings.reducer';
import passwordReset from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import accountRA from 'app/entities/account-ra/account-ra.reducer';
// prettier-ignore
import accountRef from 'app/entities/account-ref/account-ref.reducer';
// prettier-ignore
import balance from 'app/entities/balance/balance.reducer';
// prettier-ignore
import beneficiary from 'app/entities/beneficiary/beneficiary.reducer';
// prettier-ignore
import party from 'app/entities/party/party.reducer';
// prettier-ignore
import partyId from 'app/entities/party-id/party-id.reducer';
// prettier-ignore
import transaction from 'app/entities/transaction/transaction.reducer';
// prettier-ignore
import address from 'app/entities/address/address.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  accountRA,
  accountRef,
  balance,
  beneficiary,
  party,
  partyId,
  transaction,
  address,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;

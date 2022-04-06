import { IAccountRA } from 'app/shared/model/account-ra.model';
import { IBeneficiary } from 'app/shared/model/beneficiary.model';
import { IBalance } from 'app/shared/model/balance.model';
import { ITransaction } from 'app/shared/model/transaction.model';
import { IPartyId } from 'app/shared/model/party-id.model';

export interface IAccountRef {
  id?: number;
  accountRA?: IAccountRA | null;
  beneficiaries?: IBeneficiary[] | null;
  balances?: IBalance[] | null;
  transactions?: ITransaction[] | null;
  partyIds?: IPartyId[] | null;
}

export const defaultValue: Readonly<IAccountRef> = {};

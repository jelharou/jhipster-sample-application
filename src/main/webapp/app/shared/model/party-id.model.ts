import { IAccountRef } from 'app/shared/model/account-ref.model';
import { IParty } from 'app/shared/model/party.model';
import { IAddress } from 'app/shared/model/address.model';

export interface IPartyId {
  id?: number;
  accountRef?: IAccountRef | null;
  party?: IParty | null;
  addresses?: IAddress[] | null;
}

export const defaultValue: Readonly<IPartyId> = {};

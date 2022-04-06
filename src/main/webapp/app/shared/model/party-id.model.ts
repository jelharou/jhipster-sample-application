import { IAccountRef } from 'app/shared/model/account-ref.model';
import { IAddress } from 'app/shared/model/address.model';

export interface IPartyId {
  id?: number;
  accountRef?: IAccountRef | null;
  addresses?: IAddress[] | null;
}

export const defaultValue: Readonly<IPartyId> = {};

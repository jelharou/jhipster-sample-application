import { IAccountRef } from 'app/shared/model/account-ref.model';

export interface IBeneficiary {
  id?: number;
  accountRef?: IAccountRef | null;
}

export const defaultValue: Readonly<IBeneficiary> = {};

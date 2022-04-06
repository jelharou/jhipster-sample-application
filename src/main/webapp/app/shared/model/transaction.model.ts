import { IAccountRef } from 'app/shared/model/account-ref.model';

export interface ITransaction {
  id?: number;
  accountRef?: IAccountRef | null;
}

export const defaultValue: Readonly<ITransaction> = {};

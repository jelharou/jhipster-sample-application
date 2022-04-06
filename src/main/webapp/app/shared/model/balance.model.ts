import { IAccountRef } from 'app/shared/model/account-ref.model';

export interface IBalance {
  id?: number;
  accountRef?: IAccountRef | null;
}

export const defaultValue: Readonly<IBalance> = {};

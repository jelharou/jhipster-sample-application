import { IAccountRef } from 'app/shared/model/account-ref.model';

export interface IAccountRA {
  id?: number;
  accountRef?: IAccountRef | null;
}

export const defaultValue: Readonly<IAccountRA> = {};

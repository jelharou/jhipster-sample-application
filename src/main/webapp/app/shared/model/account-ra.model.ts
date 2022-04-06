import { IAccountRef } from 'app/shared/model/account-ref.model';

export interface IAccountRA {
  id?: number;
  ref?: IAccountRef | null;
}

export const defaultValue: Readonly<IAccountRA> = {};

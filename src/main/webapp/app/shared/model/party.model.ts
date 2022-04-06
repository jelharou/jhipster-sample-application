import { IPartyId } from 'app/shared/model/party-id.model';

export interface IParty {
  id?: number;
  id?: IPartyId | null;
}

export const defaultValue: Readonly<IParty> = {};

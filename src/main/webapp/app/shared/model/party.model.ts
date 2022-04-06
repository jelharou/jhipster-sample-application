import { IPartyId } from 'app/shared/model/party-id.model';

export interface IParty {
  id?: number;
  partyId?: IPartyId | null;
}

export const defaultValue: Readonly<IParty> = {};

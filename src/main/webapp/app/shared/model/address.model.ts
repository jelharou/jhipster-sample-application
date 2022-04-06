import { IPartyId } from 'app/shared/model/party-id.model';

export interface IAddress {
  id?: number;
  partyId?: IPartyId | null;
}

export const defaultValue: Readonly<IAddress> = {};

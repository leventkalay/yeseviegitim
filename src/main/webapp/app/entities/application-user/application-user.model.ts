import { IUser } from 'app/entities/user/user.model';
import { IEgitim } from 'app/entities/egitim/egitim.model';

export interface IApplicationUser {
  id?: number;
  birimi?: string | null;
  internalUser?: IUser | null;
  egitims?: IEgitim[] | null;
}

export class ApplicationUser implements IApplicationUser {
  constructor(public id?: number, public birimi?: string | null, public internalUser?: IUser | null, public egitims?: IEgitim[] | null) {}
}

export function getApplicationUserIdentifier(applicationUser: IApplicationUser): number | undefined {
  return applicationUser.id;
}

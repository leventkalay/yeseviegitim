import { IEgitim } from 'app/entities/egitim/egitim.model';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';

export interface IEgitimDersler {
  id?: number;
  egitim?: IEgitim | null;
  kullanici?: IApplicationUser | null;
}

export class EgitimDersler implements IEgitimDersler {
  constructor(public id?: number, public egitim?: IEgitim | null, public kullanici?: IApplicationUser | null) {}
}

export function getEgitimDerslerIdentifier(egitimDersler: IEgitimDersler): number | undefined {
  return egitimDersler.id;
}

import { IEgitim } from 'app/entities/egitim/egitim.model';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';

export interface IOgrenciEgitimler {
  id?: number;
  egitim?: IEgitim | null;
  kullanici?: IApplicationUser | null;
}

export class OgrenciEgitimler implements IOgrenciEgitimler {
  constructor(public id?: number, public egitim?: IEgitim | null, public kullanici?: IApplicationUser | null) {}
}

export function getOgrenciEgitimlerIdentifier(ogrenciEgitimler: IOgrenciEgitimler): number | undefined {
  return ogrenciEgitimler.id;
}

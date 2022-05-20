import * as dayjs from 'dayjs';
import { IDers } from 'app/entities/ders/ders.model';
import { IKurum } from 'app/entities/kurum/kurum.model';
import { IEgitimTuru } from 'app/entities/egitim-turu/egitim-turu.model';
import { IEgitmen } from 'app/entities/egitmen/egitmen.model';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';

export interface IEgitim {
  id?: number;
  egitimBaslik?: string | null;
  egitimAltBaslik?: string | null;
  egitimBaslamaTarihi?: dayjs.Dayjs | null;
  egitimBitisTarihi?: dayjs.Dayjs | null;
  dersSayisi?: number | null;
  egitimSuresi?: number | null;
  egitimYeri?: string | null;
  egitimPuani?: number | null;
  aktif?: boolean | null;
  ders?: IDers[] | null;
  kurum?: IKurum | null;
  egitimTuru?: IEgitimTuru | null;
  egitmen?: IEgitmen | null;
  applicationUser?: IApplicationUser | null;
}

export class Egitim implements IEgitim {
  constructor(
    public id?: number,
    public egitimBaslik?: string | null,
    public egitimAltBaslik?: string | null,
    public egitimBaslamaTarihi?: dayjs.Dayjs | null,
    public egitimBitisTarihi?: dayjs.Dayjs | null,
    public dersSayisi?: number | null,
    public egitimSuresi?: number | null,
    public egitimYeri?: string | null,
    public egitimPuani?: number | null,
    public aktif?: boolean | null,
    public ders?: IDers[] | null,
    public kurum?: IKurum | null,
    public egitimTuru?: IEgitimTuru | null,
    public egitmen?: IEgitmen | null,
    public applicationUser?: IApplicationUser | null
  ) {
    this.aktif = this.aktif ?? false;
  }
}

export function getEgitimIdentifier(egitim: IEgitim): number | undefined {
  return egitim.id;
}

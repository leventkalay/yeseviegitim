import * as dayjs from 'dayjs';
import { IEgitim } from 'app/entities/egitim/egitim.model';

export interface IDuyuru {
  id?: number;
  duyuruBaslik?: string;
  duyuruIcerik?: string;
  duyuruBaslamaTarihi?: dayjs.Dayjs;
  duyuruBitisTarihi?: dayjs.Dayjs;
  egitim?: IEgitim | null;
}

export class Duyuru implements IDuyuru {
  constructor(
    public id?: number,
    public duyuruBaslik?: string,
    public duyuruIcerik?: string,
    public duyuruBaslamaTarihi?: dayjs.Dayjs,
    public duyuruBitisTarihi?: dayjs.Dayjs,
    public egitim?: IEgitim | null
  ) {}
}

export function getDuyuruIdentifier(duyuru: IDuyuru): number | undefined {
  return duyuru.id;
}

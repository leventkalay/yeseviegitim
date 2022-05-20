export interface IEgitimTuru {
  id?: number;
  adi?: string | null;
  aciklama?: string | null;
  aktif?: boolean | null;
}

export class EgitimTuru implements IEgitimTuru {
  constructor(public id?: number, public adi?: string | null, public aciklama?: string | null, public aktif?: boolean | null) {
    this.aktif = this.aktif ?? false;
  }
}

export function getEgitimTuruIdentifier(egitimTuru: IEgitimTuru): number | undefined {
  return egitimTuru.id;
}

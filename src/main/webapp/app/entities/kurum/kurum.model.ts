export interface IKurum {
  id?: number;
  adi?: string | null;
  aciklama?: string | null;
  aktif?: boolean | null;
}

export class Kurum implements IKurum {
  constructor(public id?: number, public adi?: string | null, public aciklama?: string | null, public aktif?: boolean | null) {
    this.aktif = this.aktif ?? false;
  }
}

export function getKurumIdentifier(kurum: IKurum): number | undefined {
  return kurum.id;
}

export interface IEgitmen {
  id?: number;
  adiSoyadi?: string | null;
  unvani?: string | null;
  puani?: number | null;
  aktif?: boolean | null;
}

export class Egitmen implements IEgitmen {
  constructor(
    public id?: number,
    public adiSoyadi?: string | null,
    public unvani?: string | null,
    public puani?: number | null,
    public aktif?: boolean | null
  ) {
    this.aktif = this.aktif ?? false;
  }
}

export function getEgitmenIdentifier(egitmen: IEgitmen): number | undefined {
  return egitmen.id;
}

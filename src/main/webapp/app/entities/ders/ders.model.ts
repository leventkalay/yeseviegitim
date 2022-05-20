import { IEgitim } from 'app/entities/egitim/egitim.model';

export interface IDers {
  id?: number;
  adi?: string | null;
  aciklama?: string | null;
  dersLinki?: string | null;
  dersVideosuContentType?: string | null;
  dersVideosu?: string | null;
  egitim?: IEgitim | null;
}

export class Ders implements IDers {
  constructor(
    public id?: number,
    public adi?: string | null,
    public aciklama?: string | null,
    public dersLinki?: string | null,
    public dersVideosuContentType?: string | null,
    public dersVideosu?: string | null,
    public egitim?: IEgitim | null
  ) {}
}

export function getDersIdentifier(ders: IDers): number | undefined {
  return ders.id;
}

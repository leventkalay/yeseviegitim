import { IEgitim } from 'app/entities/egitim/egitim.model';

export interface ITakvim {
  id?: number;
  adi?: string | null;
  egitim?: IEgitim | null;
}

export class Takvim implements ITakvim {
  constructor(public id?: number, public adi?: string | null, public egitim?: IEgitim | null) {}
}

export function getTakvimIdentifier(takvim: ITakvim): number | undefined {
  return takvim.id;
}

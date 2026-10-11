import { ValueTransformer } from 'typeorm';

/** Bulatkan ke 2 angka desimal untuk menghindari floating point drift. */
export const round2 = (value: number): number =>
  Math.round((value + Number.EPSILON) * 100) / 100;

export const formatRupiah = (value: number): string =>
  `Rp${value.toLocaleString('id-ID', { minimumFractionDigits: 0, maximumFractionDigits: 2 })}`;

/** Postgres mengembalikan kolom decimal sebagai string, ubah jadi number. */
export const decimalTransformer: ValueTransformer = {
  to: (value?: number | null) => value,
  from: (value?: string | null) =>
    value === null || value === undefined ? value : parseFloat(value),
};

export const moneyColumn = {
  type: 'decimal' as const,
  precision: 15,
  scale: 2,
  default: 0,
  transformer: decimalTransformer,
};

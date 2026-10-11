import { round2 } from '../common/utils/money';

export interface SplitInput {
  name: string;
  amount: number;
}

/**
 * Bagi pajak + service secara proporsional ke tiap orang sesuai porsi
 * pesanannya. Selisih pembulatan ditaruh di orang terakhir supaya
 * jumlah semua split selalu sama dengan total tagihan.
 */
export function calculateSplits(
  splits: SplitInput[],
  tax: number,
  service: number,
): { totalAmount: number; splits: SplitInput[] } {
  const subtotal = round2(splits.reduce((sum, s) => sum + s.amount, 0));
  const extra = round2(tax + service);

  let distributed = 0;
  const result = splits.map((split, index) => {
    const isLast = index === splits.length - 1;
    const share = isLast
      ? round2(extra - distributed)
      : round2((extra * split.amount) / subtotal);
    distributed = round2(distributed + share);
    return { name: split.name, amount: round2(split.amount + share) };
  });

  return { totalAmount: round2(subtotal + extra), splits: result };
}

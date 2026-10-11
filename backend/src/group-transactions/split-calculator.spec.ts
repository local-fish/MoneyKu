import { calculateSplits } from './split-calculator';

describe('calculateSplits', () => {
  it('membagi pajak & service secara proporsional', () => {
    const result = calculateSplits(
      [
        { name: 'Andi', amount: 50000 },
        { name: 'Budi', amount: 30000 },
        { name: 'Citra', amount: 20000 },
      ],
      11000,
      5000,
    );

    expect(result.totalAmount).toBe(116000);
    expect(result.splits).toEqual([
      { name: 'Andi', amount: 58000 },
      { name: 'Budi', amount: 34800 },
      { name: 'Citra', amount: 23200 },
    ]);
  });

  it('total split selalu sama dengan total tagihan walau ada pembulatan', () => {
    const result = calculateSplits(
      [
        { name: 'A', amount: 10000 },
        { name: 'B', amount: 10000 },
        { name: 'C', amount: 10000 },
      ],
      1000,
      0,
    );

    const sum = result.splits.reduce((acc, s) => acc + s.amount, 0);
    expect(Math.round(sum * 100) / 100).toBe(result.totalAmount);
    expect(result.totalAmount).toBe(31000);
  });

  it('tanpa pajak & service, amount tidak berubah', () => {
    const result = calculateSplits([{ name: 'A', amount: 12345.67 }], 0, 0);
    expect(result).toEqual({
      totalAmount: 12345.67,
      splits: [{ name: 'A', amount: 12345.67 }],
    });
  });
});

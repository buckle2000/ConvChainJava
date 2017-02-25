package ConvChain;

import java.util.function.BiPredicate;

class Pattern {
    public boolean[][] data;

    private int getSize() {
        return data.length;
    }

    private void Set(BiPredicate<Integer, Integer> f) {
        for (int j = 0; j < getSize(); j++) for (int i = 0; i < getSize(); i++) data[i][j] = f.test(i, j);
    }

    public Pattern(int size, BiPredicate<Integer, Integer> f) {
        data = new boolean[size][size];
        Set(f);
    }

    public Pattern(boolean[][] field, int x, int y, int size) {
        this(size, (i, j) -> false);
        Set((i, j) ->
                field[(x + i + field.length) % field.length]
                        [(y + j + field[0].length) % field[0].length]);
    }

    public Pattern getRotated() {
        return new Pattern(getSize(), (x, y) -> data[getSize() - 1 - y][x]);
    }

    public Pattern getReflected() {
        return new Pattern(getSize(), (x, y) -> data[getSize() - 1 - x][y]);
    }

    public int getIndex() {
        int result = 0;
        for (int y = 0; y < getSize(); y++)
            for (int x = 0; x < getSize(); x++)
                result += data[x][y] ? 1 << (y * getSize() + x) : 0;
        return result;
    }
}

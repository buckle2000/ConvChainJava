package ConvChain;


import java.util.HashMap;
import java.util.Random;

public class Generator {
    public double temperature;
    public Random random;
    public boolean[][] field;
    public final double DEFAULT_WEIGHT = 0.01;

    private int N;
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private Pattern[] patterns;
    private HashMap<Integer, Double> weights;

    public Generator(boolean[][] sample, int N, double temperature, int width, int height) {
        this.N = N;
        this.temperature = temperature;
        this.width = width;
        this.height = height;
        this.random = new Random();
        this.field = new boolean[width][height];
        this.weights = new HashMap<>();

        for (int y = 0; y < sample[0].length; y++)
            for (int x = 0; x < sample.length; x++) {
                Pattern[] p = new Pattern[8];
                p[0] = new Pattern(sample, x, y, N);
                p[1] = p[0].getRotated();
                p[2] = p[1].getRotated();
                p[3] = p[2].getRotated();
                p[4] = p[0].getReflected();
                p[5] = p[1].getReflected();
                p[6] = p[2].getReflected();
                p[7] = p[3].getReflected();
                for (int k = 0; k < 8; k++)
                    weights.put(p[k].getIndex(), weights.getOrDefault(p[k].getIndex(), 0.) + 1.);
            }

        weights.replaceAll((pattern, weight) -> {
            if (weight <= 0.)
                return DEFAULT_WEIGHT;
            else
                return weight;
        });

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                field[x][y] = random.nextBoolean();
    }

    double energyExp(int i, int j) {
        double value = 1.0;
        for (int y = j - N + 1; y <= j + N - 1; y++)
            for (int x = i - N + 1; x <= i + N - 1; x++)
                value *= weights.getOrDefault(new Pattern(field, x, y, N).getIndex(), DEFAULT_WEIGHT);
        return value;
    }

    void metropolis(int i, int j) {
        double p = energyExp(i, j);
        field[i][j] = !field[i][j];
        double q = energyExp(i, j);

        if (Math.pow(q / p, 1.0 / temperature) < random.nextDouble())
            field[i][j] = !field[i][j];
    }

    public void iterate() {
        for (int k = 0; k < width * height; k++)
            metropolis(random.nextInt(width), random.nextInt(height));
    }


}

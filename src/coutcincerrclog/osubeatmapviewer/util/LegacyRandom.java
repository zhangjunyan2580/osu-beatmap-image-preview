package coutcincerrclog.osubeatmapviewer.util;

public class LegacyRandom {

    public static final double INT_TO_REAL = 1.0 / (Integer.MAX_VALUE + 1.0);
    public static final int INT_MASK = 0x7FFFFFFF;
    public static final int y = 0x32378FC7;
    public static final int z = 0xD55F8767;
    public static final int w = 0x104AA1AD;

    private int X;
    private int Y = y;
    private int Z = z;
    private int W = w;

    public LegacyRandom(int seed) {
        X = seed;
    }

    public LegacyRandom() {
        this(1337);
    }

    public int nextUnsignedInt() {
        int t = X ^ (X << 11);
        X = Y;
        Y = Z;
        Z = W;
        return W = W ^ (W >>> 19) ^ t ^ (t >>> 8);
    }

    public int next() {
        return INT_MASK & nextUnsignedInt();
    }

    public int next(int upperBound) {
        return (int) (this.nextDouble() * upperBound);
    }

    public int next(int lowerBound, int upperBound) {
        return (int) (lowerBound + nextDouble() * (upperBound - lowerBound));
    }

    public int next(double lowerBound, double upperBound) {
        return (int) (lowerBound + nextDouble() * (upperBound - lowerBound));
    }

    public double nextDouble() {
        return INT_TO_REAL * next();
    }

    private int bitBuffer;
    private int bitIndex = 32;

    public boolean nextBoolean() {
        if (bitIndex == 32) {
            bitBuffer = this.nextUnsignedInt();
            bitIndex = 1;
            return (bitBuffer & 1) == 1;
        }
        ++bitIndex;
        return ((bitBuffer >>>= 1) & 1) == 1;
    }

}

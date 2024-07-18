package flik;

public class HorribleSteve {
    private static final int MAX_VALUE = 500;
    public static void main(String[] args) throws Exception {
        int i = 0;
        for (; i < MAX_VALUE; ++i) {
            if (!Flik.isSameNumber(i, i)) {
                throw new Exception(
                        String.format("i:%d not same as j:%d ??", i, i));
            }
        }
        System.out.println("i is " + i);
    }
}

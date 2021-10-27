package lab1;

public class WeatherForecaster {
    public static int[] getWarmerIn(int[] temperatures){
        int[] nextBetterDay = new int[temperatures.length];
        for (int i = 0; i < temperatures.length - 1; ++i){
            int better = 0;
            for (int j = i + 1; j < temperatures.length; ++j){
                if (temperatures[i] < temperatures[j]) {
                    better = j - i;
                    break;
                }
            }
            nextBetterDay[i] = better;
        }
        return nextBetterDay;
    }

    public static void main(String[] args) {
        int[] arr1 = getWarmerIn(new int[]{3, 4, 5, 1, -1, 2, 6, 3});
        for (int i = 0; i < arr1.length; ++i) {
            System.out.print(arr1[i]);
            System.out.print(" ");
        }
        System.out.println();

        int[] arr2 = getWarmerIn(new int[]{3, 4, 5, 6});
        for (int i = 0; i < arr2.length; ++i) {
            System.out.print(arr2[i]);
            System.out.print(" ");
        }
        System.out.println();

        int[] arr3 = getWarmerIn(new int[]{3, 6, 9});
        for (int i = 0; i < arr3.length; ++i) {
            System.out.print(arr3[i]);
            System.out.print(" ");
        }
        System.out.println();

        int[] arr4 = getWarmerIn(new int[]{1, 1, 1, 1});
        for(int i = 0; i < arr4.length; ++i) {
            System.out.print(arr4[i] + " ");
        }
    }
}

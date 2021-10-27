package lab1;

public class ArrayAnalyzer {
    public static boolean isMountainArray(int[] array){
        if (array.length < 3) {
            return false;
        }

        boolean isIncreasing = true;
        for (int i = 0; i < array.length - 1; ++i){
            if (array[i] == array[i + 1]) {
                return false;
            }

            if (isIncreasing){
                if(array[i] > array[i + 1]) {
                    isIncreasing = false;
                }
            } else {
                if (array[i] < array[i + 1])
                    return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        if (isMountainArray(new int[]{2,1})){
            System.out.println("1: true");
        } else{
            System.out.println("1: false");
        }

        if (isMountainArray(new int[]{3,5,5})){
            System.out.println("2: true");
        } else{
            System.out.println("2: false");
        }

        if (isMountainArray(new int[]{0,3,2,1})){
            System.out.println("3: true");
        } else{
            System.out.println("3: false");
        }
        // false, false, true
    }
}

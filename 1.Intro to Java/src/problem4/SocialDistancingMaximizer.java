package problem4;

public class SocialDistancingMaximizer {

        public static int maxDistance(int[] seats) {
            int numberOfSeats = seats.length;
            int maxDistance = 0;
            int currentDistance = 0;

            boolean freeSeatsAtStart = true;

            for (int i = 0; i < numberOfSeats; i++) {
                if (seats[i] == 0) {
                    ++currentDistance;
                } else {
                    if (freeSeatsAtStart) {
                        if (currentDistance > maxDistance) {
                            maxDistance = currentDistance;
                        }
                        freeSeatsAtStart = false;
                    } else {
                        int halfCurrentDistance = currentDistance % 2 == 0 ? currentDistance / 2 : currentDistance / 2 + 1;
                        if (halfCurrentDistance > maxDistance) {
                            maxDistance = halfCurrentDistance;
                        }
                    }
                    currentDistance = 0;
                }
            }

            if (currentDistance > maxDistance) {
                maxDistance = currentDistance;
            }
            return maxDistance;
        }

    public static void main(String[] args) {
        System.out.println(maxDistance(new int[]{1, 0, 0, 0, 1, 0, 1})); //2
        System.out.println(maxDistance(new int[]{1,0,0,0})); // 3
        System.out.println(maxDistance(new int[]{0, 1})); // 1
        System.out.println(maxDistance(new int[]{0, 0, 0, 1})); // 3
        System.out.println(maxDistance(new int[]{0, 0, 0, 0, 1})); // 3
        System.out.println(maxDistance(new int[]{1, 0, 0, 0, 0})); // 4
        System.out.println(maxDistance(new int[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0})); // 5
        System.out.println(maxDistance(new int[]{1, 0})); // 1
        System.out.println(maxDistance(new int[]{1, 0, 1, 0, 1, 0, 0 , 1})); //1
    }
}

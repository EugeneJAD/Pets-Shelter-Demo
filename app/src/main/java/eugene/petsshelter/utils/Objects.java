package eugene.petsshelter.utils;

public class Objects {
    public static boolean equals(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        }
        if (o2 == null || o1.getClass() != o2.getClass()) {
            return false;
        }

        return o1.equals(o2);
    }
}

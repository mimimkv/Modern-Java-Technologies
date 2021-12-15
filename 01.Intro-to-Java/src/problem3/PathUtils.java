package problem3;

public class PathUtils {
    public static String getCanonicalPath(String path) {
        String[] tokens = path.split("/");
        StringBuilder canonicalPath = new StringBuilder("");

        int counter = 0;
        for (int i = tokens.length - 1; i >= 0; --i) {
            if (tokens[i].equals(".") || tokens[i].isEmpty()) {
                continue;
            } else if (tokens[i].equals("..")) {
                ++counter;
            } else if (counter == 0) {
                canonicalPath.insert(0, "/" + tokens[i]);
            } else {
                --counter;
            }
        }
        if (canonicalPath.isEmpty()) {
            canonicalPath.append('/');
        }
        return canonicalPath.toString();
    }

    public static void main(String[] args) {

        String s1 = getCanonicalPath("/home/");
        String s2 = getCanonicalPath("/../");
        String s3 = getCanonicalPath("/home//foo/");
        String s4 = getCanonicalPath("/a/./b/../../c/");

        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        System.out.println(s4);

    }
}

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Small and quick utility class to read a file's contents.
 */
public final class ReadInput {
    private ReadInput() {}

    public static List<Integer> asInteger(String fileName) {
        List<Integer> data = new ArrayList<>();
        try {
            File file = new File("C:\\Users\\razor\\Google Drive (sant.suomi@gmail.com)\\DSA Exercises\\" + fileName);
            try(Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    data.add(Integer.parseInt(sc.nextLine()));
                }
            }
        } catch (FileNotFoundException | NumberFormatException e) {
            System.out.println(e);
        }
        return data;
    }

    public static List<String> asString(String fileName) {
        List<String> data = new ArrayList<>();
        try {
            File file = new File("C:\\Users\\razor\\Google Drive (sant.suomi@gmail.com)\\DSA Exercises\\" + fileName);
            try(Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    data.add(sc.nextLine());
                }
            }
        } catch (FileNotFoundException | NumberFormatException e) {
            System.out.println(e);
        }
        return data;
    }
}
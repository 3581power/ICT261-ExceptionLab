import java.io.*;
import java.util.*;
import java.util.logging.*;

public class MarksImporter {

    // 1. Define the Custom Exception
    public static class DuplicateStudentException extends RuntimeException {
        public DuplicateStudentException(String message) {
            super(message);
        }
    }

    // 2. The Logger setup (for technical details)
    private static final Logger logger = Logger.getLogger(MarksImporter.class.getName());

    public static void importMarks(String filename) throws IOException {
        Set<String> seen = new HashSet<>();

        // Try-with-resources automatically closes the BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] parts = line.split(",");

                try {
                    // TODO 1: Check for exactly 2 fields
                    if (parts.length != 2) {
                        throw new IllegalArgumentException("Invalid line format: " + line);
                    }

                    String studentId = parts[0].trim();
                    String markString = parts[1].trim();

                    // TODO 2: Parse mark and validate 0..100
                    int mark = Integer.parseInt(markString);
                    if (mark < 0 || mark > 100) {
                        throw new IllegalArgumentException("Mark out of range (0-100): " + mark);
                    }

                    // TODO 3: Detect duplicates
                    if (!seen.add(studentId)) {
                        throw new DuplicateStudentException("Duplicate student ID found: " + studentId);
                    }

                    // TODO 4: Display accepted record
                    System.out.println("Successfully imported: " + studentId + " with mark " + mark);

                } catch (NumberFormatException e) {
                    // Recovery strategy: Skip the bad record and continue
                    System.out.println("Skipping bad record: Non-numeric mark found in line -> " + line);
                    logger.log(Level.WARNING, "NumberFormatException for line: " + line, e);
                } catch (IllegalArgumentException | DuplicateStudentException e) {
                    // Skip invalid or duplicate records and continue
                    System.out.println("Skipping invalid record: " + e.getMessage());
                    logger.log(Level.WARNING, "Invalid record skipped: " + line, e);
                }
            }
        }
    }

    public static void main(String[] args) {
        // Activity C: Catch FileNotFoundException BEFORE IOException
        try {
            importMarks("marks.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Marks file not found; check the file name.");
            logger.log(Level.SEVERE, "File not found", e); // Logs technical details
        } catch (IOException e) {
            System.out.println("Error: A system error occurred while reading the file.");
            logger.log(Level.SEVERE, "IO Error", e);
        } finally {
            // This ALWAYS runs, even if an exception occurs
            System.out.println("Import attempt finished");
        }
    }
}
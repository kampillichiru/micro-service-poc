import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PDFComparator {

    public static void main(String[] args) {
        String filePath1 = "path/to/first/file.pdf";
        String filePath2 = "path/to/second/file.pdf";

        try {
            String checksum1 = calculateChecksum(filePath1);
            String checksum2 = calculateChecksum(filePath2);

            if (checksum1.equals(checksum2)) {
                System.out.println("Checksums are the same.");
            } else {
                System.out.println("Checksums are different.");
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static String calculateChecksum(String filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(filePath);
        byte[] dataBytes = new byte[1024];

        int bytesRead;
        while ((bytesRead = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, bytesRead);
        }

        byte[] mdBytes = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte mdByte : mdBytes) {
            sb.append(Integer.toString((mdByte & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}

import org.apache.commons.text.similarity.LevenshteinDistance;

public class FuzzyMatch {
    public static void main(String[] args) {
        String keyword1 = "";
        String[] strings1 = {
            "",
            ""
        };
        fuzzyMatch(keyword1, strings1);

        String keyword2 = "";
        String[] strings2 = {
            ""
        };
        fuzzyMatch(keyword2, strings2);

        String keyword3 = "";
        String[] strings3 = {
     
        };
        fuzzyMatch(keyword3, strings3);
    }

    public static void fuzzyMatch(String keyword, String[] strings) {
        LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();

        for (String str : strings) {
            int distance = levenshteinDistance.apply(keyword, str);
            double similarity = 1 - (double) distance / Math.max(keyword.length(), str.length());

            if (similarity > 0.8) {  // Adjust threshold as needed
                System.out.println("'" + keyword + "' matches with '" + str + "' with similarity: " + similarity);
            }
        }
    }
}


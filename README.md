private static boolean levenshteinDistanceSimilarity(String keyword, String targetKeyword) {
    LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();

    return Arrays.stream(Delimiters.values())
            .flatMap(sourceDelimiter -> Arrays.stream(Delimiters.values())
                    .flatMap(targetDelimiter -> Arrays.stream(keyword.split(sourceDelimiter.getDelimiter()))
                            .flatMap(splitSourceKeyword -> Arrays.stream(targetKeyword.split(targetDelimiter.getDelimiter()))
                                    .map(splitTargetKeyword -> new Pair<>(splitSourceKeyword, splitTargetKeyword))
                            )
                    )
            )
            .anyMatch(pair -> {
                double similarity = getSimilarity(levenshteinDistance, pair.getFirst(), pair.getSecond());
                int averageNoOfWords = getAverageNoOfWords(pair.getFirst(), pair.getSecond());
                return similarity > getThreshold(averageNoOfWords);
            });
}

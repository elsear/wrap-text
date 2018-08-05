package org.sfstudy.homework.core;

import org.sfstudy.homework.exception.UnsupportedLineLengthException;

public class StringFormatter {
    /**StringBuilder for storage current line as temp.*/
    private static StringBuilder tempSb = new StringBuilder();
    private static StringBuilder resultSb = new StringBuilder();

    public static String wrapText(String text, int maxCharsPerLine) throws Exception {
        /** As we know, \n also symbol in line. It's mean that max length of line should be shorter by one symbol.*/
        maxCharsPerLine -= 1;

        /**Transform text to one line with one space between words.*/
        String oneLineText = text
                .replaceAll("\r\n", " ")
                .replaceAll("\n", " ")
                .replaceAll("\r", " ")
                .replaceAll(" +", " ");

        /**Create array with words.*/
        String[] wordsArray = oneLineText.split(" ");

        for (String word : wordsArray) {
            append(word, maxCharsPerLine);
        }
        resolveLastLine();
        return resultSb.toString();
    }

    /**
     * Method to append line by line to result text according the rules from question.
     */
    private static void append(String word, int maxCharsPerLine) throws Exception {

        /** It makes no sense to create infinity text with new line symbol only*/
        if (maxCharsPerLine < 1) throw new UnsupportedLineLengthException("Unsupported max length of line. Please use value more then 1.");

        int availableTempLength = getAvailableLineLength(maxCharsPerLine);

        if (word.length() < availableTempLength) {
            if (tempSb.length() != 0) tempSb.append(" ");
            tempSb.append(word);
        } else if (word.length() == availableTempLength) {
            if (tempSb.length() != 0) tempSb.append(" ");
            resultSb.append(tempSb);
            resultSb.append(word);
            resultSb.append("\n");
            tempSb.setLength(0);
        } else {
            if (word.length() > maxCharsPerLine) {
            /**If word is longer than the allowed length, recursively pass by parts of the word, as for individual words.*/
                if (availableTempLength == 0) {
                    defaultCommitLine();
                    availableTempLength = getAvailableLineLength(maxCharsPerLine);
                }

                String firstWordPart = word.substring(0, availableTempLength);
                String tempWordPart = word.substring(firstWordPart.length());
                append(firstWordPart, maxCharsPerLine);
                String[] wordParts = tempWordPart.split("(?<=\\G.{" + maxCharsPerLine + "})");

                for (String wordPart : wordParts) {
                    append(wordPart, maxCharsPerLine);
                }
            } else {
                defaultCommitLine();
                tempSb.append(word);
            }
        }
    }

    /**
     * Trying to calculate available number of characters remaining in the current line.
     */
    private static int getAvailableLineLength(int maxCharsPerLine) {
        return maxCharsPerLine == 1 ? 1 : tempSb.length() == 0
                ? maxCharsPerLine - tempSb.length() : maxCharsPerLine - tempSb.length() - 1;
    }

    private static void defaultCommitLine() {
        tempSb.append("\n");
        resultSb.append(tempSb);
        tempSb.setLength(0);
    }

    private static void resolveLastLine() {
        if (tempSb.length() != 0) {
            resultSb.append(tempSb);
            resultSb.append("\n");
        }
    }

}

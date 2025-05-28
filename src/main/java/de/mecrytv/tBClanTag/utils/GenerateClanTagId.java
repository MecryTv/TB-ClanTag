package de.mecrytv.tBClanTag.utils;

import de.mecrytv.tBClanTag.TBClanTag;
import de.mecrytv.tBClanTag.database.ClanTags.ClanTagManager;

import java.security.SecureRandom;
import java.time.Instant;

public class GenerateClanTagId {

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String VOWELS = "AEIOU";
    private static final String CONSONANTS = "BCDFGHJKLMNPQRSTVWXYZ";
    private static final String NUMBERS = "0123456789";

    private static final int ID_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final ClanTagManager clanTagManager = TBClanTag.getInstance().getClanTagManager();

    public static String generate() {
        String id;
        int attempts = 0;
        final int MAX_ATTEMPTS = 100;

        do {
            if (attempts < 50) {
                id = generateReadableId();
            } else {
                id = generateRandomId();
            }
            attempts++;
        } while (!isUnique(id) && attempts < MAX_ATTEMPTS);

        if (attempts >= MAX_ATTEMPTS) {
            id = generateTimestampBasedId();
        }

        return id;
    }

    private static String generateReadableId() {
        StringBuilder sb = new StringBuilder(ID_LENGTH);

        for (int i = 0; i < ID_LENGTH; i++) {
            if (i % 3 == 2) {
                sb.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
            } else if (i % 2 == 0) {
                sb.append(CONSONANTS.charAt(RANDOM.nextInt(CONSONANTS.length())));
            } else {
                sb.append(VOWELS.charAt(RANDOM.nextInt(VOWELS.length())));
            }
        }

        return sb.toString();
    }

    private static String generateRandomId() {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    private static String generateTimestampBasedId() {
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        String randomSuffix = generateRandomId().substring(0, 4);

        String combined = Math.abs(timestamp.hashCode()) + randomSuffix;

        if (combined.length() > ID_LENGTH) {
            combined = combined.substring(0, ID_LENGTH);
        }

        return combined.toUpperCase();
    }

    private static boolean isUnique(String id) {
        return !clanTagManager.isClanTagExists(id);
    }
}
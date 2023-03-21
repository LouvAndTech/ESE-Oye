package fr.eseoye.eseoye.helpers;

import java.util.Base64;

public class SecurityHelper {

    public static final int IMG_FILE_LENGTH = 8;
    public static final int SECURE_ID_LENGTH = 12;

    public static String generateSecureID(long time, int dbID, int maxLength) {
        final byte[] res = new byte[maxLength];

        for (int i = 0; i < maxLength; i++) res[i] = (byte) (i + time ^ dbID * 2 ^ 64);
        return Base64.getEncoder().encodeToString(res).replace('+', '-').replace('/', '_');
    }

}

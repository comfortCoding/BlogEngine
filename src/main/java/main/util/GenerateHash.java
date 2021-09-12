package main.util;

import static main.config.Config.HASH_LENGTH;
import static main.config.Config.HASH_SOURCE;

public class GenerateHash {

    public String generateHash()
    {
        StringBuilder sb = new StringBuilder(HASH_LENGTH);

        for (int i = 0; i < HASH_LENGTH; i++) {

            int index = (int)(HASH_SOURCE.length() * Math.random());

            sb.append(HASH_SOURCE.charAt(index));
        }

        return sb.toString();
    }

}

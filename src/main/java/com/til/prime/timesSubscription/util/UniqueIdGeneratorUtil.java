package com.til.prime.timesSubscription.util;

import com.til.prime.timesSubscription.constants.GlobalConstants;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

import java.util.Random;

public class UniqueIdGeneratorUtil {

    private static Random random = new Random();
    private static final int BASE36_RADIX = 36;
    private static final int CONSECUTIVE_SIMILAR_CHARACTERS = 4;

    public static final String generateOrderId(){
        return generateUniqueIdInConsecutiveSimilarCharactersPattern(GlobalConstants.ORDER_ID_LENGTH, null);
    }

    public static final String generatePrimeId(){
        return generateUniqueIdInConsecutiveSimilarCharactersPattern(GlobalConstants.PRIME_ID_LENGTH, null);
    }

    public static final String generateUniqueIdInConsecutiveSimilarCharactersPattern(int length, Boolean startsWithCharacter){
        StringBuilder sb = new StringBuilder();
        boolean flag = startsWithCharacter!=null ? startsWithCharacter: random.nextBoolean();
        for(int i=0; i<=((length-1)/CONSECUTIVE_SIMILAR_CHARACTERS); i++){
            if(flag){
                RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('A', 'Z').filteredBy(CharacterPredicates.LETTERS,
                        CharacterPredicates.DIGITS).build();
                sb.append(generator.generate(Math.min(CONSECUTIVE_SIMILAR_CHARACTERS, length-(CONSECUTIVE_SIMILAR_CHARACTERS*i))));
            }else{
                RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('0', '9').filteredBy(CharacterPredicates.LETTERS,
                        CharacterPredicates.DIGITS).build();
                sb.append(generator.generate(Math.min(CONSECUTIVE_SIMILAR_CHARACTERS, length-(CONSECUTIVE_SIMILAR_CHARACTERS*i))));
            }
            flag = !flag;
        }
        return sb.toString();
    }

    public static String convertToBase36(String arg) {
        String base62 = Long.toString(Long.parseLong(arg), BASE36_RADIX);
        return base62;
    }

    public static final String generateCode(String mobile, int length){
        StringBuilder sb = new StringBuilder();
        sb.append(convertToBase36(mobile));
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('0', 'z').filteredBy(CharacterPredicates.LETTERS,
                CharacterPredicates.DIGITS).build();
        sb.append(generator.generate(length-sb.length()));
        return sb.toString();
    }
}

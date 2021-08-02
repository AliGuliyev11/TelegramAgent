package com.mycode.telegramagent.utils;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote Class for generate random password,if agent clicks forgot password
 */

public class PasswordCreator {


    public static String passwordGenerator(){
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange(0, 100)
                .filteredBy(CharacterPredicates.ASCII_ALPHA_NUMERALS)
                .build();
        return generator.generate(8);
    }
}

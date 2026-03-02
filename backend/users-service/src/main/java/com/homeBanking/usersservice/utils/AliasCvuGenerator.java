package com.homeBanking.usersservice.utils;


import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
/*public class AliasCvuGenerator {

    private Random random = new Random();

    public String generateAlias() {

        List<String> aliasOptions = new ArrayList<>();
        List<String> chosenAlias = new ArrayList<>();

        try {
            File aliasFile = new File("/Users/pablotrivino/Backup Notebook/Pablo/Mauro/Home-banking-with-Microservices/users-service/src/main/java/com/homeBanking/usersservice/utils/alias.txt");
            Scanner myReader = new Scanner(aliasFile);

            while (myReader.hasNextLine()) {
                String word = myReader.nextLine();
                aliasOptions.add(word);
            }

            myReader.close();

            for (int i = 0; i < 3; i++) {
                int randomPosition;
                String randomAlias;

                do {
                    randomPosition = random.nextInt(aliasOptions.size());
                    randomAlias = aliasOptions.get(randomPosition);
                } while (chosenAlias.contains(randomAlias));

                chosenAlias.add(randomAlias);
            }

            return String.join(".", chosenAlias);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }*/
public class AliasCvuGenerator {

    private static final Random RANDOM = new Random();

    public static String generateAlias() {
        try {
            ClassPathResource resource = new ClassPathResource("alias.txt");

            List<String> words;
            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

                words = reader.lines()
                        .filter(line -> !line.isBlank())
                        .collect(Collectors.toList());
            }

            if (words.size() < 3) {
                throw new IllegalStateException("alias.txt debe contener al menos 3 palabras");
            }

            return random(words) + "." + random(words) + "." + random(words);

        } catch (Exception e) {
            throw new IllegalStateException("Error al generar alias desde alias.txt", e);
        }
    }

    public String generateCvu() {

        StringBuilder cvu= new StringBuilder("7031990");

        for(int i=0; i<15; i++) {
            int randomNumber = RANDOM.nextInt(10);
            cvu.append(randomNumber);
        }

        return cvu.toString();
    }

    private static String random(List<String> words) {
        return words.get(RANDOM.nextInt(words.size())).toUpperCase();
    }
}


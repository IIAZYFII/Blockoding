package com.azyf.finalyearproject;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.core.Block;

/**
 * The official IntelliJ Idea tutorial was followed to set up a JavaFx with SpringBoot. This class was part of the set up.
 * Reference -> https://www.youtube.com/watch?v=u0dEf-QN-90
 */
@SpringBootApplication
public class FinalYearProjectApplication {

    public static void main(String[] args) {

        Application.launch(BlockApplication.class, args);
    }

}

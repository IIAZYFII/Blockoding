package com.azyf.finalyearproject;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.core.Block;

@SpringBootApplication
public class FinalYearProjectApplication {

    public static void main(String[] args) {

        Application.launch(BlockApplication.class, args);
    }

}

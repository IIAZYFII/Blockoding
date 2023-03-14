package com.azyf.finalyearproject;

import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;

public class TerminalComponent {
    private static TextArea terminal;

    public TerminalComponent() {
        terminal = new TextArea();
    }

    public static TextArea getTerminal() {
        return terminal;
    }

    public static void setTerminal(TextArea terminal) {
        TerminalComponent.terminal = terminal;
    }

    public static void setTerminalContent(String content) {
        String appendContent = terminal.getText() + content + "\n";
        terminal.setText(appendContent);
    }

    public static void askTerminalContent(String content) {
        setTerminalContent(content);
        String tmpContent = terminal.getText();
        terminal.setOnMouseClicked(e -> {
            terminal.clear();
            terminal.setEditable(true);
            e.consume();
        });
        terminal.setOnKeyPressed( e -> {
            if(e.getCode() == KeyCode.ENTER) {
                String userInput = terminal.getText();
                terminal.clear();
                terminal.setText(tmpContent + "\n" + userInput);
                terminal.setEditable(false);
                terminal.setOnMouseClicked(mouseEvent -> {mouseEvent.consume();});
                terminal.setOnKeyPressed(keyEvent -> {keyEvent.consume();});

                e.consume();

            }

        });
    }




}

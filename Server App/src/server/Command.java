/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.Arrays;

/**
 *
 * @author Demo
 */
public class Command
{
    private final String label;
    private final String[] args;
    private final Worker source;
    
    Command(String[] command, Worker source) {
        if (0 == command.length) {
            label = null;
            args = null;
        } else {
            label = command[0];
            args = Arrays.copyOfRange(command, 1, command.length);
        }
        this.source = source;
    }
    
    Command(String command, Worker source) {
        this(command.split(" "), source);
    }
    
    public static Command anonymousCommand(String command) {
        return new Command(command.split(" "), null);
    }
    
    Worker getSource() {
        return source;
    }
    
    String getLabel() {
        return label;
    }
    
    String[] getArgs() {
        return args;
    }
    
    String getArg(int index) {
        if (index < 0 || index >= args.length) return null;
        return args[index];
    }
    
    boolean isCommand(String command) {
        return null != label && label.equalsIgnoreCase(command);
    }
    
    Command nextCommand() {
        return new Command(args, source);
    }
    
    boolean isCommand(String command, int numArgs) {
        return isCommand(command) && args.length == numArgs;
    }
    
    @Override
    public String toString() {
        return label + " " + String.join(" ", args);
    }
}

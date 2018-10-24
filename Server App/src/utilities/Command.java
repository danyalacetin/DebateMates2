/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.Arrays;
import server.Worker;

/**
 *
 * @author Demo
 */
public class Command
{
    private final String label;
    private final String[] args;
    private final Worker source;
    
    private Command(String[] command, Worker source) {
        if (0 == command.length) {
            label = null;
            args = null;
        } else {
            label = command[0].toLowerCase();
            args = Arrays.copyOfRange(command, 1, command.length);
            for (int i = 0; i < args.length; ++i) {
                args[i] = args[i].toLowerCase();
            }
        }
        this.source = source;
    }
    
    private Command(String command, Worker source) {
        this(command.split(" "), source);
    }
    
    public static Command createAnonymous(String command) {
        return new Command(command, null);
    }
    
    public static Command create(String command, Worker worker) {
        return new Command(command, worker);
    }
    
    public Worker getSource() {
        return source;
    }
    
    public String getLabel() {
        return label;
    }
    
    public String[] getArgs() {
        return args;
    }
    
    public int getNumArgs() {
        return args.length;
    }
    
    public String getArg(int index) {
        if (index < 0 || index >= args.length) return null;
        return args[index];
    }
    
    public boolean is(String command) {
        return null != label && label.equalsIgnoreCase(command);
    }
    
    public Command extractCommand() {
        return new Command(args, source);
    }
    
    public Command wrapCommand(String newCommand) {
        return new Command(newCommand + " " + toString(), source);
    }
    
    public boolean is(String command, int numArgs) {
        return is(command) && args.length == numArgs;
    }
    
    @Override
    public String toString() {
        if (0 == getNumArgs()) return label;
        return label + " " + String.join(" ", args);
    }
}

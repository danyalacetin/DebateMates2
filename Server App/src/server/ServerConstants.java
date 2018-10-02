/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Demo
 */
enum ServerConstants {
    JOINED("join success"),
    LOGIN("login success"),
    CONNECTED("connect success");
    
    
    private final String msg;
    
    private ServerConstants(String msg) {
        this.msg = msg;
    }
    
    @Override
    public String toString() {
        return msg;
    }
}

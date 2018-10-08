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
    JOIN_SUCCESS("join success"),
    JOIN_FAILED("join failed"),
    LOGIN_SUCCESS("login success"),
    LOGIN_FAILED("login failed");
    
    
    private final String msg;
    
    private ServerConstants(String msg) {
        this.msg = msg;
    }
    
    @Override
    public String toString() {
        return msg;
    }
}

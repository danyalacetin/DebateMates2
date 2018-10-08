package com.example.aleczhong.myapplication.applogic;

public abstract class DelayedReturn {
    private String failureString;
    private String successString;

    public DelayedReturn(String success, String fail) {
        failureString = fail;
        successString = success;
    }

    public abstract void onSuccess();
    public abstract void onFailure();

    public boolean check(String toCompare) {
        boolean isSuccessful = false;
        if (toCompare.equalsIgnoreCase(successString)) {
            isSuccessful = true;
            onSuccess();
        }
        else if (toCompare.equalsIgnoreCase(failureString)) {
            isSuccessful = true;
            onFailure();
        }
        return isSuccessful;
    }
}

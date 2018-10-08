package com.example.aleczhong.myapplication.applogic;

public abstract class DelayedReturn {

    public abstract void onSuccess();
    public abstract void onFailure();
    public abstract int testString(String toTest);

    public boolean check(String toTest) {
        int compare = testString(toTest);
        if (1 == compare) onSuccess();
        else if (-1 == compare) onFailure();
        return 0 != compare;
    }
}
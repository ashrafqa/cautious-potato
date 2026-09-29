package com.example.hooks;

import com.example.support.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {
    @Before
    public void setUp() {
        DriverManager.start();
    }

    @After
    public void tearDown() {
        DriverManager.stop();
    }
}

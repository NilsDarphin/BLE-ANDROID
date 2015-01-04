package com.example.nils.lec;

import java.util.ArrayList;

/**
 * Created by nils on 04/01/15.
 */
public class AppsManager {

    public  class App {
        public int resId;
        public String name;
        public String description;

        public App (int resId, String name, String description) {
            this.resId = resId;
            this.name = name;
            this.description = description;
        }

        public App (String name, String description) {
            this.resId = R.drawable.ic_launcher;
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getResId() {
            return resId;
        }

    }

    private ArrayList<App> apps;

    public AppsManager () {
        apps = new  ArrayList<App>();
        apps.add(new App("Titre 1", "Description 1"));
        apps.add(new App(R.drawable.mole, "Titre 1", "Description 1"));
    }

    public ArrayList<App> getApps() { return apps; }
}
package com.teamtreehouse.public_data.controller;

import com.teamtreehouse.public_data.dao.CountryDao;
import com.teamtreehouse.public_data.model.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prompter {
    private List<Country> countries;
    private BufferedReader reader;
    private Map<String, String> menu;
    private CountryDao dao;

    public Prompter(CountryDao countryDao) {
        dao = countryDao;
        countries = dao.fetchAllCountries();
        reader = new BufferedReader(new InputStreamReader(System.in));
        menu = new HashMap<>();
        menu.put("edit", "Edit a country");
        menu.put("add", "Add a country");
        menu.put("delete", "Delete a country");
    }

    private String promptAction() throws IOException {
        System.out.println("Your options are:");
        for (Map.Entry<String, String> option : menu.entrySet()) {
            System.out.printf("%n%s - %s %n",
                    option.getKey(),
                    option.getValue());
        }
        System.out.print("\nWhat do you want to do: ");
        String choice = reader.readLine();
        return choice.trim().toLowerCase();
    }

    public void loadMenuOptions() {
        String choice = "";
        do{
            try {
                choice = promptAction();
                switch (choice) {
                    case "edit":
                    Country country = promptForCountry(countries);
                    dao.editCountry(country);
                }
            } catch (IOException ioe) {
                System.out.print("%nProblem with input%n%n");
                ioe.printStackTrace();
            }
        } while (!choice.equals("quit"));
    }

    private Country promptForCountry(List<Country> countries) throws IOException {
        int counter = 1;
        int selection = -1;
        for (Country country : countries) {
            System.out.printf("%d.)  %s %n", counter, country.getName());
            counter++;
        }

        do {
            try {
                System.out.print("Select a country:");
                String optionAsString = reader.readLine();
                selection = Integer.parseInt(optionAsString.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.  Please enter a number.");
            }
        } while (selection > countries.size() || selection < 1);
        selection = selection - 1;

        int index = countries.indexOf(selection);
        return dao.fetchAllCountries().get(index);
    }
}

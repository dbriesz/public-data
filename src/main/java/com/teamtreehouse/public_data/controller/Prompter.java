package com.teamtreehouse.public_data.controller;

import com.teamtreehouse.public_data.dao.CountryDao;
import com.teamtreehouse.public_data.model.Country;
import javassist.NotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Prompter {
    private CountryDao dao;
    private List<Country> countries;
    private BufferedReader reader;
    private Map<String, String> menu;

    public Prompter(CountryDao countryDao) {
        dao = countryDao;
        countries = new ArrayList<>();
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
        String choice = "";
        for (Country country : countries) {
            System.out.printf("%s.)  %s %n", country.getCode(), country.getName());
        }

        do {
            try {
                System.out.print("Select a country:");
                choice = reader.readLine();
                countries.stream()
                        .filter(country -> country.getCode().equals(choice))
                        .findFirst()
                        .orElseThrow(NotFoundException::new);
            } catch (IOException e) {
                System.out.println("Invalid input.  Please enter a 3 letter country code.");
            }
        } while (choice.length() != 3);

        return dao.findCountryByCode(choice);
    }
}

package com.teamtreehouse.public_data.controller;

import com.teamtreehouse.public_data.dao.CountryDao;
import com.teamtreehouse.public_data.model.Country;

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
        countries = dao.fetchAllCountries();
        reader = new BufferedReader(new InputStreamReader(System.in));
        menu = new HashMap<>();
        menu.put("edit", "Edit a country");
        menu.put("add", "Add a country");
        menu.put("delete", "Delete a country");
    }

    private String promptAction() throws IOException {
        System.out.println("\n\nYour options are:");
        for (Map.Entry<String, String> option : menu.entrySet()) {
            System.out.printf("%n%s - %s %n",
                    option.getKey(),
                    option.getValue());
        }
        System.out.print("\n\nWhat do you want to do: ");
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
                    country = editCountryData(country);
                    dao.editCountry(country);
                }
            } catch (IOException ioe) {
                System.out.print("%nProblem with input%n%n");
                ioe.printStackTrace();
            }
        } while (!choice.equals("quit"));
    }

    private Country promptForCountry(List<Country> countries) throws IOException {
        String choice;
        Country countryChoice = null;
        for (Country country : countries) {
            System.out.printf("Country Code: %s \tCountry: %s %n", country.getCode(), country.getName());
        }

        do {
            try {
                System.out.print("\nSelect a country by its 3 letter code): ");
                choice = reader.readLine();
                if (choice.length() != 3) {
                    System.out.print("\nSelect a country by its 3 letter code): ");
                    choice = reader.readLine();
                }
                countryChoice = dao.findByCode(countries, choice.toUpperCase());
            } catch (IOException e) {
                System.out.println("\nInvalid input.  Please enter a 3 letter country code.");
            }
        } while (!countries.contains(countryChoice) || countryChoice == null);

        return countryChoice;
    }

    private Country editCountryData(Country country) throws IOException {

        System.out.print("\nPlease enter the country name: ");
        String name = reader.readLine();
        country.setName(name);
        System.out.print("\nPlease enter the number of internet users: ");
        Double internetUsers = Double.parseDouble(reader.readLine());
        country.setInternetUsers(internetUsers);
        System.out.print("\nPlease enter the adult literacy rate: ");
        Double adultLiteracy = Double.parseDouble(reader.readLine());
        country.setAdultLiteracyRate(adultLiteracy);

        country = new Country.CountryBuilder(name)
                .withInternetUsers(internetUsers)
                .withAdultLiteracy(adultLiteracy)
                .build();

        System.out.println("\nUpdating...");
        return country;
    }
}

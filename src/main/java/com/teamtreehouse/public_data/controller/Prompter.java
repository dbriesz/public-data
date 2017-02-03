package com.teamtreehouse.public_data.controller;

import com.teamtreehouse.public_data.dao.CountryDao;
import com.teamtreehouse.public_data.model.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.*;

public class Prompter {
    private CountryDao dao;
    private List<Country> countries;
    private BufferedReader reader;
    private Map<Integer, String> menu;

    public Prompter(CountryDao countryDao) {
        dao = countryDao;
        countries = new ArrayList<>();
        countries = dao.fetchAllCountries();
        reader = new BufferedReader(new InputStreamReader(System.in));
        menu = new HashMap<>();
        menu.put(1, "View country data");
        menu.put(2, "Edit a country");
        menu.put(3, "Add a country");
        menu.put(4, "Delete a country");
        menu.put(5, "View statistics for each indicator");
        menu.put(6, "Quit");
    }

    private int promptAction() throws IOException {
        System.out.println("\n\nYour options are:");
        for (Map.Entry<Integer, String> option : menu.entrySet()) {
            System.out.printf("%n%d - %s %n",
                    option.getKey(),
                    option.getValue());
        }
        System.out.print("\nWhat do you want to do: ");

        return Integer.parseInt(reader.readLine());
    }

    public void loadMenuOptions() throws IOException {
        Integer choice = 0;
        do{
            try {
                choice = promptAction();
                switch (choice) {
                    case 1:
                        viewCountryData();
                        break;
                    case 2:
                        editCountryData();
                        break;
                    case 3:
                        addCountry();
                        break;
                    case 4:
                        deleteCountry();
                        break;
                    case 5:
                        viewStatistics();
                        break;
                    case 6:
                        System.out.println("\nGoodbye!");
                        System.exit(0);
                    default:
                        System.out.printf("%nUnknown choice:  '%d'. Try again.  %n%n", choice);
                }
            } catch (IOException ioe) {
                System.out.print("%nProblem with input%n%n");
                ioe.printStackTrace();
            }
        } while (choice != 6);
    }

    private Country promptForCountry(List<Country> countries) throws IOException {
        String choice;
        Country countryChoice = null;
        System.out.println("\nCode\tCountry");
        System.out.println("-----------------------------------------------");
        for (Country country : countries) {
            System.out.printf("%s\t%s%n", country.getCode(), country.getName());
        }

        do {
            try {
                System.out.print("\nSelect a country by its 3 letter code: ");
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

    private String promptForCode() throws IOException {
        String code = "";
        do {
            try {
                System.out.print("\nPlease enter a 3 letter country code: ");
                code = reader.readLine();
            } catch (IOException e) {
                System.out.println("\nInvalid input.  Please enter a 3 letter country code.");
            }
        } while (code.length() != 3 || code.equals(""));

        return code.toUpperCase();
    }

    private String promptForName() throws IOException {
        String name = "";

        do {
            try {
                System.out.print("\nPlease enter the country name: ");
                name = reader.readLine();
            } catch (IOException e) {
                System.out.println("\nInvalid input.  Please enter a country name.");
            }
        } while (name.length() > 32 || name.equals(""));

        return name;
    }

    private Double promptForInternetUsers() throws IOException {
        Double internetUsers = -1.0;

        do {
            try {
                System.out.print("\nPlease enter the percentage of internet users: ");
                internetUsers = Double.valueOf(reader.readLine());
            } catch (IOException e) {
                System.out.println("\nInvalid input.  Please enter a number.");
            }
        } while (internetUsers < 0 || internetUsers > 100);

        return internetUsers;
    }

    private Double promptForAdultLiteracy() throws IOException {
        Double adultLiteracy = -1.0;

        do {
            try {
                System.out.print("\nPlease enter the adult literacy rate: ");
                adultLiteracy = Double.valueOf(reader.readLine());
            } catch (IOException e) {
                System.out.println("\nInvalid input.  Please enter a number.");
            }
        } while (adultLiteracy < 0 || adultLiteracy > 100);

        return adultLiteracy;
    }

    private void viewCountryData() {
        System.out.println("\nCode\tCountry\t\t\t\t\t\t\t\tInternet Users\tLiteracy");
        System.out.println("---------------------------------------------------------------------------------");
        String internetUsersAsString;
        String adultLiteracyAsString;

        for (Country country : countries) {
            if (country.getInternetUsers() == null) {
                internetUsersAsString = "--";
            } else {
                internetUsersAsString = String.valueOf(new DecimalFormat("#.00").format(country.getInternetUsers()));
            }

            if (country.getAdultLiteracyRate() == null) {
                adultLiteracyAsString = "--";
            } else {
                adultLiteracyAsString = String.valueOf(new DecimalFormat("#.00").format(country.getAdultLiteracyRate()));
            }

            System.out.printf("%s\t\t%-32s\t%-14s\t%s%n",
                    country.getCode(), country.getName(), internetUsersAsString, adultLiteracyAsString);
        }
    }

    private void editCountryData() throws IOException {
        Country country = promptForCountry(countries);

        country.setName(promptForName());
        country.setInternetUsers(promptForInternetUsers());
        country.setAdultLiteracyRate(promptForAdultLiteracy());

        dao.update(country);

        System.out.println("\nUpdate complete!");
    }

    private void addCountry() throws IOException {
        Country country = new Country.CountryBuilder(promptForCode())
                .withName(promptForName())
                .withInternetUsers(promptForInternetUsers())
                .withAdultLiteracy(promptForAdultLiteracy())
                .build();
        System.out.printf("Adding %s to the database...%n", country.getName());
        dao.save(country);
        countries.add(country);
        System.out.println("\nCountry added!");
    }

    private void deleteCountry() throws IOException {
        Country country = promptForCountry(countries);
        System.out.printf("%nRemoving %s from the database...%n", country.getName());
        dao.delete(country);
        countries.remove(country);
        System.out.println("\nCountry deleted!");
    }

    private void viewStatistics() {
        String minInternetName = dao.minInternetUsers().getName();
        String maxInternetName = dao.maxInternetUsers().getName();
        String minAdultLiteracyName = dao.minAdultLiteracy().getName();
        String maxAdultLiteracyName = dao.maxAdultLiteracy().getName();

        Double minInternet = dao.minInternetUsers().getInternetUsers();
        Double maxInternet = dao.maxInternetUsers().getInternetUsers();
        Double minAdultLiteracy = dao.minAdultLiteracy().getAdultLiteracyRate();
        Double maxAdultLiteracy = dao.maxAdultLiteracy().getAdultLiteracyRate();

        System.out.println("\nCountry with the lowest percentage of internet users:");
        System.out.printf("%s\t\t\t%.2f", minInternetName, minInternet);

        System.out.println("\n\nCountry with the highest percentage of internet users:");
        System.out.printf("%s\t\t%.2f", maxInternetName, maxInternet);

        System.out.println("\n\nCountry with the lowest percentage of adult literacy:");
        System.out.printf("%s\t\t\t%.2f", minAdultLiteracyName, minAdultLiteracy);

        System.out.println("\n\nCountry with the highest percentage of adult literacy:");
        System.out.printf("%s\t\t\t%.2f", maxAdultLiteracyName, maxAdultLiteracy);

        System.out.print("\n\nAverage internet users:\t\t");
        System.out.printf("%.2f", dao.avgInternetUsers());

        System.out.print("\n\nAverage adult literacy:\t\t");
        System.out.printf("%.2f", dao.avgAdultLiteracy());

        System.out.print("\n\nCorrelation coefficient between internet users and adult literacy:\t\t");
        System.out.printf("%.2f", dao.getCorrelationCoefficient());

        if (dao.isPositive()) {
            System.out.println("\nThere is a positive correlation between a country's internet users and adult literacy.");
        } else {
            System.out.println("\nThere is a negative correlation between a country's internet users and adult literacy.");
        }
    }
}

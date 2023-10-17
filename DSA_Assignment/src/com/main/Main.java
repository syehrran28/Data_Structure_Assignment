package com.main;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Collision collision = null;

        //=======================================
        // Start choosing collision
        //=======================================

        boolean selectedCollision = false;
        while (!selectedCollision) {
            System.out.println("\nSolution for collision: ");
            System.out.println("1: Open Addressing");
            System.out.println("2: Separate Chaining");
            System.out.print("Choose the solution for collision: ");
            int collisionOption = scanner.nextInt();
            if (collisionOption == 1) {
                System.out.println("\nType of Open Addressing:");
                System.out.println("1: Linear Probing");
                System.out.println("2: Quadratic Probing");
                System.out.println("3: Double Hashing");
                System.out.print("Enter the type of Open Addressing: ");
                int openAddressingOption = scanner.nextInt();
                switch (openAddressingOption) {
                    case 1:
                        collision =  Collision.LINEAR_PROBING;
                        selectedCollision = true;
                        break;
                    case 2:
                        collision =  Collision.QUADRATIC_PROBING;
                        selectedCollision = true;
                        break;
                    case 3:
                        collision =  Collision.DOUBLE_HASHING;
                        selectedCollision = true;
                        break;
                    default:
                        System.out.println("Invalid input pls try again. ");
                        selectedCollision = false;
                        break;
                }
            } else if (collisionOption == 2) {
                collision =  Collision.SEPARATE_CHAINING;
                selectedCollision = true;
            } else {
                System.out.println("Invalid input pls try again. ");
                selectedCollision = false;
            }
        }
        //=======================================
        // End choosing collision
        //=======================================

        //=======================================
        // Start init HashTable
        //=======================================

        boolean selectedSize = false;
        int size = 0;
        while (!selectedSize) {
            System.out.print("\nEnter the size for the hash table (prefer prime number): ");
            size = scanner.nextInt();
            if (size <= 0) {
                System.out.println("Value must more than zero. ");
            } else {
                selectedSize = true;
            }
        }


        System.out.println("\nData type: ");
        System.out.println("1: Integer");
        System.out.println("2: String");
        System.out.print("Enter your choice: ");
        int valueType = scanner.nextInt();
        boolean IntegerType = (valueType == 1);

        HashTable hashTable = new HashTable(size, IntegerType, collision);

        //=======================================
        // End init HashTable
        //=======================================

        //=======================================
        // Start main programme
        //=======================================
        boolean running = true;
        while (running) {
            System.out.println("\nOptions:");
            System.out.println("1: Insert a value into the hash table");
            System.out.println("2: Remove a value from the hash table");
            System.out.println("3: Search for a value in the hash table");
            System.out.println("4: Clear the hash table");
            System.out.println("5: Print the hash table");
            System.out.println("0: Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter a value: ");
                    String value = scanner.nextLine();
                    boolean inserted = hashTable.insert(value);
                    if (inserted) {
                        System.out.println("Value inserted into the hash table.");
                    } else {
                        System.out.println("Value insert failed. ");
                    }
                    break;

                case 2:
                    System.out.print("Enter a value to remove: ");
                    String removeValue = scanner.nextLine();
                    boolean removed = hashTable.remove(removeValue);
                    if (removed) {
                        System.out.println("Value removed from the hash table.");
                    } else {
                        System.out.println("Value not found in the hash table.");
                    }
                    break;

                case 3:
                    System.out.print("Enter a value to search: ");
                    String searchValue = scanner.nextLine();
                    int search = hashTable.contains(searchValue);
                    if (collision == Collision.SEPARATE_CHAINING && search >= 0) {
                        System.out.println("Index not found");
                    }
                    break;

                case 4:
                    hashTable.clear();
                    System.out.println("Hash table cleared.");
                    break;
                case 5:
                    System.out.println("Hash table:");
                    hashTable.print();
                    break;

                case 0:
                    running = false;
                    break;

                default:
                    System.out.println("Invalid input for the option above. ");
            }
        }

        scanner.close();
        System.out.println("Programme terminated, Thanks for using HashTable programme. ");
        //=======================================
        // End main programme
        //=======================================
    }


}

package com.main;

import java.util.LinkedList; // LinkedList allows us to use linked lists and enabling efficient manipulation of linked lists in our Java program.
import java.util.Scanner;


public class HashTable {
    private LinkedList<Entry>[] table;
    private int size;
    private int key;
    private boolean IntegerType;
    private Collision collision;
    private int primeNum;

    public HashTable(int key, boolean IntegerType, Collision collision) {
        this.key = key;
        this.size = 0;
        this.IntegerType = IntegerType;
        this.table = new LinkedList[key];
        for (int i = 0; i < key; i++) {
            table[i] = new LinkedList<>();
        }
        this.collision = collision;
        this.primeNum = 0;
    }


    private int hashFunction(String value) {  // Hash Function
        if (IntegerType) {
            try {
                int intValue = Integer.parseInt(value);
                return Math.abs(intValue % key);
            } catch (NumberFormatException e) {
                return -1;
            }
        } else {
            int hash = 0;
            for (char c : value.toUpperCase().toCharArray()) {
                if (c >= 'A' && c <= 'Z') {
                    hash += (c - 'A' + 1);
                }
            }
            return Math.abs(hash % key);
        }
    }

    private int secondHashFunction(String value) {
        Scanner scanner = new Scanner(System.in);
        int intValue;
        if (IntegerType) {
            try {
                intValue = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return -1;
            }
        } else {
            intValue = value.hashCode();
        }

        while (primeNum == 0) {
            System.out.print("\nEnter a prime number for double hashing: ");
            int primeNumber = scanner.nextInt();

            if(!(checkPrimeNumber(primeNumber))) {
                System.out.println("The input is not a prime number. ");
            } else if (primeNumber > key) {
                System.out.println("The input is larger that the table capacity. ");
            } else {
                primeNum = primeNumber;
                return primeNumber - (intValue % primeNumber);
            }
        }
        return -1;
    }

    public int contains(String value) {  // Checks if a value exists in the hash table
        if (IntegerType) {
            try {
                int intValue = Integer.parseInt(value);
                if (intValue <= 0) {
                    System.out.println("Invalid value. Please enter a whole number which is greater than zero.");
                    return -1;
                }
                value = String.valueOf(intValue);
            } catch (NumberFormatException e) {
                System.out.println("Invalid value. Please enter a whole number which is greater than zero.");
                return -1;
            }
        } else {
            if (!value.matches("[a-zA-Z]+")) {
                System.out.println("Invalid value. Please enter a string without any numbers, spaces, or symbols.");
                return -1;
            }
        }

        int index = hashFunction(value);
        LinkedList<Entry> list = table[index];

        if (collision != Collision.SEPARATE_CHAINING) {
            // OPEN ADDRESSING

            int count = 0;
            int indexBeforeRehash = index;

            boolean isCollision = !(list.isEmpty());

            try {
                while (isCollision) {
                    if(list.get(0).value.equals(value)) {
                        System.out.println("Value found at index " + index);
                        return -1;
                    }
                    count += 1;
                    switch (collision) {
                        case LINEAR_PROBING:
                            index = (indexBeforeRehash + count) % key;
                            break;
                        case QUADRATIC_PROBING:
                            index = (indexBeforeRehash + (count * count)) % key;
                            break;
                        case DOUBLE_HASHING:
                            int stepSize = secondHashFunction(value);
                            index = (indexBeforeRehash + (count * stepSize)) % key;
                            break;
                        default:
                            return -1;
                    }
                    // Check again after rehash the index
                    list = table[index];
                    isCollision = !(list.isEmpty());
                }
            } catch (Exception e) {
                System.out.println("Index not found. ");
                return -1;
            }
            return index;
        } else {
            // SEPARATE CHAINING
            for (Entry entry : list) {
                if (entry.value.equals(value)) {
                    System.out.println("Value found at index " + index);
                    return -1;
                }
            }

            return index;
        }
    }

    public boolean insert(String value) {           //Insert value in the hash table
        if(collision != Collision.SEPARATE_CHAINING) {
            if(size >= key) {
                System.out.println("Full capacity for open addressing.");
                return false;
            }
        }

        int index = contains(value);
        if (index != -1) {
            LinkedList<Entry> list = table[index];
            list.add(new Entry(value));
            size++;
            return true;
        }
        return false;
    }
    


    public boolean remove(String value) {       // Removes a value from the hash table
        if (IntegerType) {
            try {
                int intValue = Integer.parseInt(value);
                if (intValue <= 0) {
                    System.out.println("Invalid value. Please enter a whole number which is greater than zero.");
                    return false;
                }
                value = String.valueOf(intValue);
            } catch (NumberFormatException e) {
                System.out.println("Invalid value. Please enter a whole number which is greater than zero.");
                return false;
            }
        } else {
            if (!value.matches("[a-zA-Z]+")) {
                System.out.println("Invalid value. Please enter a string without any numbers.");
                return false;
            }
        }

        int index = hashFunction(value);
        LinkedList<Entry> list = table[index];

        if (collision != Collision.SEPARATE_CHAINING) {
            // OPEN ADDRESSING
            int count = 0;
            int indexBeforeRehash = index;

            boolean isCollision = !(list.isEmpty());

            while (isCollision) {
                if(list.get(0).value.equals(value)) {
                    list.remove(0);
                    size--;
                    return true;
                }
                count += 1;
                switch (collision) {
                    case LINEAR_PROBING:
                        index = (indexBeforeRehash + count) % key;
                        break;
                    case QUADRATIC_PROBING:
                        index = (indexBeforeRehash + (count * count)) % key;
                        break;
                    case DOUBLE_HASHING:
                        int stepSize = secondHashFunction(value);

                        index = (indexBeforeRehash + (count * stepSize)) % key;
                        break;
                    default:
                        return false;
                }
                // Check again after rehash the index
                list = table[index];
                isCollision = !(list.isEmpty());
            }
        }
        // SEPARATE CHAINING
        for (Entry entry : list) {
            if (entry.value.equals(value)) {
                list.remove(entry);
                size--;
                return true;
            }
        }
        return false;

    }

    public void clear() {					// Erase hash table and set the size to 0
        for (int i = 0; i < key; i++) {
            table[i].clear();
        }
        size = 0;
    }


    public void print() {					// Display contents of hash table
        for (int i = 0; i < key; i++) {
            LinkedList<Entry> list = table[i];
            System.out.print("Index " + i + ": ");
            boolean isFirstValue = true;
            for (Entry entry : list) {
                if (!isFirstValue) {
                    System.out.print(" -> ");
                }
                System.out.print(entry.value);
                isFirstValue = false;
            }
            System.out.println();
        }
    }

    private static class Entry {
        private String value;

        public Entry(String value) {
            this.value = value;
        }
    }

    private boolean checkPrimeNumber(int value) {
        if (value <= 1) {
            return false;
        }
        for (int i = 2; i < value; i++) {
            if((value % i) == 0) {
                return false;
            }
        }
        return true;
    }
}

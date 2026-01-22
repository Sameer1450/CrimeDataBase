package crime;
import edu.rutgers.cs112.LL.LLNode;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
/**
 * Students will be analyzing cybercrime incident data using hash tables (with separate chaining) 
 * and linked lists to organize and query crime records efficiently. The theme revolves around 
 * parsing, storing, and analyzing real-world crime logs from Rutgers University’s public 
 * safety database: Daily Crime & Fire Safety Log.
 * 
 * @author Anna Lu
 * @author Krish Lenka
 * Modifications, extensions, and implementation by:
 * @author Sameer Mansuri
 *
 * Course: CS 112 – Rutgers University 
 * Term: Fall 2025
 */
public class RUCrimeDatabase {
    private LLNode<Incident>[] incidentTable; // Array of LLNodes
    private int totalIncidents;
    private static final double LOAD_FACTOR_THRESHOLD = 4.0;

    /**
     * Default constructor initializes the hash table with a size of 10.
     * The total number of incidents is set to zero.
     */
    public RUCrimeDatabase() {
        incidentTable = new LLNode[10];
        totalIncidents = 0;
    }

    /**
     * Adds a new incident to the hash table
     * @param incident An incident object which we will use to add to the hash table:
     */
    public void addIncident(Incident incident) {
         int index = hashFunction(incident.getIncidentNumber());
        LLNode<Incident> newNode = new LLNode<Incident>(incident);
        newNode.setNext(incidentTable[index]);
        incidentTable[index] = newNode;
        totalIncidents++;
        double loadFactor = (double) totalIncidents / incidentTable.length;
        if (loadFactor >= LOAD_FACTOR_THRESHOLD) {
            rehash();
        }
    }

    /**
     * Reads the csv file, creates an Incident object for each line, and calls addIncident().
     * @param filename Path to file containing incident data
     */
    public void buildIncidentTable(String inputfile) {
        StdIn.setFile(inputfile);
        while (!StdIn.isEmpty()) {
            String line = StdIn.readLine();
            if (line == null || line.trim().equals("")) continue;
            String[] parts = line.split(",");
            String incidentNumber   = parts[0];
            String nature           = parts[1];
            String reportDate       = parts[2];
            String occurrenceDate   = parts[3];
            String location         = parts[4];
            String disposition      = parts[5];
            String generalLocation  = parts[6];
            Category category = Category.fromString(nature);
            Incident newIncident = new Incident(
                    incidentNumber,
                    nature,
                    reportDate,
                    occurrenceDate,
                    location,
                    disposition,
                    generalLocation,
                    category
            );
            addIncident(newIncident);
        }
    }
 

    /**
     * Rehashes the incident groups in the hash table.
     * This is called when the load factor exceeds a certain threshold.
     */
    public void rehash() {
        LLNode<Incident>[] oldTable = incidentTable;
        incidentTable = (LLNode<Incident>[]) new LLNode[oldTable.length * 2];
        totalIncidents = 0;
        for (int i = 0; i < oldTable.length; i++) {
            LLNode<Incident> current = oldTable[i];
            while (current != null) {
                Incident inc = current.getData();
                addIncident(inc);
                current = current.getNext();
            }
        }
    } 

    /**
     * Deletes an incident based on its incident number.
     * @param incidentNumber The incident number of the incident to delete 
     */
    public void deleteIncident(String incidentNumber) {
        int index = hashFunction(incidentNumber);
        LLNode<Incident> current = incidentTable[index];
        LLNode<Incident> prev = null;
        while (current != null) {
            Incident inc = current.getData();
            if (inc.getIncidentNumber().equals(incidentNumber)) {
                if (prev == null) {
                    incidentTable[index] = current.getNext();
                } else {
                    prev.setNext(current.getNext());
                }
                totalIncidents--;
                return;
            }
            prev = current;
            current = current.getNext();
        }
    }

    /**
     * Iterates over another RUCrimeDatabase's incident table, and adds its incidents
     * to this table IF they do not already exist.
     * @param other RUCrimeDatabase to copy new incidents from
     */
    public void join(RUCrimeDatabase other) {
        LLNode<Incident>[] otherTable = other.getIncidentTable();
        for (int i = 0; i < otherTable.length; i++) {
            LLNode<Incident> current = otherTable[i];
            while (current != null) {
                Incident otherIncident = current.getData();
                String incNum = otherIncident.getIncidentNumber();
                int index = hashFunction(incNum);
                LLNode<Incident> check = incidentTable[index];
                boolean exists = false;
                while (check != null) {
                    if (check.getData().getIncidentNumber().equals(incNum)) {
                        exists = true;
                        break;
                    }
                    check = check.getNext();
                }
                if (!exists) {
                    addIncident(otherIncident);
                }
                current = current.getNext();
            }
        }
    } 
    
    /**
     * Returns a list of the top K locations with the most incidents 
     * If K > numLocations, return all locations
     * @return ArrayList<String> containing the top K locations
     */
    public ArrayList<String> topKLocations(int K) {
        String[] locations = {
        "ACADEMIC",
        "CAMPUS SERVICES",
        "OTHER",
        "PARKING LOT",
        "RECREATION",
        "RESIDENTIAL",
        "STREET/ROADWAY"
        };
        int[] counts = new int[locations.length];
        for (int i = 0; i < incidentTable.length; i++) {
            LLNode<Incident> current = incidentTable[i];
            while (current != null) {
                Incident inc = current.getData();
                String gl = inc.getGeneralLocation();
                if (gl != null) {
                    for (int j = 0; j < locations.length; j++) {
                        if (gl.equals(locations[j])) {
                            counts[j]++;
                            break;
                        }
                    }
                }
                current = current.getNext();
            }
        }
        ArrayList<String> result = new ArrayList<String>();
        int limit = K;
        if (limit > locations.length) {
            limit = locations.length;
        }
        for (int r = 0; r < limit; r++) {
            int maxIndex = -1;
            int maxCount = -1;
            for (int i = 0; i < counts.length; i++) {
                if (counts[i] > maxCount) {
                    maxCount = counts[i];
                    maxIndex = i;
                }
            }
            if (maxIndex == -1 || maxCount <= 0) {
                break;
            }
            result.add(locations[maxIndex]);
            counts[maxIndex] = -1;
        }
        return result;
    }  

    /**
     * Returns the percentage of incidents for every category.
     * Categories: Property, Violent,
     *             Mischief, Trespass, or Other
     * @return A HashMap<Category, Double> with percentage of incidents of each category
     */
    public HashMap<Category, Double> natureBreakdown() { 
        HashMap<Category, Double> breakdown = new HashMap<Category, Double>();
        breakdown.put(Category.PROPERTY, 0.0);
        breakdown.put(Category.VIOLENT, 0.0);
        breakdown.put(Category.MISCHIEF, 0.0);
        breakdown.put(Category.TRESPASS, 0.0);
        breakdown.put(Category.OTHER, 0.0);
        for (int i = 0; i < incidentTable.length; i++) {
            LLNode<Incident> current = incidentTable[i];
            while (current != null) {
                Incident inc = current.getData();
                Category c = inc.getCategory();
                breakdown.put(c, breakdown.get(c) + 1.0);
                current = current.getNext();
            }
        }
        if (totalIncidents > 0) {
            breakdown.put(Category.PROPERTY,
                    100.0 * (breakdown.get(Category.PROPERTY) / totalIncidents));
            breakdown.put(Category.VIOLENT,
                    100.0 * (breakdown.get(Category.VIOLENT) / totalIncidents));
            breakdown.put(Category.MISCHIEF,
                    100.0 * (breakdown.get(Category.MISCHIEF) / totalIncidents));
            breakdown.put(Category.TRESPASS,
                    100.0 * (breakdown.get(Category.TRESPASS) / totalIncidents));
            breakdown.put(Category.OTHER,
                    100.0 * (breakdown.get(Category.OTHER) / totalIncidents));
        }
        return breakdown;
    }

    //Given methods
    /**
     * DO NOT MODIFY THIS METHOD.
     * Returns the hash table array for inspection/testing
     * @return The array of LLNode<IncidentGroup> representing the hash table
     */
    public LLNode<Incident>[] getIncidentTable() {
        return incidentTable;
    }

    public void setIncidentTable(LLNode<Incident>[] incidentTable) {
        this.incidentTable = incidentTable;
    }

    public int numberOfIncidents() {
        return totalIncidents;
    }

    /**
     * DO NOT MODIFY THIS METHOD.
     * Returns the index in the hash table for a given incident number.
     * @return The index in the hash table for the incident number
     * @param incidentNumber The incident number to hash
     */
    private int hashFunction(String incidentNumber) {
        String last5Digits = incidentNumber.substring(Math.max(0, incidentNumber.length() - 5));
        int val = Integer.parseInt(last5Digits) % incidentTable.length;
        //System.out.println("Hashing incident number: " + last5Digits + " val: " + val);
        return val;
    }

}
package com.hjwylde.uni.comp261.assignment2.stringSearch;

import java.io.*;
import java.util.*;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.hjwylde.commons.lang.trees.Trie;

/*
 * Code for Assignment 2, COMP 261
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class AssignmentTasks {
    
    public static final FileFilter TXT_FILTER = new FileFilter() {
        
        @Override
        public boolean accept(File file) {
            String name = file.getName();
            
            int extIndex = name.lastIndexOf(".");
            if (extIndex != -1) {
                String ext = name.substring(extIndex + 1);
                
                return (ext.equalsIgnoreCase("txt") || file.isDirectory());
            }
            
            return file.isDirectory();
        }
    };
    
    private File rootDirectory;
    
    private Map<File, String> documents;
    
    private List<String> phrasesList;
    private Trie phrasesTrie;
    
    private Map<File, List<Integer>> phrasesCount;
    
    public AssignmentTasks() {
        if (!loadDocuments()) {
            JOptionPane.showMessageDialog(null, "Error loading documents.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!loadPhrases()) {
            JOptionPane.showMessageDialog(null, "Error loading phrases.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        taskOne();
        
        taskTwo();
    }
    
    public void taskOne() {
        StringSearch finder = new StringSearch.BoyerMoore();
        
        // Boyer Moore
        
        long curTime = System.currentTimeMillis();
        
        Map<File, List<Integer>> phrasesCount = new HashMap<>();
        for (Map.Entry<File, String> e : documents.entrySet()) {
            String document = e.getValue();
            List<Integer> counts = new ArrayList<>(phrasesList.size());
            
            for (int i = 0; i < phrasesList.size(); i++) {
                String haystack = document;
                String phrase = phrasesList.get(i);
                int count = 0;
                
                int index = finder.find(document, phrase);
                while (index != -1) {
                    count++;
                    
                    if ((index + 1) > haystack.length())
                        break;
                    
                    haystack = haystack.substring(index + 1);
                    index = finder.find(haystack, phrase);
                }
                
                counts.add(count);
            }
            
            phrasesCount.put(e.getKey(), counts);
        }
        
        System.out.println("Total time for Boyer Moore: "
            + (System.currentTimeMillis() - curTime) + "ms.");
        curTime = System.currentTimeMillis();
        
        /*
         * KMP: This is just a repeat of the above code using KMP instead, to test /
         * compare the times of KMP and Boyer Moore.
         */
        
        // Knuth Morris Pratt
        
        /*
         * finder = new StringSearch.KnuthMorrisPrat();
         * 
         * phrasesCount = new HashMap<File, List<Integer>>();
         * for (Map.Entry<File, String> e : documents.entrySet()) {
         * String document = e.getValue();
         * List<Integer> counts = new ArrayList<Integer>(phrasesList.size());
         * 
         * for (int i = 0; i < phrasesList.size(); i++) {
         * String haystack = document;
         * String phrase = phrasesList.get(i);
         * int count = 0;
         * 
         * int index = finder.find(document, phrase);
         * while (index != -1) {
         * count++;
         * 
         * if (index + 1 > haystack.length())
         * break;
         * 
         * haystack = haystack.substring(index + 1);
         * index = finder.find(haystack, phrase);
         * }
         * 
         * counts.add(count);
         * }
         * 
         * phrasesCount.put(e.getKey(), counts);
         * }
         * 
         * System.out.println("Total time for Knuth Morris Pratt: "
         * + (System.currentTimeMillis() - curTime) + "ms.");
         * curTime = System.currentTimeMillis();
         */
        
        // Trie
        
        Map<File, List<Integer>> triePhrasesCount = new HashMap<>();
        for (Map.Entry<File, String> e : documents.entrySet()) {
            String document = e.getValue();
            List<Integer> counts = new ArrayList<>(phrasesList.size());
            
            Map<CharSequence, List<Integer>> phraseIndices = phrasesTrie
                .search(document);
            for (int i = 0; i < phrasesList.size(); i++) {
                String phrase = phrasesList.get(i);
                
                counts.add((phraseIndices.containsKey(phrase)) ? phraseIndices
                    .get(phrase).size() : 0);
            }
            
            triePhrasesCount.put(e.getKey(), counts);
        }
        
        System.out.println("Total time for Trie: "
            + (System.currentTimeMillis() - curTime) + "ms.");
        
        this.phrasesCount = phrasesCount;
        
        // A check to make sure they both have same result (should return true):
        // System.out.println(triePhrasesCount.equals(phrasesCount));
        
        // Write the counts to a file with each document being on a new line,
        // seperated by a tab.
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
            rootDirectory, "Task One Counts.tab")))) {
            for (Map.Entry<File, List<Integer>> e : phrasesCount.entrySet()) {
                bw.append(phrasesList.size() + "\t");
                bw.append(e.getKey().getName());
                
                for (Integer count : e.getValue())
                    bw.append("\t" + count);
                
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void taskTwo() {
        final Map<File, Double> csMap = new HashMap<>();
        Queue<File> similarDocuments = new PriorityQueue<>(phrasesCount.size(),
            new Comparator<File>() {
                
                @Override
                public int compare(File f1, File f2) {
                    if (!csMap.containsKey(f1) || !csMap.containsKey(f2))
                        throw new NoSuchElementException();
                    
                    return Main.getSign(csMap.get(f2) - csMap.get(f1));
                }
            });
        
        for (Map.Entry<File, List<Integer>> e : phrasesCount.entrySet()) {
            csMap.clear();
            similarDocuments.clear();
            
            for (Map.Entry<File, List<Integer>> other : phrasesCount.entrySet()) {
                if (e.getKey().equals(other.getKey()))
                    continue;
                
                csMap.put(
                    other.getKey(),
                    AssignmentTasks.cosineSimilarity(e.getValue(),
                        other.getValue()));
                
                similarDocuments.add(other.getKey());
            }
            
            File f;
            System.out.println(e.getKey().getName());
            f = similarDocuments.poll();
            System.out.println("\t" + f.getName() + ": " + csMap.get(f));
            f = similarDocuments.poll();
            System.out.println("\t" + f.getName() + ": " + csMap.get(f));
            f = similarDocuments.poll();
            System.out.println("\t" + f.getName() + ": " + csMap.get(f));
            System.out.println();
        }
    }
    
    private boolean loadDocuments() {
        File dir = null;
        if (JOptionPane.showConfirmDialog(null,
            "Select root directory for text files.", "File Loading",
            JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
            dir = AssignmentTasks.dirSearchDialog();
        
        if (dir == null)
            return false;
        
        rootDirectory = dir;
        
        Map<File, String> documents = new HashMap<>();
        for (File txtFile : dir.listFiles(AssignmentTasks.TXT_FILTER)) {
            if (documents.containsKey(txtFile))
                continue;
            
            StringBuilder documentString = new StringBuilder();
            
            try (Scanner s = new Scanner(txtFile)) {
                if (s.hasNext())
                    documentString = new StringBuilder(s.next());
                
                while (s.hasNext())
                    documentString.append(" " + s.next());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            
            documents.put(txtFile, documentString.toString());
        }
        
        this.documents = documents;
        
        return true;
    }
    
    private boolean loadPhrases() {
        File phrases = null;
        if (JOptionPane.showConfirmDialog(null, "Select phrases file.",
            "File Loading", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
            phrases = AssignmentTasks.searchDialog();
        
        if (phrases == null)
            return false;
        
        List<String> phrasesList = new ArrayList<>();
        Trie phrasesTrie = new Trie();
        
        try (Scanner s = new Scanner(phrases)) {
            while (s.hasNextLine()) {
                String next = s.nextLine();
                
                if (next.length() == 0)
                    continue;
                
                phrasesList.add(next);
                phrasesTrie.add(next);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        this.phrasesList = phrasesList;
        this.phrasesTrie = phrasesTrie;
        
        return true;
    }
    
    private static double cosineSimilarity(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size())
            throw new IllegalArgumentException();
        
        // a . b
        double aXb = 0.0;
        // |a|
        double modA = 0.0;
        // |b|
        double modB = 0.0;
        
        for (int i = 0; i < a.size(); i++) {
            aXb += a.get(i) * b.get(i);
            modA += a.get(i) * a.get(i);
            modB += b.get(i) * b.get(i);
        }
        
        modA = Math.sqrt(modA);
        modB = Math.sqrt(modB);
        
        return aXb / (modA * modB);
    }
    
    private static File dirSearchDialog() {
        JFileChooser fc = new JFileChooser();
        
        fc.setCurrentDirectory(new File(Main.ROOT_PATH));
        
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            return fc.getSelectedFile();
        
        return null;
    }
    
    private static File searchDialog() {
        JFileChooser fc = new JFileChooser();
        
        fc.setCurrentDirectory(new File(Main.ROOT_PATH));
        
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            return fc.getSelectedFile();
        
        return null;
    }
}

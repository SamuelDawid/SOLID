package org.example.patterns.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasyczny Prototype z metodą copy() (czyli "copy constructor" jako statyczna metoda).
 * Klonowanie GŁĘBOKIE — sygnatariusze są nową listą.
 */
public class DocumentTemplate {

    private String title;
    private String body;
    private String author;
    private List<String> signatories;

    public DocumentTemplate(String title, String body, String author,
                            List<String> signatories) {
        this.title = title;
        this.body = body;
        this.author = author;
        this.signatories = new ArrayList<>(signatories);
    }

    /** Copy constructor — głęboka kopia. */
    public DocumentTemplate(DocumentTemplate other) {
        this.title  = other.title;
        this.body   = other.body;
        this.author = other.author;
        this.signatories = new ArrayList<>(other.signatories);   // głęboka kopia listy
    }

    /** Operacja prototypu — zwraca nową kopię z aktualnego stanu. */
    public DocumentTemplate copy() {
        return new DocumentTemplate(this);
    }

    // Settery — Prototype to wzorzec o klonowaniu, klasa może być mutowalna
    public void setTitle(String title)      { this.title = title; }
    public void setBody(String body)        { this.body = body; }
    public void setAuthor(String author)    { this.author = author; }
    public void addSignatory(String name)   { this.signatories.add(name); }

    public String getTitle()                { return title; }
    public String getBody()                 { return body; }
    public String getAuthor()               { return author; }
    public List<String> getSignatories()    { return signatories; }

    @Override public String toString() {
        return "Doc{title=%s, author=%s, sig=%s}".formatted(title, author, signatories);
    }
}

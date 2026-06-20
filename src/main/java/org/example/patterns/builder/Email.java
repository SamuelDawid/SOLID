package org.example.patterns.builder;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Klasyczny ręczny Builder.
 * - prywatny konstruktor → tylko Builder może utworzyć Email
 * - final fields → niemutowalność
 * - walidacja w konstruktorze (wywoływana z build())
 */
public class Email {
    private final String from;
    private final String to;
    private final String subject;
    private final String body;
    private final String cc;
    private final boolean html;
    private final List<String> attachments;

    private Email(Builder b) {
        // Wymagane — walidacja przez requireNonNull
        this.from    = Objects.requireNonNull(b.from,    "from jest wymagane");
        this.to      = Objects.requireNonNull(b.to,      "to jest wymagane");
        this.subject = Objects.requireNonNull(b.subject, "subject jest wymagane");

        // Opcjonalne — sensowne wartości domyślne
        this.body = b.body != null ? b.body : "";
        this.cc   = b.cc;
        this.html = b.html;

        // Defensywna kopia — zewnętrzna mutacja listy nie wpłynie na Email
        this.attachments = b.attachments != null
                ? List.copyOf(b.attachments)
                : List.of();
    }

    public String  getFrom()             { return from; }
    public String  getTo()               { return to; }
    public String  getSubject()          { return subject; }
    public String  getBody()             { return body; }
    public String  getCc()               { return cc; }
    public boolean isHtml()              { return html; }
    public List<String> getAttachments() { return attachments; }

    @Override public String toString() {
        return "Email{from=%s, to=%s, subject=%s, html=%s, attachments=%d}"
                .formatted(from, to, subject, html, attachments.size());
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String from;
        private String to;
        private String subject;
        private String body;
        private String cc;
        private boolean html = false;
        private List<String> attachments;

        public Builder from(String from)       { this.from = from;       return this; }
        public Builder to(String to)           { this.to = to;           return this; }
        public Builder subject(String subject) { this.subject = subject; return this; }
        public Builder body(String body)       { this.body = body;       return this; }
        public Builder cc(String cc)           { this.cc = cc;           return this; }
        public Builder html()                  { this.html = true;       return this; }

        public Builder attach(String path) {
            if (attachments == null) attachments = new ArrayList<>();
            attachments.add(path);
            return this;
        }

        public Email build() {
            return new Email(this);   // walidacja w konstruktorze
        }
    }
}

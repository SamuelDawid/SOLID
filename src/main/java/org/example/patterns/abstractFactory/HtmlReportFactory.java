package org.example.patterns.abstractFactory;

public class HtmlReportFactory implements ReportFactory {
    @Override public Header createHeader() { return new HtmlHeader(); }
    @Override public Body   createBody()   { return new HtmlBody(); }
    @Override public Footer createFooter() { return new HtmlFooter(); }
}

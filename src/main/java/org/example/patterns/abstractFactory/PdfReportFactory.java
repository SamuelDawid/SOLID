package org.example.patterns.abstractFactory;

public class PdfReportFactory implements ReportFactory {
    @Override public Header createHeader() { return new PdfHeader(); }
    @Override public Body   createBody()   { return new PdfBody(); }
    @Override public Footer createFooter() { return new PdfFooter(); }
}

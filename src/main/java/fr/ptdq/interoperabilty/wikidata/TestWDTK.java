/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ptdq.interoperabilty.wikidata;

import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author chris
 */
public class TestWDTK
{

    static final String extractPropertyId = "P227"; // "GND identifier"
    static final String filterPropertyId = "P31"; // "instance of"
    static final Value filterValue = Datamodel.makeWikidataItemIdValue("Q5"); // "human"

    int itemsWithPropertyCount = 0;
    int itemCount = 0;
    PrintStream out;

    /**
     * Main method. Processes the whole dump using this processor. To change
     * which dump file to use and whether to run in offline mode, modify the
     * settings in {@link ExampleHelpers}.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        ExampleHelpers.configureLogging();
        DataExtractionProcessor.printDocumentation();

        DataExtractionProcessor processor = new DataExtractionProcessor();
        //ExampleHelpers.processEntitiesFromWikidataDump(processor);
        processor.close();
    }

    public DataExtractionProcessor() throws IOException
    {
        // open file for writing results:
        out = new PrintStream(
                ExampleHelpers.openExampleFileOuputStream("extracted-data.csv"));
        // write CSV header:
        out.println("ID,Label (en),Label (de),Value,Wikipedia (en),Wikipedia (de)");
    }

    @Override
    public void processItemDocument(ItemDocument itemDocument)
    {
        this.itemCount++;

        // Check if the item matches our filter conditions:
        if (!itemDocument.hasStatementValue(filterPropertyId, filterValue))
        {
            return;
        }

        // Find the first value for this property, if any:
        StringValue stringValue = itemDocument
                .findStatementStringValue(extractPropertyId);

        // If a value was found, write the data:
        if (stringValue != null)
        {
            this.itemsWithPropertyCount++;
            out.print(itemDocument.getItemId().getId());
            out.print(",");
            out.print(csvEscape(itemDocument.findLabel("en")));
            out.print(",");
            out.print(csvEscape(itemDocument.findLabel("de")));
            out.print(",");
            out.print(csvEscape(stringValue.getString()));
            out.print(",");
            SiteLink enwiki = itemDocument.getSiteLinks().get("enwiki");
            if (enwiki != null)
            {
                out.print(csvEscape(enwiki.getPageTitle()));
            }
            else
            {
                out.print("\"\"");
            }
            out.print(",");
            SiteLink dewiki = itemDocument.getSiteLinks().get("dewiki");
            if (dewiki != null)
            {
                out.print(csvEscape(dewiki.getPageTitle()));
            }
            else
            {
                out.print("\"\"");
            }
            out.println();
        }

        // Print progress every 100,000 items:
        if (this.itemCount % 100000 == 0)
        {
            printStatus();
        }
    }

    @Override
    public void processPropertyDocument(PropertyDocument propertyDocument)
    {
        // Nothing to do
    }

    /**
     * Escapes a string for use in CSV. In particular, the string is quoted and
     * quotation marks are escaped.
     *
     * @param string the string to escape
     * @return the escaped string
     */
    private String csvEscape(String string)
    {
        if (string == null)
        {
            return "\"\"";
        }
        else
        {
            return "\"" + string.replace("\"", "\"\"") + "\"";
        }
    }

    /**
     * Prints the current status, time and entity count.
     */
    public void printStatus()
    {
        System.out.println("Found " + this.itemsWithPropertyCount
                + " matching items after scanning " + this.itemCount
                + " items.");
    }

    /**
     * Prints some basic documentation about this program.
     */
    public static void printDocumentation()
    {
        System.out
                .println("********************************************************************");
        System.out.println("*** Wikidata Toolkit: DataExtractionProcessor");
        System.out.println("*** ");
        System.out
                .println("*** This program will download and process dumps from Wikidata.");
        System.out
                .println("*** It will scan the dump to find items with values for property");
        System.out.println("*** " + extractPropertyId
                + " and print some data for these items to a CSV file. ");
        System.out.println("*** See source code for further details.");
        System.out
                .println("********************************************************************");
    }

    public void close()
    {
        printStatus();
        this.out.close();
    }
}

package step.extension.trace;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import step.framework.extensions.ExtensionException;
import step.framework.extensions.ExtensionListener;

/**
 *  This is the Trace extension listener.
 *  If properly configured, it is invoked on extension init and destroy.
 */
public class TraceExtensionListener extends ExtensionListener {

    /**
     *  Checks if the output file configuration option was specified.
     */
    public void extensionInitialized() throws ExtensionException
    {
        //
        //  output
        //
        String output = (String) getExtension().getProperty("output");
	    PrintStream outputStream =  null;
        boolean usingFile = false;

    	if (output != null)
    	{
    	    try
    	    {
        		outputStream = new PrintStream(output);
        	    System.out.println("Using custom output (file): \"" + output + "\"");
                usingFile = true;
    	    }
    	    catch (FileNotFoundException e)
    	    {
        		// outputStream will be null causing the default to be selected
        	    System.out.println("Output file not found!");
    	    }
    	}
        if(output == null || outputStream == null)
        {
    	    outputStream = System.out;
    	    System.out.println("Using default output: System.out");
    	}

        getExtension().setProperty("output.stream", outputStream);
        getExtension().setProperty("output.usingFile", new Boolean(usingFile));
    }

    /**
     *  Closes output file (if one is being used).
     */
    public void extensionDestroyed() throws ExtensionException
    {
        Boolean usingFile = (Boolean) getExtension().getProperty("output.usingFile");
        if(usingFile)
        {
            // close file
            PrintStream outputStream = (PrintStream) getExtension().getProperty("output.stream");
            outputStream.close();
      	    System.out.println("Output file closed");
        }

        getExtension().setProperty("output.stream", null);
        getExtension().setProperty("output.usingFile", null);
    }

}

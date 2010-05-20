package step.extension.trace;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Map;
import java.util.Properties;

import step.framework.oldextensions.ExtensionException;
import step.framework.oldextensions.ExtensionListener;
import step.framework.oldextensions.ExtensionListenerParameter;

/**
 *  This is the Trace extension listener.
 *  If properly configured, it is invoked on extension init and destroy.
 */
public class TraceExtensionListener implements ExtensionListener {

    /**
     *  Checks if the output file configuration option was specified.
     */
    public void extensionInitialized(ExtensionListenerParameter param)
    throws ExtensionException {

        // access the extension configuration and context
        Properties extConfig = param.getExtension().getConfig();
        Map<String,Object> extContext = param.getExtension().getContext();

        //
        //  output
        //
        String output = extConfig.getProperty("output");
	    PrintStream outputStream =  null;
        boolean usingFile = false;

    	if (output != null) {
    	    try {
        		outputStream = new PrintStream(output);
        	    System.out.println("Using custom output (file): \"" + output + "\"");
                usingFile = true;
    	    } catch (FileNotFoundException e) {
        		// outputStream will be null causing the default to be selected
        	    System.out.println("Output file not found!");
    	    }
    	}
        if(output == null || outputStream == null) {
    	    outputStream = System.out;
    	    System.out.println("Using default output: System.out");
    	}

        extContext.put("output", outputStream);
        extContext.put("output.usingFile", new Boolean(usingFile));
    }

    /**
     *  Closes output file (if one is being used).
     */
    public void extensionDestroyed(ExtensionListenerParameter param) throws ExtensionException {
        // access the extension context
        Map<String,Object> extContext = param.getExtension().getContext();

        Boolean usingFile = (Boolean) extContext.get("output.usingFile");
        if(usingFile) {
            // close file
            PrintStream outputStream = (PrintStream) extContext.get("output");
            outputStream.close();
      	    System.out.println("Output file closed");
        }

        extContext.remove("output");
        extContext.remove("output.usingFile");
    }

}

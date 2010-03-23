package step.extension.trace;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Map;
import java.util.Properties;

import step.framework.extensions.ExtensionException;
import step.framework.extensions.ExtensionListener;
import step.framework.extensions.ExtensionListenerParameter;

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

        // access the extension configuration
        Properties extConfig = param.getExtension().getConfig();

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

        // store the print stream in the extension's context
        Map<String,Object> extContext = param.getExtension().getContext();
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

/**
 *  Command option helper.
 */

import org.apache.commons.cli.*;

public class CommandHelper {

    static def SEED_OPT = "s";

    static def FILE_OPT = "f";

    static def MAX_COST_OPT = "Mc";
    static def MIN_COST_OPT = "mc";

    static def NR_OPT = "n";
    static def MAX_NR_OPT = "M";
    static def MIN_NR_OPT = "m";

    static def GROUP_OPT = "g";
    static def MAX_GROUP_OPT = "Mg";
    static def MIN_GROUP_OPT = "mg";

    static def DATE_OPT = "d";
    static def TIME_OPT = "t";

    static def PROFIT_OPT = "pft";


    //
    //  Create options
    //

    static def addSeedOption(options) {
        Option sOption = new Option(SEED_OPT, "seed",
            /* hasArg */ true, "Random number generator seed");
        sOption.setArgName("number");
        sOption.setArgs(1);
        options.addOption(sOption);
    }

    static def addFileOption(options) {
        Option dOption = new Option(FILE_OPT, "file",
            /* hasArg */ true, "Data file");
        dOption.setArgName("file");
        dOption.setArgs(1);
        options.addOption(dOption);
    }

    static def addMaxCostOption(options) {
        Option mOption = new Option(MAX_COST_OPT, "maxcost",
            /* hasArg */ true, "Maximum cost");
        mOption.setArgName("number");
        mOption.setArgs(1);
        options.addOption(mOption);
    }

    static def addNrOption(options) {
        Option nrOption = new Option(NR_OPT, "number",
            /* hasArg */ true, "Number");
        nrOption.setArgName("number");
        nrOption.setArgs(1);
        options.addOption(nrOption);
    }

    static def addMaxGroupOption(options) {
        Option mgOption = new Option(MAX_GROUP_OPT, "maxgroup",
            /* hasArg */ true, "Maximum size of group");
        mgOption.setArgName("number");
        mgOption.setArgs(1);
        options.addOption(mgOption);
    }

    static def addDateOption(options) {
        Option mgOption = new Option(DATE_OPT, "d",
            /* hasArg */ true, "Date");
        mgOption.setArgName("date YYYYMMDD");
        mgOption.setArgs(1);
        options.addOption(mgOption);
    }

    static def addProfitOption(options) {
        Option mgOption = new Option(PROFIT_OPT, "profit",
            /* hasArg */ true, "Maximum profit");
        mgOption.setArgName("decimal number");
        mgOption.setArgs(1);
        options.addOption(mgOption);
    }


    //
    //  Type initializers
    //

    static def initInteger(value, defaultValue) {
        if (value != null) {
            return new Integer(value);
        } else {
            return defaultValue;
        }
    }

    static def initLong(value, defaultValue) {
        if (value != null) {
            return new Long(value);
        } else {
            return defaultValue;
        }
    }

    static def initDouble(value, defaultValue) {
        if (value != null) {
            return new Double(value);
        } else {
            return defaultValue;
        }
    }

    static def initFile(value, defaultValue) {
        if (value != null) {
            return new File(value);
        } else {
            return defaultValue;
        }
    }

    static def initRandom(seed) {
        if(seed == null) {
            return new Random();
        } else {
            return new Random(seed);
        }
    }

}

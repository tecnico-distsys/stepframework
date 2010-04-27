/**
 *  Command option helper.
 */

import org.apache.commons.cli.*;

public class CommandHelper {

    static def INPUT_OPT = "i";
    static def OUTPUT_OPT = "o";
    static def INPUT_LOPT = "input";
    static def OUTPUT_LOPT = "output";

    static def SEED_OPT = "s";
    static def SEED_LOPT = "seed";

    static def FILE_OPT = "f";
    static def FILE_LOPT = "file";

    static def MAX_COST_OPT = "Mc";
    static def MIN_COST_OPT = "mc";
    static def MAX_COST_LOPT = "maxcost";
    static def MIN_COST_LOPT = "mincost";

    static def NR_OPT = "n";
    static def MAX_NR_OPT = "M";
    static def MIN_NR_OPT = "m";
    static def NR_LOPT = "number";
    static def MAX_NR_LOPT = "maxnumber";
    static def MIN_NR_LOPT = "minnumber";

    static def GROUP_OPT = "g";
    static def MAX_GROUP_OPT = "Mg";
    static def MIN_GROUP_OPT = "mg";
    static def GROUP_LOPT = "group";
    static def MAX_GROUP_LOPT = "maxgroup";
    static def MIN_GROUP_LOPT = "mingroup";

    static def DATE_OPT = "d";
    static def TIME_OPT = "t";
    static def DATE_LOPT = "date";
    static def TIME_LOPT = "time";

    static def PROFIT_OPT = "pft";
    static def PROFIT_LOPT = "profit";


    //
    //  Create options
    //

    static def buildInputOption() {
        Option iOption = new Option(INPUT_OPT, INPUT_LOPT,
            /* hasArg */ true, "input file");
        iOption.setArgName("file");
        iOption.setArgs(1);
        return iOption;
    }

    static def buildOutputOption() {
        Option oOption = new Option(OUTPUT_OPT, OUTPUT_LOPT,
            /* hasArg */ true, "output file");
        oOption.setArgName("file");
        oOption.setArgs(1);
        return oOption;
    }

    static def buildSeedOption() {
        Option sOption = new Option(SEED_OPT, SEED_LOPT,
            /* hasArg */ true, "Random number generator seed");
        sOption.setArgName("number");
        sOption.setArgs(1);
        return sOption;
    }

    static def buildFileOption() {
        Option dOption = new Option(FILE_OPT, FILE_LOPT,
            /* hasArg */ true, "Data file");
        dOption.setArgName("file");
        dOption.setArgs(1);
        return dOption;
    }

    static def buildMaxCostOption() {
        Option mOption = new Option(MAX_COST_OPT, MAX_COST_LOPT,
            /* hasArg */ true, "Maximum cost");
        mOption.setArgName("number");
        mOption.setArgs(1);
        return mOption;
    }

    static def buildNrOption() {
        Option nrOption = new Option(NR_OPT, NR_LOPT,
            /* hasArg */ true, "Number");
        nrOption.setArgName("number");
        nrOption.setArgs(1);
        return nrOption;
    }

    static def buildMaxGroupOption() {
        Option mgOption = new Option(MAX_GROUP_OPT, MAX_GROUP_LOPT,
            /* hasArg */ true, "Maximum size of group");
        mgOption.setArgName("number");
        mgOption.setArgs(1);
        return mgOption;
    }

    static def buildDateOption() {
        Option mgOption = new Option(DATE_OPT, DATE_LOPT,
            /* hasArg */ true, "Date");
        mgOption.setArgName("date YYYYMMDD");
        mgOption.setArgs(1);
        return mgOption;
    }

    static def buildProfitOption() {
        Option mgOption = new Option(PROFIT_OPT, PROFIT_LOPT,
            /* hasArg */ true, "Maximum profit");
        mgOption.setArgName("decimal number");
        mgOption.setArgs(1);
        return mgOption;
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

/**
 *  CSV helper.
 */

import org.supercsv.prefs.*;

class CSVHelper {

    static def getRequestRecordsHeaderList() {
        def headerList = ["filter", "soap", "soap-meta", "wsi", "si", "hibernate"];
        return headerList;
    }

    static def headerListToArray(headerList) {
        String[] headerArray = new String[headerList.size];
        for (int i = 0; i < headerArray.length; i++) {
            headerArray[i] = headerList[i];
        }
        return headerArray;
    }

}

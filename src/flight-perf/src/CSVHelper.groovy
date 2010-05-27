/**
 *  CSV helper.
 */

import org.supercsv.prefs.*;

class CSVHelper {

    static def getRequestRecordsHeaderList() {
        def headerList = ["filter_time",
                          "soap_time", "soap_name",
                          "soap_request_logical_length", "soap_request_node_count", "soap_request_max_depth",
                          "soap_response_logical_length", "soap_response_node_count", "soap_response_max_depth",
                          "wsi_time",
                          "si_time", "si_name",
                          "hibernate_read_time", "hibernate_write_time"];
        return headerList;
    }

}

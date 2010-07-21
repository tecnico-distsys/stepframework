package org.tripplanner.flight.perf.helper;

import org.supercsv.prefs.*;

/**
 *  CSV helper.
 */
class CSVHelper {

    static def getRequestRecordHeaderList() {
        def headerList = ["filter_time",
                          "soap_time", "soap_name",
                          "soap_request_logical_length", "soap_request_node_count", "soap_request_max_depth",
                          "soap_response_logical_length", "soap_response_node_count", "soap_response_max_depth",
                          "wsi_time",
                          "si_time", "si_name",
                          "hibernate_time", "hibernate_read_time", "hibernate_write_time"];
        return headerList;
    }

    static def getRequestRecordNumericHeaderList() {
        def requestRecordHeaderList = getRequestRecordHeaderList();
        def headerList = [ ];

        requestRecordHeaderList.each{ header ->
            if (header.endsWith("_time") || header.endsWith("_length") ||
                header.endsWith("_depth") || header.endsWith("_count")) {
                headerList.add(header);
            }
        }

        return headerList;
    }

    static def getSampleStatisticsHeaderList() {
        def requestRecordNumericHeaderList = getRequestRecordNumericHeaderList();
        def headerList = [ ];

        requestRecordNumericHeaderList.each { header ->
            headerList.add(header + "-mean");
            headerList.add(header + "-stdDev");
            headerList.add(header + "-median");
            headerList.add(header + "-lowerQ");
            headerList.add(header + "-upperQ");
        }

        return headerList;
    }

    static def getOverallStatisticsHeaderList() {
        def requestRecordNumericHeaderList = getRequestRecordNumericHeaderList();
        def headerList = [ ];

        requestRecordNumericHeaderList.each { header ->
            headerList.add(header + "-mean");
            headerList.add(header + "-mean-error-90pctconf");
            headerList.add(header + "-mean-error-95pctconf");
            headerList.add(header + "-mean-error-99pctconf");
            headerList.add(header + "-stdDev");
        }

        return headerList;
    }

    static def getVirtualUserOutputSampleCountsHeaderList() {
        def headerList = [
            "request-searchFlights",
            "request-createSingleReservation",
            "request-createMultipleReservations",
            "exception-FlightFault_Exception",
            "exception-ServiceUnavailable_Exception"
        ];

        return headerList;
    }

    static def getVirtualUserOutputOverallCountsHeaderList() {
        def virtualUserOutputSampleCountsHeaderList = getVirtualUserOutputSampleCountsHeaderList();
        def headerList = [ ];

        virtualUserOutputSampleCountsHeaderList.each { header ->
            headerList.add(header + "-mean");
            headerList.add(header + "-mean-error-90pctconf");
            headerList.add(header + "-mean-error-95pctconf");
            headerList.add(header + "-mean-error-99pctconf");
            headerList.add(header + "-stdDev");
        }

        return headerList;
    }

}

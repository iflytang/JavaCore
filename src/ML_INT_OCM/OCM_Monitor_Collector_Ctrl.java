package src.ML_INT_OCM;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tsf
 * @date 20-7-7
 * @desp dynamically adjustable periodical request OCM data.
 *       besides, we leverage int-collector's data to adjust the paramters,
 *       i.e., <request_frequency, request_slice_precision>
 */

public class OCM_Monitor_Collector_Ctrl {
    /* ocm server socket info <ip_addr, port> */
    private static final String SER_OCM_ADDR = "192.168.108.126";
    private static final int SER_OCM_PORT = 9999;

    /* collector server socket info  <ip_addr, port>, also known as DA (data analyzer) */
    private static final String SER_DA_ADDR = "192.168.109.221";
    private static final int SER_DA_PORT = 2020;

    /* ocm client socket for communicating with OCM. */
    private static Socket OCMSock;
    private static InputStream inStrm_OCM;
    private static InputStreamReader inStrmRd_OCM;
    private static BufferedInputStream bufInput_OCM;
    private static BufferedReader bufRd_OCM;
    private static OutputStream outStrm_OCM;
    private static PrintWriter prtwt_OCM;

    /* collector client socket for communicating with collector. */
    private static Socket DASock;
    private static InputStream inStrm_DA;
    private static InputStreamReader inStrmRd_DA;
    private static BufferedInputStream bufInput_DA;
    private static BufferedReader bufRd_DA;
    private static OutputStream outStrm_DA;
    private static PrintWriter prtwt_DA;

    public static String getSerOcmAddr() {
        return SER_OCM_ADDR;
    }

    public static int getSerOcmPort() {
        return SER_OCM_PORT;
    }

    public static Socket getOCMSock() {
        return OCMSock;
    }

    public static InputStream getInStrm_OCM() {
        return inStrm_OCM;
    }

    public static InputStreamReader getInStrmRd_OCM() {
        return inStrmRd_OCM;
    }

    public static BufferedInputStream getBufInput_OCM() {
        return bufInput_OCM;
    }

    public static BufferedReader getBufRd_OCM() {
        return bufRd_OCM;
    }

    public static OutputStream getOutStrm_OCM() {
        return outStrm_OCM;
    }

    public static PrintWriter getPrtwt_OCM() {
        return prtwt_OCM;
    }

    public static String getSerDaAddr() {
        return SER_DA_ADDR;
    }

    public static int getSerDaPort() {
        return SER_DA_PORT;
    }

    public static Socket getDASock() {
        return DASock;
    }

    public static InputStream getInStrm_DA() {
        return inStrm_DA;
    }

    public static InputStreamReader getInStrmRd_DA() {
        return inStrmRd_DA;
    }

    public static BufferedInputStream getBufInput_DA() {
        return bufInput_DA;
    }

    public static BufferedReader getBufRd_DA() {
        return bufRd_DA;
    }

    public static OutputStream getOutStrm_DA() {
        return outStrm_DA;
    }

    public static PrintWriter getPrtwt_DA() {
        return prtwt_DA;
    }

    /* set tcp socket connection to OCMSock. */
    public static void setupSocketToOcm(){
        try{
            OCMSock = new Socket(SER_OCM_ADDR, SER_OCM_PORT);  // build socket object and setup connection
            inStrm_OCM = OCMSock.getInputStream();
            inStrmRd_OCM = new InputStreamReader(inStrm_OCM);
            bufInput_OCM = new BufferedInputStream(inStrm_OCM);
            bufRd_OCM = new BufferedReader(inStrmRd_OCM);
            outStrm_OCM = OCMSock.getOutputStream();
            prtwt_OCM = new PrintWriter(outStrm_OCM);
            System.out.println("OCMSock: " + OCMSock);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /* set tcp socket connection to DASock. */
    public static void setupSocketToDA(){
        try{
            DASock = new Socket(SER_DA_ADDR, SER_DA_PORT);  // build socket object and setup connection
            inStrm_DA = DASock.getInputStream();
            inStrmRd_DA = new InputStreamReader(inStrm_DA);
            bufInput_DA = new BufferedInputStream(inStrm_DA);
            bufRd_DA = new BufferedReader(inStrmRd_DA);
            outStrm_DA = DASock.getOutputStream();
            prtwt_DA = new PrintWriter(outStrm_DA);
            System.out.println("DASock: " + DASock);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void teardownOcmSocket() {
        try {
            bufRd_OCM.close();
            inStrm_OCM.close();
            prtwt_OCM.close();
            OCMSock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void teardownDaSocket() {
        try {
            bufRd_DA.close();
            inStrm_DA.close();
            prtwt_DA.close();
            DASock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String construct_ocm_conf(double start_freq, double watchWindow, double slice) {
       String ocm_conf = Double.toString(start_freq)+" "+Double.toString(watchWindow)+" "+Double.toString(slice);
       return ocm_conf;
    }

    public static void main (String[] args) {
        double start_freq = 192.475;   // THz, central wavelength
        double watchWindow = 0.05;     // THz

        double min_slice = 0.0003125;
        double max_slice = 0.05;
        double slice = max_slice;  // 0.0003125 0.05

        String file_prefix = "src/ML_INT_OCM/";
        String file_name = file_prefix + "result.dat";

        try {
            setupSocketToOcm();
            setupSocketToDA();

            /* request ocm data from ocm agent. */
            Ocm_Monitor_Thread ocm_thread = new Ocm_Monitor_Thread(file_name, watchWindow, slice);
            ocm_thread.start();

            /* receive 'ber' data and reconfigure 'ocm_conf' */
            Collector_Ctrl_Thread da_thread = new Collector_Ctrl_Thread(ocm_thread);
            da_thread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

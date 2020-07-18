package src.ML_INT_OCM;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class OCM_util {
    public static byte[] charArr2ByteArr(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

    public static double bytes2Double(byte[] arr, int k){
        long value=0;
        for(int i=0;i< 8;i++){
            value|=((long)(arr[k]&0xff))<<(8*i);
            k++;
        }
        return Double.longBitsToDouble(value);
    }

    public static String construct_ocm_conf(double start_freq, double watchWindow, double slice) {
        String ocm_conf = Double.toString(start_freq)+" "+Double.toString(watchWindow)+" "+Double.toString(slice);
        return ocm_conf;
    }

    /* get ber's abs of magnitude order, for example, ber = 6.515497334070864e-09, then order is 9. */
    public static int get_order_of_ber(double ber) {
        return (int) Math.ceil(Math.abs(Math.log10(ber)));
    }
}

package example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;

import example.BarcodeScanner;

public class BarcodeScannerTest {

	@Test
	public void test() throws NotFoundException, ChecksumException, FormatException, IOException {
		
		BarcodeScanner bs = new BarcodeScanner();
		String b = bs.readBarcodes("src/test/resources/test_bad_prep.png");
		System.out.println("b:" + b);
		assertNull(b);
		
		BarcodeScanner bs2 = new BarcodeScanner();
		String b2 = bs2.readBarcodes("src/test/resources/test_ok_prep.png");
		System.out.println("b2:" + b2);
		assertEquals("TOFA001581", b2);
	}

}

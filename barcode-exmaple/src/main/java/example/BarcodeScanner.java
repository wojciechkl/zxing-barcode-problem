package example;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.oned.Code128Reader;

public class BarcodeScanner {
	final static Logger logger = LoggerFactory.getLogger(BarcodeScanner.class);
		
	public String readBarcodes(String filePath) throws IOException, NotFoundException, ChecksumException, FormatException {
		System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
		
		List<String> res = new ArrayList<String>();
		BufferedImage bim = ImageIO.read(new File(filePath));			
		return processPage(0, res, bim);
	}

	private String processPage(int page, List<String> res, BufferedImage bim) throws NotFoundException {
		LuminanceSource source = new BufferedImageLuminanceSource(bim);
	    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

	    Reader reader = new Code128Reader();
	    
	    Map<DecodeHintType,Object> tmpHintsMap = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
        tmpHintsMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        tmpHintsMap.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);
        
        GenericMultipleBarcodeReader greader = new GenericMultipleBarcodeReader(reader);
        
        try {
        	Result [] results = greader.decodeMultiple(bitmap,tmpHintsMap);
		    for(Result r: results) {
		    	return r.getText();
		    }
        }
        catch(NotFoundException nf) {
        	logger.info("No barcodes found (zxing exception)");
        }
        return null;
	}

	public void dumpImage(Path fp, BufferedImage bim) throws IOException {
		Path image = fp.getParent().resolve("images").resolve(fp.getFileName() + ".png");
		ImageIO.write(bim, "png", image.toFile());
	}
}

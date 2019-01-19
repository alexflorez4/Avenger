package com.shades.services.misc;

import com.shades.dao.InventoryDao;
import net.odcorp.exception.CheckedContentException;
import net.odcorp.util.flatfile.rowdata.RowDataParser;
import net.odcorp.util.flatfile.rowdata._RowDataConstants;
import net.odcorp.util.flatfile.rowdata.xmlserializer.FlatFileXmlSerializer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.InputSource;

import java.io.File;
import java.util.Map;

@Transactional
@Service
public class FileServices {

    private static final Logger logger = Logger.getLogger(FileServices.class);

    @Autowired
    private InventoryDao inventoryDao;

    public void updateInvoiceToAccount(File invoice) throws Exception {
        InputSource is = FlatFileXmlSerializer.getFlatFileAsXMLInputSource(invoice,null, _RowDataConstants.RowType.ROWS_WITH_ATTRIBUTES);
        RowDataParser p = new RowDataParser();
        p.parse(is);

        for(Map<String, String> row : p.getRowData()){
            System.out.println(row.get("Order") + " " +  row.get("Total"));
        }
    }

}

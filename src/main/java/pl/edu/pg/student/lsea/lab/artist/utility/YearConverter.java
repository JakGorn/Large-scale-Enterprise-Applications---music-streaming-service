package pl.edu.pg.student.lsea.lab.artist.utility;

import java.time.Year;
import java.util.logging.Logger;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter class for Year type.
 * @author Jakub Górniak
 */
@Converter(autoApply = true)
public class YearConverter implements AttributeConverter<Year, Short> {
     
    Logger log = Logger.getLogger(YearConverter.class.getSimpleName());
 
    @Override
    public Short convertToDatabaseColumn(Year attribute) {
    	if(attribute == null)
    	{
    		return null;
    	}
        short year = (short) attribute.getValue();
        log.info("Convert Year ["+attribute+"] to short ["+year+"]");
        return year;
    }
 
    @Override
    public Year convertToEntityAttribute(Short dbValue) {
    	if(dbValue == null)
    	{
    		return null;
    	}
        Year year = Year.of(dbValue);
        log.info("Convert Short ["+dbValue+"] to Year ["+year+"]");
        return year;
    }
}
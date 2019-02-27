import java.io.IOException; // Needed for the IOException
import java.io.UnsupportedEncodingException; // Needed for the UnsupportedEncodingException
/**
 * This is a class that executes the tool that formats the code to have spacing lines around the brackets.
 *
 * @author Nikita Tsyganov 3104954
 * @version February 20, 2019
 */
public class CleanCodeFormatterMain {

	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		
		CleanCodeFormatter cleanCodeFormatter = new CleanCodeFormatter();
		// Initialize the process
		cleanCodeFormatter.processFile();

	}

}
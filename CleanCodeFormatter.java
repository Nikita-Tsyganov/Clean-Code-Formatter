import java.util.Scanner; // Needed for the Scanner class
import java.util.regex.Pattern;
import java.io.BufferedWriter; // Needed for the BufferedWriter class
import java.io.File; // Needed for the File class
import java.io.FileOutputStream; // Needed for the FileOutputStream class
import java.io.IOException; // Needed for the IOException
import java.io.OutputStreamWriter; // Needed for the OutputStreamWriter class
import java.io.Writer; // Needed for the Writer class
/**
 * This is a tool class that formats the code to have spacing lines around the brackets.
 *
 * @author Nikita Tsyganov 3104954
 * @version February 20, 2019
 */
public class CleanCodeFormatter {
	
	private String inputFile = "input.txt";
	private String outputFile = "output.txt";
	private String previousLine = "";
	private String line = "";
	private String nextLine = "";
	private String token = "";
	private boolean isPreviousLineEmpty = false;
	private boolean wasNewLinePrinted = false;
	private int nest = 0;
	private int index = 0;
	
	public CleanCodeFormatter() {}
	
	public CleanCodeFormatter(String inputFile) {
		
		this.inputFile = inputFile;
		
	}
	
	public CleanCodeFormatter(String inputFile, String outputFile) {
		
		this(inputFile);
		this.outputFile = outputFile;
		
	}

	/**
	 * The extractMonthlyAveragePrices method loops over the "1994_Weekly_Gas_Averages.txt" file line by line,
	 * calculates the monthly average prices and stores them in an array which is returned upon completion.
	 * @return the array with the monthly average prices.
	 * @throws IOException 
	 */
	public void processFile() throws IOException {

        // Specify the file to read
        Scanner f = new Scanner(new File(this.inputFile), "UTF-8");
        
        // Create a writer to create and write into a file
  		Writer writer = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(this.outputFile), "UTF-8") );
  		
  		if ( f.hasNext() ) line = f.nextLine();
  		processLine();
  		previousLine = line;
  		if ( f.hasNext() ) line = f.nextLine();
  		if ( f.hasNext() ) nextLine = f.nextLine();
  		
  		writer.write(previousLine + "\n");
  		
        //
        do {
        	
        	processLine();
        	writer.write(line + "\n");
        	previousLine = line;
        	line = nextLine;
        	if ( f.hasNext() ) nextLine = f.nextLine();

        } while ( f.hasNext() );
        
        processLine();
    	writer.write(line + "\n");
    	line = nextLine;
    	processLine();
    	writer.write(line);

 		writer.close();

    }
	
	public void processLine() {
		
		if ( isLineEmpty(line) ) {
			
			return;
			
		}
		
		formatBracketsSpacing();
		
		if ( isPreviousLineEmpty() && isNextLineEmpty() ) {
			
			formatDoubleParenthesis();
			return;
			
		}
		
		wasNewLinePrinted = false;
		Scanner s = new Scanner(line);
    	
    	while ( s.hasNext() ) {
    		
    		token = s.next();
    			
			if ( isClosingBracket() || isOpeningBracket() ) {
				
				addEmptyLines();
				break;
				
			}
    		       		
    	}
    	
    	formatDoubleParenthesis();
    	
    	isPreviousLineEmpty = wasNewLinePrinted;
		
	}
	
	public void addEmptyLines() {
		
		if ( isPreviousLineEmpty() ) {
			
			line = line + "\n";
			wasNewLinePrinted = true;
			
		} else if ( isNextLineEmpty() ) {
			
			line = "\n" + line;
			
		} else {
			
			line = "\n" + line + "\n";
			wasNewLinePrinted = true;
			
		}
		
	}
	
	public boolean isPreviousLineEmpty() {
		
		return isPreviousLineEmpty || isLineEmpty(previousLine);
		
	}
	
	public boolean isNextLineEmpty() {
		
		return isLineEmpty(nextLine);
		
	}
	
	public boolean isLineEmpty(String line) {
		
		return line.trim().isEmpty();
		
	}
	
	public void formatBracketsSpacing() {
		
		line =  line.replace("if(", "if (")
				.replace("for(", "for (")
				.replace("while(", "while (")
				.replace("){", ") {");
		
	}
	
	public void formatDoubleParenthesis() {
		
		int doubleParenthesisIndex = line.lastIndexOf("))");
		
		if (doubleParenthesisIndex == -1) {
			
			//line = line.replaceAll(Pattern.quote("(("), "( (");
			nest = 0;
			return;
			
		}
		
		line = replaceLast(line, "))", ") )");
		line = replaceNthLast(line, "(", "( ", 2 + nest++);
		//line = replaceLast(line, "((", "( (");
		//line = replaceNth(line, ")", " )", 2 + nest++);
		
		formatDoubleParenthesis();
		
	}
	
	public boolean isClosingBracket() {
		
		return token.equals("}")
				|| token.equals("};")
				|| token.equals(");")
				|| token.equals("];")
				|| token.equals("});");
		
	}
	
	public boolean isOpeningBracket() {
		
		return token.equals("{")
				|| line.endsWith("(")
				|| line.endsWith("[");
				
	}
	
	public String replaceLast(String string, String regex, String replacement) {
		
        return string.replaceFirst("(?s)(.*)" + Pattern.quote(regex), "$1" + replacement);
        
    }
	
	public String replaceNth(String string, String oldStr, String newStr, int n) {
		
		int index = string.indexOf(oldStr);
		
		for (int i = 2; i <= n; i++) {
			
			index = string.indexOf(oldStr, index + 1);
			
		}
		
		return string.substring(0, index) + newStr + string.substring(index + 1);
        
    }
	
	public String replaceNthLast(String string, String oldStr, String newStr, int n) {
		
		int index = string.lastIndexOf(oldStr);
		
		for (int i = 2; i <= n; i++) {
			
			index = string.lastIndexOf(oldStr, index - 1);
			
		}
		
		return string.substring(0, index) + newStr + string.substring(index + 1);
        
    }
	
	/*public String replaceNthLast(String string, String regex, String replacement, int n) {
		
		regex = Pattern.quote(regex);
		String nthRegex = "";
		String nthSubstitution = "";
		
		for (int i = 2; i <= n; i++) {
			
			System.out.println(n);
			nthRegex += "(.*" + regex + ").*";
			nthSubstitution += "$" + (i + 2);
			
		}
		
        String result =  string.replaceFirst("(^.*)(" + regex + ")" + nthRegex + "(.*)$", "$1" + replacement + "$3" + nthSubstitution);
        System.out.println(result);
        return result;
        
    }*/
	
}
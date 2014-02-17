package jsonCsv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import model.GeoPosition;
import model.Item;
import model.SearchResult;

import org.eclipse.persistence.oxm.MediaType;

import au.com.bytecode.opencsv.CSVWriter;

public class Main {

	// Constants
	private static final String END_POINT_URL = "https://api.goeuro.com/api/v1/suggest/position/en/name/";
	private static final Integer MAX_STRING_LENGTH = 1000;
	// It could be improved if you want to specify the path and the file name 
	private static final File FILE_OUTPUT = new File("jsonToCsv.csv");
	
	public static void main(String[] args) throws IOException {

		if (!checkArgs(args))
			return;
		
		String parameter = args[0];

		// Workaround for SSL
		// I should install the certificate in my keystore
		disableCertificateValidation();

		Reader in = null;
		String endPoint = END_POINT_URL + URLEncoder.encode(parameter, "UTF-8").replace("+", "%20");

		try {

			URL url = new URL(endPoint);
			URLConnection conn = url.openConnection();
			
			System.out.println("INFO: Connecting to: "+endPoint);
			
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put("eclipselink.media-type", MediaType.APPLICATION_JSON);
			properties.put("eclipselink.json.include-root", false);
			in = new InputStreamReader(conn.getInputStream(),"UTF-8");
			JAXBContext jc = JAXBContext.newInstance(new Class[]{SearchResult.class, Item.class, GeoPosition.class}, properties); 
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			
			JAXBElement<SearchResult> element = unmarshaller.unmarshal(new StreamSource(in),SearchResult.class);
			
			if (element.getValue()!=null){
				Integer size = element.getValue().getResults().size();
				if (size>0){
					System.out.println("INFO: Result List size: "+size);
					writeInCsv(element.getValue().getRecords());
				}
				else{
					System.out.println("INFO: Result List is empty! ");
					System.out.println("WARNING: No file has been created!");
				}
			}
			else
				System.out.println("INFO: No Result list and file has been created");
		} catch (MalformedURLException e) {
			System.err.println(e.getMessage());
			return;
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: No JSON reply from: " + endPoint);
			return;
		} catch (IOException e) {
			System.err.println("ERROR: IOException "+e.getMessage());
			return;
		} catch (JAXBException e) {
			System.err.println("ERROR: JAXBException "+e.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.err.println(e.getMessage());
					System.err.println("ERROR: Unable to close InputStream");
				}
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void writeInCsv(final List<String[]> records) throws IOException{

			if (FILE_OUTPUT.exists())
				System.out.println("INFO: Csv result file will be replaced");
			else
				System.out.println("INFO: New file: "+FILE_OUTPUT.toString()+" will be created");
			
			AccessController.
	          doPrivileged(new PrivilegedAction() {
	            public Object run() {
	                try { 
	                	Writer writer;
	        			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_OUTPUT), "UTF-8"));
	        	
	        			CSVWriter csvWriter = new CSVWriter(writer);
	        			
	        			csvWriter.writeAll(records);
	        			
	        			csvWriter.close();
	        			
	        			System.out.println("OK! Result file: "+FILE_OUTPUT.getAbsolutePath());
	        			
	                } catch(IOException ioe) {
	                    System.err.println(ioe.getMessage());
	                }
	                return null;
	            }
	        });
			
	}
	
	private static void disableCertificateValidation() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
			}
		} };

		// Ignore differences between given hostname and certificate hostname
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); 
	}

	public static boolean isStringWithSpace(String str) {
		return str.matches("^([A-Za-z ])*$");
												
	}

	public static boolean checkArgs(String[] args) {
		if (args.length > 1) {
			System.err
					.println("ERROR: Too many arguments. Only one argument is required");
			return false;
		}
		if (args.length == 0) {
			System.err.println("USAGE: java -jar GoEuroTest.jar \"STRING\"");
			return false;
		}
		if (args[0].length() > MAX_STRING_LENGTH) {
			System.err
					.println("ERROR: Too many string characters. Characters limit: "
							+ MAX_STRING_LENGTH);
			return false;
		}
		if (isNumeric(args[0])) {
			System.err.println("ERROR: Only Number is not allowed");
			return false;
		}
		if (!isStringWithSpace(args[0])) {
			System.err.println("ERROR: Only string with spaces is allowed and without numbers");
			return false;
		}
		return true;
	}

}

package dummy;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
public class APITest {
	
	
	@Test
	public void updateXML() throws Exception
	{
		String inputFile = "C:\\Users\\user\\Desktop\\nagesh\\a.xml";
		String outputFile = "C:\\Users\\user\\Desktop\\nagesh\\new_details.xml";
		Document doc = DocumentBuilderFactory.newInstance()
		        .newDocumentBuilder().parse(new InputSource(inputFile));

		    // locate the node(s)
		    XPath xpath = XPathFactory.newInstance().newXPath();
		    NodeList nodes = (NodeList)xpath.evaluate
		        ("//employee/*[text()='Nagesh']", doc, XPathConstants.NODESET);

		    // make changes to the element present at first
		    nodes.item(0).setTextContent("Anirudh");

		    // save the result
		    Transformer xformer = TransformerFactory.newInstance().newTransformer();
		    xformer.transform(new DOMSource(doc), new StreamResult(new File(outputFile)));
	}
	
	@Test
	public void updateJson() throws Exception
	{
		String newfile ="C:\\Users\\user\\Desktop\\nagesh\\edited-json.json";
		String oldfile ="C:\\Users\\user\\Desktop\\nagesh\\sample-json.json";
		
		// parsing file "JSONExample.json" 
        Object obj = new JSONParser().parse(new FileReader(oldfile)); 
          
        // type casting obj to JSONObject 
        JSONObject jo = (JSONObject) obj; 
          
        // getting firstName and lastName 
        String firstName = (String) jo.get("firstname"); 
        String lastName = (String) jo.get("lastname"); 
          
        System.out.println("Firsts name : "+firstName +"Last Name : "+lastName); 
        // change the last name
        jo.put("lastname", "Anirudh");
        System.out.println(jo.get("lastname"));
        
        PrintWriter pw = new PrintWriter(newfile); 
        pw.write(jo.toJSONString()); 
          
        pw.flush(); 
        pw.close();
        
     // parsing file "JSONExample.json" 
        Object objVerification = new JSONParser().parse(new FileReader("C:\\Users\\user\\Desktop\\nagesh\\edited-json.json")); 
          
        // type casting obj to JSONObject 
        JSONObject joVerification = (JSONObject) obj; 
          
        // verify the change
        Assert.assertEquals((String) jo.get("lastname"), "Anirudh");
	}
	
	
	@Test
	public void getMethod()
	{
		int userID = 1;
		// request the server
		Response response = RestAssured.get("https://jsonplaceholder.typicode.com/posts/"+userID);
		// store the response body in string
		String responseBody = response.getBody().asString();
		Assert.assertEquals(200, response.getStatusCode());
		System.out.println(responseBody);
		String userIdActual = response.jsonPath().getString("userId");
		Assert.assertEquals(userID, Integer.parseInt(userIdActual));
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void postMethod()
	{
		// request the server
		RequestSpecification reqSpec = RestAssured.given();

		JSONObject jo = new JSONObject();
		jo.put("title", "foo");
		jo.put("body", "bar");
		jo.put("userId", "101");
		reqSpec.body(jo.toString());
		reqSpec.contentType("application/json; charset=UTF-8");
		
		Response resp = reqSpec.post("https://jsonplaceholder.typicode.com/posts");
		SoftAssert soft = new SoftAssert();
		
		soft.assertEquals(201, resp.getStatusCode());
		
		// Note: After this we should write verification for what we have created
		// but the web site says"Note: the resource will not be really created on the server but it will be faked as if."
		// so if we write verification it will fail
		
		// TODO
		soft.assertAll();
		// assert all is used to verify even other verification is failed in script
	}
}
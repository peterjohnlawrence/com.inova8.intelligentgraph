package olgapTest;

import java.util.List;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.spin.SpinSail;

@SuppressWarnings("deprecation")
public class ShortestPathTest {
	public static void main(String[] args) {

		Repository workingRep = new SailRepository(new SpinSail(new MemoryStore()));
		//Repository workingRep = new SailRepository(new SpinSail(new NativeStore()));
		workingRep.init();
		try (RepositoryConnection conn = workingRep.getConnection();
				RepositoryConnection workingConn = workingRep.getConnection();) {

			String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>  \n";
			queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
			//queryString += "SELECT   * \n";
			//queryString += "WHERE { \n";
			//queryString += "   BIND( \"http://localhost:8082/rdf4j-server/repositories/tfl\" as ?service)";
			//queryString += "   (?edge ?subject ?property ?direct ?object )  olgap:shortestPathProperty   (?service   "
			//		+ "<http://in4mium.com/londontube/id/Mornington_Crescent>  <http://in4mium.com/londontube/id/Burnt_Oak> "
			//		+ "<http://in4mium.com/londontube/id/Mornington_Crescent>  <http://in4mium.com/londontube/id/Baker_Street> "
			//		+ "\" (<http://in4mium.com/londontube/ref/connectsToxx>|<http://in4mium.com/londontube/ref/onLinexx>)!(rdf:type)^(<http://in4mium.com/londontube/ref/connectsFrom>|  <http://in4mium.com/londontube/ref/hasStationOnLinexx>)^!(rdf:type)\" "
			//		+  "\"!(rdf:type)(<http://in4mium.com/londontube/ref/connectsTo>)!^(rdf:type)^(<http://in4mium.com/londontube/ref/connectsFrom>| <http://in4mium.com/londontube/ref/hasStationInZone>)\""
			//		+ " 8 )  .";	
			
			queryString += "PREFIX londontube: <http://in4mium.com/londontube/id/> \n"
			+	"PREFIX  tfl: <http://in4mium.com/londontube/ref/> \n"
			+ "  SELECT ?subject ?property ?object ?direct  ?edge \n" //?service  ?start  ?end ?propertyPath ?maxPath\n" 
			+ "WHERE {\n"
			+ "  BIND( <http://localhost:8080/rdf4j-server/repositories/tfl> as ?service)\n"
			+ "	 BIND( <http://in4mium.com/londontube/id/Mornington_Crescent> as ?start)\n"
			+ "	 BIND( <http://in4mium.com/londontube/id/Oakleigh_Park> as ?end)\n"
			+ "	 BIND( \"!(rdf:type|<http://in4mium.com/londontube/ref/connectsTo>)^!(rdf:type|<http://in4mium.com/londontube/ref/hasStationInZone>|<http://in4mium.com/londontube/ref/hasStationOnLine>|<http://in4mium.com/londontube/ref/connectsFrom>)\" as ?propertyPath)\n"
			+ "	 BIND( 20 as ?maxPath)\n"
			+ "	(?edge ?subject ?property ?direct ?object )  olgap:shortestPath   ( ?service  ?start  ?end ?propertyPath ?maxPath )  .\n"
			+ "}";

//			queryString += "PREFIX northwind: <http://northwind.com/model/> \n"
//			+	"PREFIX  NWD: <http://northwind.com/> \n"
//			+ "SELECT ?subject ?property ?object ?direct  ?edge \r\n" + "WHERE {\r\n"
//			+ "     BIND( <http://localhost:8082/rdf4j-server/repositories/northwind> as ?service)\r\n"
//			+ "	 BIND( <http://northwind.com/Customer-PRINI> as ?start)\r\n"
//			+ "	 BIND( <http://northwind.com/Customer-FURIB> as ?end)\r\n"
//			+ "	 BIND( \"!(rdf:type)^!(rdf:type)\" as ?propertyPath)\r\n"
//			+ "	 BIND( 20 as ?maxPath)\r\n"
//			+ "	(?edge ?subject ?property ?direct ?object )  <http://inova8.com/olgap/shortestPath>   (?service  ?start  ?end ?propertyPath ?maxPath )  .\r\n"
//			+ "}";			
			
//								queryString += "PREFIX londontube: <http://in4mium.com/londontube/id/> \n"
//											+	"PREFIX  tfl: <http://in4mium.com/londontube/ref/> \n"
//											+ "SELECT ?subject ?property ?object ?direct  ?edge \r\n" + "WHERE {\r\n"
//											+ "     BIND( <http://localhost:8082/rdf4j-server/repositories/tfl> as ?service)\r\n"
//											+ "	 BIND( <http://in4mium.com/londontube/id/Oakleigh_Park> as ?start)\r\n"
//											+ "	 BIND( <http://in4mium.com/londontube/id/Mornington_Crescent> as ?end)\r\n"
//											//+ "	 BIND( \"(<http://in4mium.com/londontube/ref/connectsToxx>|<http://in4mium.com/londontube/ref/onLinexx>)!(rdf:type)^(<http://in4mium.com/londontube/ref/connectsFrom>|<http://in4mium.com/londontube/ref/hasStationOnLinexx>)^!(rdf:type)\" as ?propertyPath)\r\n"
//											//+ "	 BIND( \"^!(rdf:type)!(rdf:type)^(tfl:connectsFrom)\" as ?propertyPath)\r\n"
//											+ "	 BIND( \"(<http://in4mium.com/londontube/ref/connectsFrom>)!(rdf:type|<http://in4mium.com/londontube/ref/connectsTo>)^!(rdf:type|<http://in4mium.com/londontube/ref/hasStationInZone>|<http://in4mium.com/londontube/ref/hasStationOnLine>|<http://in4mium.com/londontube/ref/connectsFrom>)^(rdf:type)\" as ?propertyPath)\r\n"
//											+ "	 BIND( 20 as ?maxPath)\r\n"
//											+ "	(?edge ?subject ?property ?direct ?object )  <http://inova8.com/olgap/shortestPath>   (?service  ?start  ?end ?propertyPath ?maxPath )  .\r\n"
//											+ "}";
//						
//					
//						queryString += "PREFIX NHSD: <http://nexifysolutions.com/Customer360/ods/id/> \n"
//								+ "PREFIX NHS: <http://nexifysolutions.com/Customer360/ods/ref/> \n"
//								+ "SELECT ?subject ?property ?object ?direct  ?edge \r\n" + "WHERE {\r\n"
//								+ "  BIND( <http://localhost:8082/rdf4j-server/repositories/nhs> as ?service)\r\n"
//								+ "	 BIND( <http://nexifysolutions.com/Customer360/ods/id/G9352270> as ?start)\r\n"
//								+ "	 BIND( <http://nexifysolutions.com/Customer360/ods/id/03NVG> as ?end)\r\n"
//								+ "	 BIND( \"!(rdf:type|<http://nexifysolutions.com/Customer360/ods/ref/Nurse_Prescriber.Current_Care_Organisation_Code>|<http://nexifysolutions.com/Customer360/ods/ref/General_Practitioner.Current_Care_Organisation>|<http://nexifysolutions.com/Customer360/ods/ref/Clinical_Commissioning_Group.Organisation_Sub-Type_Code>|<http://nexifysolutions.com/Customer360/ods/ref/Independent_Provider.Organisation_Sub-Type_Code>|<http://nexifysolutions.com/Customer360/ods/ref/General_Practitioner.Organisation_Sub-Type_Code>|<http://nexifysolutions.com/Customer360/ods/ref/GP_Practice.Status_Code>|<http://nexifysolutions.com/Customer360/ods/ref/General_Practitioner.Status_Code>|<http://nexifysolutions.com/Customer360/ods/ref/Clinical_Commissioning_Group_Site.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Independent_Provider.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/GP_Practice.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/General_Practitioner.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Dispensary.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/GP_Branch_Surgery.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/General_Dental_Practice.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/General_Dental_Practitioner.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Clinical_Commisioning_Group.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Geographical_Region.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Independant_Healthcare_Provider.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Independant_Healthcare_Provider_Site.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Independant_Provider.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/NHS_Care_Trust.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/NHS_Care.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/NHS_Trust.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/NHS_Trust_Site.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Pharmacy_Headquarters_Organization.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Practice_Membership.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Primary_Care_Trust.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Primary_Care_Trust_Site.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Clinical_Commissioning_Group.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Dispensary.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/GP_Branch_Surgery.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/GP_Practice_Group.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/General_Dental_Practitioner_Membership.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/General_Practitioner_Membership.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Private_Controlled_Drug_Prescriber.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/General_Practitioner.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Dispensary.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/GP_Branch_Surgery.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/General_Dental_Practice.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/General_Dental_Practitioner.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Clinical_Commisioning_Group.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Geographical_Region.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Independant_Healthcare_Provider.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Independant_Healthcare_Provider_Site.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Independant_Provider.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/NHS_Care_Trust.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/NHS_Care.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/NHS_Trust.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/NHS_Trust_Site.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Pharmacy_Headquarters_Organization.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Practice_Membership.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Primary_Care_Trust.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Primary_Care_Trust_Site.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Clinical_Commissioning_Group.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Dispensary.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/GP_Branch_Surgery.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/GP_Practice_Group.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/General_Dental_Practitioner_Membership.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/General_Practitioner_Membership.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Private_Controlled_Drug_Prescriber.Amended_Record_Indicator.inverse>)			^!(rdf:type|<http://nexifysolutions.com/Customer360/ods/ref/Nurse_Prescriber.Current_Care_Organisation_Code>|<http://nexifysolutions.com/Customer360/ods/ref/General_Practitioner.Current_Care_Organisation>|<http://nexifysolutions.com/Customer360/ods/ref/Clinical_Commissioning_Group.Organisation_Sub-Type_Code>|<http://nexifysolutions.com/Customer360/ods/ref/Independent_Provider.Organisation_Sub-Type_Code>|<http://nexifysolutions.com/Customer360/ods/ref/General_Practitioner.Organisation_Sub-Type_Code>|<http://nexifysolutions.com/Customer360/ods/ref/GP_Practice.Status_Code>|<http://nexifysolutions.com/Customer360/ods/ref/General_Practitioner.Status_Code>|<http://nexifysolutions.com/Customer360/ods/ref/Clinical_Commissioning_Group_Site.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Independent_Provider.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/GP_Practice.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/General_Practitioner.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Dispensary.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/GP_Branch_Surgery.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/General_Dental_Practice.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/General_Dental_Practitioner.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Clinical_Commisioning_Group.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Geographical_Region.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Independant_Healthcare_Provider.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Independant_Healthcare_Provider_Site.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Independant_Provider.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/NHS_Care_Trust.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/NHS_Care.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/NHS_Trust.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/NHS_Trust_Site.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Pharmacy_Headquarters_Organization.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Practice_Membership.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Primary_Care_Trust.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Primary_Care_Trust_Site.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Clinical_Commissioning_Group.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Dispensary.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/GP_Branch_Surgery.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/GP_Practice_Group.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/General_Dental_Practitioner_Membership.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/General_Practitioner_Membership.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/Private_Controlled_Drug_Prescriber.Amended_Record_Indicator>|<http://nexifysolutions.com/Customer360/ods/ref/General_Practitioner.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Dispensary.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/GP_Branch_Surgery.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/General_Dental_Practice.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/General_Dental_Practitioner.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Clinical_Commisioning_Group.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Geographical_Region.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Independant_Healthcare_Provider.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Independant_Healthcare_Provider_Site.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Independant_Provider.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/NHS_Care_Trust.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/NHS_Care.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/NHS_Trust.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/NHS_Trust_Site.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Pharmacy_Headquarters_Organization.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Practice_Membership.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Primary_Care_Trust.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Primary_Care_Trust_Site.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Clinical_Commissioning_Group.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Dispensary.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/GP_Branch_Surgery.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/GP_Practice_Group.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/General_Dental_Practitioner_Membership.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/General_Practitioner_Membership.Amended_Record_Indicator.inverse>|<http://nexifysolutions.com/Customer360/ods/ref/Private_Controlled_Drug_Prescriber.Amended_Record_Indicator.inverse>)\" as ?propertyPath)\r\n"
//			+ "	 BIND( 5 as ?maxPath)\r\n"
//								+ "	(?edge ?subject ?property ?direct ?object )  <http://inova8.com/olgap/shortestPath>   (?service  ?start  ?end ?propertyPath ?maxPath )  .\r\n"
//								+ "}";		
						
						
			
			
			queryString += "order by ?edge";

			TupleQuery query = conn.prepareTupleQuery(queryString);

			try (TupleQueryResult result = query.evaluate()) {
					List<String> bindingNames = result.getBindingNames();
					while (result.hasNext()) {
						BindingSet solution = result.next();
						StringBuilder aResult = new StringBuilder();
						for (String bindingName : bindingNames) {
							aResult.append(bindingName).append(" = ").append(solution.getValue(bindingName).stringValue()).append("; ");
						}
						System.out.println(aResult);
				}
				System.out.println("Finished!");
			}
			
			System.out.println("Try again from cache!");
			
			try (TupleQueryResult result = query.evaluate()) {
				List<String> bindingNames = result.getBindingNames();
				while (result.hasNext()) {
					BindingSet solution = result.next();
					StringBuilder aResult = new StringBuilder();
					for (String bindingName : bindingNames) {
						aResult.append(bindingName).append(" = ").append(solution.getValue(bindingName).stringValue()).append("; ");
					}
					System.out.println(aResult);
			}
			System.out.println("Finished!");
		}		
			
			
			
			
			
			
			
		} finally {
			workingRep.shutDown();
		}
	}

}

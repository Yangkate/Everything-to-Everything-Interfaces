package net.viralpatel.maven;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 *
 * @author Administrator
 */
public class HelloWorldServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 23456787890L;

	public HelloWorldServlet() {
		super();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String booker = req.getParameter("booker");

		String people = req.getParameter("people");

		String room = req.getParameter("room");

		String distanceToLake = req.getParameter("distanceToLake");

		String cityNearBy = req.getParameter("cityNearBy");

		System.out.println("cityNearBy" + cityNearBy);
		String daysForStay = req.getParameter("daysForStay");

		String startDate = req.getParameter("startDate");
		System.out.println("startDate" + startDate);
		String maxShift = req.getParameter("maxShift");

		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		out.println("<span ><strong>Dear " + booker
				+ ", your booking details are below</strong></span><br/>");
		

		String url = "http://localhost:8080/booking/cottagey.n3";
		String query = "";
		Dataset dataset = null;
		Model model = ModelFactory.createDefaultModel();
		model.read(url, "N3");
		//model.write(System.out, "N3");
		dataset = DatasetFactory.create(model);

		query = setPrefix(people, room, distanceToLake, cityNearBy,
				daysForStay, startDate);
		System.out.println("query" + query);
		ResultSet resultSet = search(query, resp, dataset, startDate,
				daysForStay);
		System.out.println("resultSet-1" + resultSet);
		
		if (resultSet == null) {

			if (Integer.parseInt(maxShift) != 0) {

				out.println("<span ><strong>sorry no cottage mathes your search,now we are trying to find shift...</strong></span><br/>");
				System.out.println("shift now");
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				try {
					cal.setTime(dateFormat.parse(startDate));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (int i = 1; i <= Math.abs(Integer.parseInt(maxShift)); i++) {
					// int sign=Integer.parseInt(maxShift)
					// / Math.abs(Integer.parseInt(maxShift));
					// startDate = startDate + i * Integer.parseInt(maxShift)
					// / Math.abs(Integer.parseInt(maxShift));
					cal.add(Calendar.DATE, i);
					String convertedStartDate = dateFormat
							.format(cal.getTime());
					System.out.println("Date increase by one.."
							+ convertedStartDate);
					query = setPrefix(people, room, distanceToLake, cityNearBy,
							daysForStay, convertedStartDate);

					ResultSet resultSet2 = search(query, resp, dataset,
							convertedStartDate, daysForStay);

					cal.add(Calendar.DATE, -i);
					cal.add(Calendar.DATE, -i);
					String convertedStartDate2 = dateFormat.format(cal
							.getTime());
					System.out.println("Date increase by one.."
							+ convertedStartDate2);
					query = setPrefix(people, room, distanceToLake, cityNearBy,
							daysForStay, convertedStartDate);
					// out.println("<span ><strong>Notice! some cottages are found when shift"+
					// -i +"days.</strong></span><br/>");

					ResultSet resultSet3 = search(query, resp, dataset,
							convertedStartDate2, daysForStay);
					cal.add(Calendar.DATE, i);

					if (resultSet2 == null && resultSet3 == null) {

						out.println("<span ><strong>sorry no cottage mathes your, search more</strong></span><br/>");
					}

				}
				out.println("<span ><strong>cannot find cottage availavle that mathes your search</strong></span><br/>");
			} else {

				out.println("<span ><strong>cannot find cottage availavle that mathes your search</strong></span><br/>");
			}

		} else {
			return;
		}

	}

	// public String setCalender(){}
	public String setPrefix(String people, String room, String distanceToLake,
			String cityNearBy, String daysForStay, String startDate) {
		cityNearBy = "\"" + cityNearBy + "\"";

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(dateFormat.parse(startDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.add(Calendar.DATE, Integer.parseInt(daysForStay));
		String endDate = "\"" + dateFormat.format(cal.getTime()) + "\"";
		
		startDate = "\"" + startDate + "\"";

		String a = "PREFIX jyu:  <http://localhost:8080/booking/cottagey.rdf#>"
				+ "PREFIX xsd:     <http://www.w3.org/2001/XMLSchema#>";
//		String query = a
//				+ "SELECT ?Cottage ?people ?room ?distanceToLake ?city ?address ?image WHERE{ ?Cottage jyu:allowPeople ?people ; jyu:hasRoom ?room ; jyu:distanceToLake ?distanceToLake ; jyu:cityNearBy ?city; jyu:endDate ?endDate ; jyu:startDate ?startDate ; jyu:hasAddress ?address ; jyu:hasImage ?image. FILTER ( ?people >= "
//				+ people + "&& ?room >= " + room + " &&?distanceToLake <="
//				+ distanceToLake + "&&regex(str(?city)," + cityNearBy
//				+ ")&&?startDate <= " + startDate + "^^xsd:date &&?endDate >= "
//				+ endDate + "^^xsd:date) }";
		String query = a
				+ "SELECT ?Cottage ?people ?room ?distanceToLake ?city ?address ?image WHERE{ ?Cottage jyu:allowPeople ?people ; jyu:hasRoom ?room ; jyu:distanceToLake ?distanceToLake ; jyu:cityNearBy ?city; jyu:endDate ?endDate ; jyu:startDate ?startDate ; jyu:hasAddress ?address ; jyu:hasImage ?image. FILTER ( ?people >= "
				+ people + "&& ?room >= " + room + " &&?distanceToLake <="
				+ distanceToLake + "&&regex(str(?city)," + cityNearBy
				+ ")) }";

		return query;
	}

	public ResultSet search(String query, HttpServletResponse resp,
			Dataset dataset, String startdate, String daysForStay) {
		Random r = new Random();
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = resp.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Query q = QueryFactory.create(query);
		QueryExecution qexec = QueryExecutionFactory.create(q, dataset);
		ResultSet resultSet = qexec.execSelect();
		List<String> varNames = resultSet.getResultVars();
		if (resultSet.hasNext()) {
			while (resultSet.hasNext()) {
				int bookingNumber = Math.abs(r.nextInt() * 384646 + 1384646);
				out.println("<span ><strong>Booking bumber:" + bookingNumber
						+ "</strong></span><br/>");
				QuerySolution row = (QuerySolution) resultSet.next();
				for (String varName : varNames) {

					RDFNode value = row.get(varName);
					System.out.println("value.toString()" + value.toString());
					String[] a = null;
					if (varName.equals("Cottage")) {
						a = value.toString().split("#", 2);
						out.println("<span ><strong>" + varName + "=" + a[1]
								+ ";</strong></span><br/>");

					} else if (varName.equals("image")) {
						a = value.toString().split("\\^", 3);

						out.println("<img src=" + a[0]
								+ "alt=W3Schools.com><br/>");

					} else {
						a = value.toString().split("\\^", 3);
						out.println("<span><strong>" + varName + "=" + a[0]
								+ ";</strong></span><br/>");

					}

				}
				out.println("<span><strong> StartDate=" + startdate
						+ "</strong></span><br/>");
				out.println("<span><strong> dayForStay=" + daysForStay
						+ "</strong></span><br/>");
			}

		} else {

			resultSet = null;
			System.out.println("resultSet" + resultSet);
		}
		qexec.close();
		
		return resultSet;
	}

}

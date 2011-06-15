package pl.edu.uj.www;

import java.awt.Color;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.ibm.jms.JMSMessage;
import com.ibm.jms.JMSTextMessage;
import com.ibm.mq.jms.JMSC;
import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQQueueReceiver;
import com.ibm.mq.jms.MQQueueSession;

/**
 * Servlet implementation class LoginServlet
 */
@SuppressWarnings("deprecation")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String elem = "Unknown exception";
		try {
			elem = getQueueElement();

		} catch (JMSException e) {
			e.printStackTrace();
			elem = "JMS Exception";

		} finally {
			getServletContext().setAttribute("elem", elem);
			
		Map<String, Number> data = new HashMap<String, Number>();
		//	String tmp="aaaaa;1#bbbbb;2#cccccc;3#ddddd;4#eeeee;10";
		System.out.println("elem: "+elem);	
		
		
			String[] dane=elem.split("#");
			
			for(String s : dane){
				
				String [] element=s.split(";");
				
				if(element.length!=2){
					getServletContext().setAttribute("BadDataFormat", element);
				}
				
				data.put(element[0],Double.parseDouble(element[1]));
				
			}
			
			doChart(data);
			response.sendRedirect("index.jsp");

		}

	}

	private static String getQueueElement() throws JMSException {

		MQQueueConnectionFactory cf = null;
		MQQueueConnection connection = null;
		MQQueueSession session = null;
		MQQueue queue = null;
		// MQQueueSender sender=null;
		MQQueueReceiver receiver = null;

		cf = new MQQueueConnectionFactory();

		// Config
		cf.setHostName("macosx.hopto.org");
		cf.setPort(1414);
		cf.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
		cf.setQueueManager("QM_mp_Komputer");
		cf.setChannel("S_mp_Komputer");

		connection = (MQQueueConnection) cf.createQueueConnection();
		session = (MQQueueSession) connection.createQueueSession(false,
				Session.AUTO_ACKNOWLEDGE);

		queue = (MQQueue) session.createQueue("queue:///gpw");
		// sender = (MQQueueSender) session.createSender(queue);
		receiver = (MQQueueReceiver) session.createReceiver(queue);

		// long uniqueNumber = System.currentTimeMillis() % 1000;
		// JMSTextMessage message = (JMSTextMessage)
		// session.createTextMessage("SimplePTP "+ uniqueNumber);

		// Start the connection
		connection.start();

		// sender.send(message);
		// System.out.println("Sent message:\\n" + message);

		JMSMessage receivedMessage = (JMSMessage) receiver.receive(10000);
		if (receivedMessage instanceof JMSTextMessage) {
			JMSTextMessage txt = (JMSTextMessage) receivedMessage;
			System.out.println("Message Received: " + txt.getText());
			// sender.close();
			receiver.close();
			session.close();
			connection.close();
			System.out.println("\nSUCCESS\n");

			return txt.getText();
		} else {
			System.out.println("\nReceived message:\n" + receivedMessage);
			// sender.close();
			receiver.close();
			session.close();
			connection.close();
			System.out.println("\nSUCCESS\n");
			if (receivedMessage == null)
				return null;
			return receivedMessage.toString();
		}

	}

	private void doChart(Map<String, Number> data) {

		// <img src="chart.png" alt="" />
		// tworze zestaw danych
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (String key : data.keySet()) {
			dataset.setValue(data.get(key), "indeksy", key);
		}

		// tworze wykres
		final JFreeChart chart = ChartFactory.createBarChart(
				"Allocation of Duties", "Values", null, dataset,
				PlotOrientation.VERTICAL, true, true, true);
		// ustawiam kolor
		GradientPaint gradientpaint0 = new GradientPaint(0.0F, 0.0F, new Color(
				209, 228, 246), 0.0F, 0.0F, new Color(82, 141, 201));
		BarRenderer r = (BarRenderer) chart.getCategoryPlot().getRenderer();
		r.setSeriesPaint(0, gradientpaint0);
		File f=new File("chart.png");
		try {
			ChartUtilities.saveChartAsPNG(f, chart, 400,
					300);
			System.out.println("Wykres zapisano: "+f.getAbsoluteFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * try { ServletOutputStream out = response.getOutputStream();
		 * response.setContentType("image/png");
		 * ChartUtilities.writeChartAsPNG(out, chart, 400, 300); out.close();
		 * response.sendRedirect("index.jsp"); } catch (IOException e) {
		 * System.out.println("Problem ze stworzeniem wykresu!");
		 * e.printStackTrace(); }
		 */
	}

	public static void main(String[] args) {
		String elem = null;
		try {
			elem = getQueueElement();
			System.out.println("msg: " + elem);

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
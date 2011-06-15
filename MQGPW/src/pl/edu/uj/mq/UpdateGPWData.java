package pl.edu.uj.mq;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.jms.*;
import javax.naming.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.jms.JMSMessage;
import com.ibm.jms.JMSTextMessage;
import com.ibm.mq.jms.JMSC;
import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQQueueReceiver;
import com.ibm.mq.jms.MQQueueSender;
import com.ibm.mq.jms.MQQueueSession;

/**
 * Servlet implementation class UpdateGPWData
 */
@WebServlet("/UpdateGPWData")
public class UpdateGPWData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateGPWData() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		response.setContentType("text/html");
		response.setHeader("pragma", "no-cache");
		PrintWriter out = response.getWriter();
		out.print("<HTML><HEAD><TITLE></TITLE></HEAD>");

		try {

			//load data
			//XMLLoader loader = new XMLLoader();			
			
		    long randomNumber = System.currentTimeMillis() % 1000;
			setQueueElement("AGORA;0.59#08OCTAVA;2.73#4FUNMEDIA;16.36#ABCDATA;3.25#ABMSOLID;8.9#ABPL;24.06");

		} catch (Exception e) {
			out.print("<p>" + e.getMessage() + "</p>");
		}

		out.print("<BODY><H3>Queue has been UPDATED</H3><UL>");

		out.print("</FORM></BODY></HTML>");
		out.close();
	}


	private static boolean setQueueElement(String msg) throws JMSException {

		MQQueueConnectionFactory cf = null;
		MQQueueConnection connection = null;
		MQQueueSession session = null;
		MQQueue queue = null;
	    MQQueueSender sender=null;

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
		 sender = (MQQueueSender) session.createSender(queue);
		//receiver = (MQQueueReceiver) session.createReceiver(queue);


		
		 JMSTextMessage message = (JMSTextMessage)
		 session.createTextMessage(msg);

		// Start the connection
		connection.start();

		
		
		
		 sender.send(message);
		// System.out.println("Sent message:\\n" + message);

		 connection.close();
		 
		return true;
	}	
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

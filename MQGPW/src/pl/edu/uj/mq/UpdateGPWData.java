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
			sendTestMSg("OK");

		} catch (JMSException e) {
			out.print("<p>" + e.getMessage() + "</p>");
		} catch (Exception e) {
			out.print("<p>" + e.getMessage() + "</p>");
		}

		out.print("<BODY><H3>Queue has been UPDATED</H3><UL>");

		out.print("</FORM></BODY></HTML>");
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	public void sendTestMSg(String msgText) throws NamingException,
			JMSException {
		QueueConnection queueCon = null;
		try {

			Hashtable env = new Hashtable();

			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"com.sun.jndi.fscontext.RefFSContextFactory");

			env.put(Context.PROVIDER_URL, "file:///c:/imq_admin_objects");

			// get the initial context, refer to your app server docs for this
			Context ctx = new InitialContext(env);

			// get the connection factory, and open a connection
			QueueConnectionFactory qcf = (QueueConnectionFactory) ctx
					.lookup("QM_GPW");
			queueCon = qcf.createQueueConnection();

			// create queue session off the connection
			QueueSession queueSession = queueCon.createQueueSession(false,
					Session.AUTO_ACKNOWLEDGE);

			// get handle on queue, create a sender and send the message
			Queue queue = (Queue) ctx.lookup("jms/queue/devilman");
			QueueSender sender = queueSession.createSender(queue);

			Message msg = queueSession.createTextMessage("hello...");

			msg.setBooleanProperty("ACK_DEBUG", true);
			msg.setFloatProperty("ACK_BALANCE", 24234.44f);
			sender.send(msg);

			System.out.println("sent the message");
		} finally {
			// close the queue connection
			if (queueCon != null) {
				queueCon.close();
			}
		}
	}

}

package murach.email;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import murach.business.User;
import murach.data.UserDB;
import murach.util.MailUtilBrevo;

@WebServlet("/emailList")
public class EmailListServlet extends HttpServlet {

    // các email này được phép đăng ký lại nhiều lần để test
    private static final List<String> TEST_EMAILS
            = Arrays.asList("emcuadailoc1508@gmail.com");

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "join";
        }

        String url = "/index.jsp";
        if (action.equals("join")) {
            url = "/index.jsp";
        } else if (action.equals("add")) {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            User user = new User(firstName, lastName, email);
            String message;

            boolean isTestEmail = TEST_EMAILS.contains(email.trim().toLowerCase());
            boolean exists = UserDB.emailExists(user.getEmail());

            if (exists && !isTestEmail) {
                message = "This email address already exists.<br>"
                        + "Please enter another email address.";
                url = "/index.jsp";
            } else {
                message = "";
                url = "/thanks.jsp";

                if (!exists) {
                    UserDB.insert(user);
                }

                String to = email;
                String from = "emcuadailoc1508@gmail.com";
                String subject = "Welcome to our email list";
                String body = "Dear " + firstName + ",\n\n"
                        + "Thanks for joining our email list. "
                        + "We'll make sure to send "
                        + "you announcements about new products "
                        + "and promotions.\n"
                        + "Have a great day and thanks again!\n\n"
                        + "Kelly Slivkoff\n"
                        + "Mike Murach & Associates";
                boolean isBodyHTML = false;

                try {
                    MailUtilBrevo.sendMail(to, from, subject, body, isBodyHTML);
                } catch (MessagingException e) {
                    String errorMessage
                            = "ERROR: Unable to send email. "
                            + "Check Tomcat logs for details.<br>"
                            + "ERROR MESSAGE: " + e.getMessage();
                    request.setAttribute("errorMessage", errorMessage);
                    this.log("Unable to send email. TO: " + email
                            + " ERROR: " + e);
                }
            }
            request.setAttribute("user", user);
            request.setAttribute("message", message);
        }

        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
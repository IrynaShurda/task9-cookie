package TimeServlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    public static final String TIME_ZONE = "timezone";
    public static final String TIME = "time";
    private TemplateEngine engine;

    public void init() {
        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("/Users/iryna/IdeaProjects/task9-servletPlus/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        boolean isTimeZoneParameterExist = req.getParameterMap().containsKey(TIME_ZONE);
        String timezone;
        if (isTimeZoneParameterExist) {
            timezone = req.getParameter(TIME_ZONE);
        }
        else {
            timezone = req.getCookies()[0].getValue();
        }

        String time = ParseTimeZone.getCurrentTimeInTimeZone(timezone);
        Cookie cookieWithTimeZone = new Cookie(TIME_ZONE, ParseTimeZone.toTimeZone(timezone));
        resp.addCookie(cookieWithTimeZone);
        Context simpleContext = new Context(resp.getLocale(), Map.of(TIME, time));
        engine.process(TIME, simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
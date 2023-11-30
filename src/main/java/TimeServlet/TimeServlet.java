package TimeServlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    public static final String TIME_ZONE = "timezone";
    public static final String TIME = "time";
    private TemplateEngine engine;

    public void init() {
        engine = new TemplateEngine();
        String resourcePath = getResourcePath("/templates/");
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(resourcePath);
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    private static String getResourcePath(String resourceFolder) {
        String path = TimeServlet.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8);
        File file = new File(decodedPath);
        if (file.isFile()) {
            decodedPath = file.getParent();
        }
        return decodedPath + resourceFolder;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        boolean isTimeZoneParameterExist = req.getParameterMap().containsKey(TIME_ZONE);
        String timezone = "UTC";
        if (isTimeZoneParameterExist) {
            timezone = req.getParameter(TIME_ZONE);
        }
        else {
            Cookie[] cookies = req.getCookies();
            if (cookies != null && cookies.length > 0) {
                timezone = cookies[0].getValue();
            }
        }

        String time = ParseTimeZone.getCurrentTimeInTimeZone(timezone);
        Cookie cookieWithTimeZone = new Cookie(TIME_ZONE, ParseTimeZone.toTimeZone(timezone));
        resp.addCookie(cookieWithTimeZone);
        Context simpleContext = new Context(resp.getLocale(), Map.of(TIME, time));
        engine.process(TIME, simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
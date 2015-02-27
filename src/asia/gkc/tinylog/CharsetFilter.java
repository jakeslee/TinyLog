package asia.gkc.tinylog;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by jakes on 14-12-3.
 */
@WebFilter(filterName = "CharsetFilter")
public class CharsetFilter implements Filter {
    String encoding = null;

    public void destroy() {
        encoding = null;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (encoding != null){
            req.setCharacterEncoding(encoding);
            resp.setContentType("charset=" + encoding);
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter("encoding");
    }

}

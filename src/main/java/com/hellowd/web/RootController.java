/**
 * @author SangHoon, Lee(digimon.1740@gmail.com)
 */
package com.hellowd.web;

import com.hellowd.exception.BadRequestException;
import com.hellowd.exception.ForbiddenException;
import com.hellowd.exception.NotFoundException;
import com.hellowd.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class RootController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@ExceptionHandler(Exception.class)
	public Map<String, Object> handleException(Exception e, HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> fault = new LinkedHashMap<String, Object>(4);
		int errorStatusCode = 500;
		if (e instanceof BadRequestException) {
			fault.put("faultcode", "Client");
			fault.put("faultstring", "400 Bad Request");
			errorStatusCode = HttpServletResponse.SC_BAD_REQUEST;
		} else if (e instanceof UnauthorizedException) {
			fault.put("faultcode", "Client");
			fault.put("faultstring", "401 Unauthorized");
			errorStatusCode = HttpServletResponse.SC_UNAUTHORIZED;
		} else if (e instanceof ForbiddenException) {
			fault.put("faultcode", "Client");
			fault.put("faultstring", "403 Forbidden");
			errorStatusCode = HttpServletResponse.SC_FORBIDDEN;
		} else if (e instanceof NotFoundException) {
			fault.put("faultcode", "Client");
			fault.put("faultstring", "404 Not Found");
			errorStatusCode = HttpServletResponse.SC_NOT_FOUND;
		} else {
			fault.put("faultcode", "Server");
			fault.put("faultstring", "500 Internal Server Error");
			errorStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}

		try {
			fault.put("faultactor", req.getRequestURI());

			Map<String, String> detail = new LinkedHashMap<String, String>(2);
			detail.put("message", e.getMessage());
			detail.put("stacktrace", getStackTrace(e));

			fault.put("detail", detail);

			resp.sendError(errorStatusCode, fault.toString());

		} catch (Exception ex) {
			logger.error("Error at HttpServletResponse.sendError(" + errorStatusCode + ")", ex);
		}

		return fault;

	}

	private String getStackTrace(Throwable thrown) {
		StringWriter sw = new StringWriter();
		thrown.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

}

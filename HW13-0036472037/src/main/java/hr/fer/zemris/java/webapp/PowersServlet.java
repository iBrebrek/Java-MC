package hr.fer.zemris.java.webapp;

import java.io.IOException;
import java.util.function.Function;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Downloads excel file containing powers based on given parameters.
 * Given parameters must be: a and b between -100 and 100,
 * n between 1 and 4. If parameters are not valid file is not
 * downloaded and work is redirected to error.jsp.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (8.6.2016.)
 */
@WebServlet(name="powers", urlPatterns={"/powers"})
public class PowersServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int a = 0;
		int b = 0;
		int n = 0;
		try {
			a = Integer.parseInt(req.getParameter("a"));
			b = Integer.parseInt(req.getParameter("b"));
			n = Integer.parseInt(req.getParameter("n"));
			if(a < -100 || a > 100) throw new Exception();
			if(b < -100 || b > 100) throw new Exception();
			if(n < 1 || n > 5) throw new Exception();
		} catch(Exception exc) {
			req.setAttribute("errorMsg", 
					"Excel file was not created because your input was invalid"+
					"<ul><li>a must be between -100 and 100</li>"+
					"<li>b must be between -100 and 100</li>"+
					"<li>n must be between 1 and 5</li></ul>"
			);
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
		}
		
		HSSFWorkbook book = new HSSFWorkbook();
		
		int numberOfRows = Math.abs(a-b)+1;
		Function<Integer, Integer> byOne = null;
		if(a < b) byOne = x -> (x+1);
		else byOne = x -> (x-1);
		
		for(int i = 1; i <= n; i++) {
			HSSFSheet sheet =  book.createSheet("Sheet #"+i);
			int temp = a;
			
			HSSFRow row = sheet.createRow(0);
			row.createCell(0).setCellValue("x");
			row.createCell(1).setCellValue("f(x^"+i+")");
			
			for(int j = 1; j <= numberOfRows; j++, temp=byOne.apply(temp)) {
				row = sheet.createRow(j);
				row.createCell(0).setCellValue(temp);
				row.createCell(1).setCellValue(Math.pow(temp, i));
			}
		}

		resp.reset();
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=\"powers.xls\"");
		
		book.write(resp.getOutputStream());
		resp.getOutputStream().flush();
		resp.getOutputStream().close();
		book.close();
	}
}

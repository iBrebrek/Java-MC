package hr.fer.zemris.java.webapp.voting;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Downloads file voting_results.xls containing
 * table of bands and how many votes they got.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (8.6.2016.)
 */
@WebServlet(name="voting-xls", urlPatterns={"/voting-xls"})
public class VotingXLSServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet =  book.createSheet("Votes");
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("Band");
		row.createCell(1).setCellValue("Number of votes");
		
		List<Band> bands = VotingUtils.getBands(req, resp, "sortedBands");

		int rowIndex = 1;
		for(Band band : bands) {
			row = sheet.createRow(rowIndex++);
			row.createCell(0).setCellValue(band.getName());
			row.createCell(1).setCellValue(band.getVotes());
		}

		resp.reset();
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=\"voting_results.xls\"");
		
		book.write(resp.getOutputStream());
		resp.getOutputStream().flush();
		resp.getOutputStream().close();
		book.close();
	}
}

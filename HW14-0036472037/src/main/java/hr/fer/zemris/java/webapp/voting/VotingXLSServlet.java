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

import hr.fer.zemris.java.webapp.dao.DAOProvider;
import hr.fer.zemris.java.webapp.model.PollOption;

/**
 * Downloads file voting_results.xls containing
 * table of options and how many votes they got.
 * 
 * @author Ivica Brebrek
 * @version 1.1  (14.6.2016.)
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
		row.createCell(0).setCellValue("Option");
		row.createCell(1).setCellValue("Number of votes");
		
		long pollID = Long.parseLong(req.getParameter("pollID"));
		List<PollOption> options = DAOProvider.getDao().getPollOptions(pollID);

		int rowIndex = 1;
		for(PollOption option : options) {
			row = sheet.createRow(rowIndex++);
			row.createCell(0).setCellValue(option.getTitle());
			row.createCell(1).setCellValue(option.getVotesCount());
		}

		String pollName = DAOProvider.getDao().getPoll(pollID).getTitle();
		
		resp.reset();
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=\"[VOTES] "+pollName+".xls\"");
		
		book.write(resp.getOutputStream());
		book.close();
	}
}

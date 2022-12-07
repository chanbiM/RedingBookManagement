package view;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import domain.book;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.JDBCUtil;

public class BookInforController implements Initializable {
	String idData;
	String bookData;

	@FXML
	TextField txtname;

	@FXML
	TextField txtWriter;

	@FXML
	TextField txtpublisher;

	@FXML
	TextField txtgerne;

	@FXML
	TextArea txtImpression;

	JDBCUtil db = new JDBCUtil();
	Connection con = db.getConnection();
	book book = new book();

	PreparedStatement pstmt = null;
	ResultSet rs = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void loadData() {
		System.out.println(idData + bookData);
		String sql = "SELECT * FROM `book` WHERE `user_added`='" + idData + "' and name like'" + bookData + "'";

		try {

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String name = rs.getString("name");
				String writer = rs.getString("writer");
				String genre = rs.getString("genre");
				String publisher = rs.getString("publisher");
				String impression = rs.getString("impression");

				book.setbName(name);
				book.setbWriter(writer);
				book.setbGenre(genre);
				book.setBpublisher(publisher);
				book.setbImpression(impression);
			}

			// 데이터 넣기
			txtname.setText(book.getbName());
			txtgerne.setText(book.getbGenre());
			txtWriter.setText(book.getbWriter());
			txtpublisher.setText(book.getBpublisher());
			txtImpression.setText(book.getbImpression());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initData(String bookName, String iData) {
		bookData = bookName;
		idData = iData;
	}

	@FXML
	Button closeBtn;

	public void close() {
		Stage stage = (Stage) closeBtn.getScene().getWindow();
		stage.close();
		MainController.inforpopData();
	}

}

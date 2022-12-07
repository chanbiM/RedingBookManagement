package view;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import domain.book;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.AppUtil;
import util.JDBCUtil;

public class UpBookController implements Initializable {
	private String idData = "";
	private String passwordData = "";
	private String bookData = "";

	@FXML
	TextField txtname;

	@FXML
	TextField txtWriter;

	@FXML
	TextField txtpublisher;

	@FXML
	ChoiceBox<String> choiceBox = new ChoiceBox<>();

	JDBCUtil db = new JDBCUtil();
	Connection con = db.getConnection();
	book book = new book();

	PreparedStatement pstmt = null;
	ResultSet rs = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	// 수정확인
	public void update() {
		if (txtname.getText().isEmpty()) {
			AppUtil.warAlert("", "공백은 입력할 수 없습니다.");
			return;
		} else {
			if (txtWriter.getText().isEmpty()) {
				AppUtil.warAlert("", "공백은 입력할 수 없습니다.");
				return;
			} else {
				if (txtpublisher.getText().isEmpty()) {
					AppUtil.warAlert("", "공백은 입력할 수 없습니다.");
					return;
				} else {
					String sql = "UPDATE `book` SET `name`='" + txtname.getText() + "',`writer`='" + txtWriter.getText()
							+ "',`genre`='" + choiceBox.getValue() + "',`publisher`='" + txtpublisher.getText()
							+ "' WHERE `user_added`='" + idData + "' and `name`='" + bookData + "'";

					try {
						System.out.println("들어감");
						pstmt = con.prepareStatement(sql);
						// pstmt.setString(1, txtname.getText());
						// pstmt.setString(2, txtWriter.getText());
						// pstmt.setString(3, choiceBox.getValue());
						// pstmt.setString(4, txtpublisher.getText());
						// pstmt.setString(5, idData);
						// pstmt.setString(6, bookData);

						System.out.println("삽입 성공!!");
						pstmt.executeUpdate();

						AppUtil.inforAlert("", bookData + ", 수정되었습니다.");
						close();

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
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

				book.setbName(name);
				book.setbWriter(writer);
				book.setbGenre(genre);
				book.setBpublisher(publisher);
			}

			choiceBox.getItems().add(0, book.getbGenre());
			choiceBox.getSelectionModel().select(0);
			choiceBox.getItems().addAll("기타", "철학", "종교", "사회과학", "자연과학", "기술과학", "예술", "언어", "문학", "역사");

			// 데이터 넣기
			txtname.setText(book.getbName());
			txtWriter.setText(book.getbWriter());
			txtpublisher.setText(book.getBpublisher());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initData(String bookName, String iData, String password) {
		bookData = bookName;
		idData = iData;
		passwordData = password;
	}

	@FXML
	Button closeBtn;

	public void close() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/MainLayout.fxml"));
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);

			scene.getStylesheets().add(getClass().getResource("/view/Layout.css").toExternalForm());

			MainController change = loader.getController();
			change.init(idData, passwordData);
			change.loadData();

			Stage primaryStage = (Stage) closeBtn.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("reading book story");
			primaryStage.setResizable(false);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

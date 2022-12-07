package view;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import domain.book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.AppUtil;
import util.JDBCUtil;

public class ImpressionController implements Initializable {
	// 소감 한줄
	// 검색 버튼을 누르면 DB 검색, 가* 그리고 목록에 보임
	// 선택을 하면 옆에 예전에 썼던 것이 보임, 수정할 수 있음
	// -> 업데이트 소감은 책 추가하면 무조건 공백으로 되어있음.

	JDBCUtil db = new JDBCUtil();
	private String idData = "";
	private String passwordData = "";
	Connection con = db.getConnection();
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	@FXML
	TextField txtSearch;

	@FXML
	TextArea txtImpression;

	@FXML
	private ListView<book> bookList;
	private ObservableList<book> bItems;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}

	public void SearchData() {
		// bItems.removeAll(bItems);

		bItems = FXCollections.observableArrayList(); // 개체가 뉴를 통해 만들어짐
		bookList.setItems(bItems);

		String sql = "select * from book where user_added='" + idData + "' and name like '%" + txtSearch.getText()
				+ "%'";

		try {
			System.out.println("들어감");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String name = rs.getString("name");
				String wirter = rs.getString("writer");
				String genre = rs.getString("genre");
				String publisher = rs.getString("publisher");

				book book = new book(name, wirter, genre, publisher);

				bItems.addAll(book);
				// bookList.getItems().addAll(bItems);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 누르면 내용나오게?

	public void searchIm() {
		int idx = bookList.getSelectionModel().getSelectedIndex();
		// UPDATE `book` SET `impression`='여어' WHERE `name` LIKE '%룰라%' AND
		// `user_added`='555';

		if (idx >= 0) {
			String sSql = "select impression from book where user_added='" + idData + "' and name='"
					+ bookList.getSelectionModel().getSelectedItem() + "'";

			try {
				pstmt = con.prepareStatement(sSql);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					String impression = rs.getString("impression");

					txtImpression.setText(impression);
				}

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// 삭제문
		} else {
			AppUtil.errorAlert("책을 클릭하세요.", "에러");
		}

	}

	public void upIm() {
		int idx = bookList.getSelectionModel().getSelectedIndex();

		if (idx >= 0) {
			String sql = "UPDATE book SET impression='" + txtImpression.getText() + "' WHERE name='"
					+ bookList.getSelectionModel().getSelectedItem() + "' AND user_added='" + idData + "'";

			try {
				System.out.println("들어감");
				pstmt = con.prepareStatement(sql);
				pstmt.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			AppUtil.errorAlert("수정할 책을 선택해주세요.", "에러");
		}
	}

	public void initData(String data, String password) {
		idData = data;
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

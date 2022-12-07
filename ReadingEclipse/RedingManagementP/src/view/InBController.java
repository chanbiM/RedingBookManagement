package view;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class InBController implements Initializable {

	@FXML
	ChoiceBox<String> choiceBox = new ChoiceBox<>();

	JDBCUtil db = new JDBCUtil();
	Connection con = db.getConnection();
	book book = new book();
	private String idData = "";
	private String passwordData = "";

	PreparedStatement pstmt = null;
	ResultSet rs = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		choiceBox.getItems().add(0, "--선택해주세요--");
		choiceBox.getSelectionModel().select(0);
		choiceBox.getItems().addAll("기타", "철학", "종교", "사회과학", "자연과학", "기술과학", "예술", "언어", "문학", "역사");
	}

	public void initData(String data, String password) {
		idData = data;
		book.setbUserAdded(idData);
		passwordData = password;
	}

	@FXML
	TextField txtBookname;

	@FXML
	TextField txtWriter;

	@FXML
	TextField txtpublisher;

	// 도서 추가
	public void insertBook() {

		if (txtBookname.getText().isEmpty()) {
			// 알림
			AppUtil.errorAlert("책이름을 입력해주세요.", null);
			return;
		} else {
			if (txtWriter.getText().isEmpty()) {
				// 알림
				AppUtil.errorAlert("지은이를 입력해주세요.", null);
				return;
			} else {
				System.out.println("1차");
				if (txtpublisher.getText().isEmpty()) {
					// 알림
					AppUtil.errorAlert("출판사를 입력해주세요.", null);
					return;
				} else {
					System.out.println("2차");

					if (choiceBox.getValue().equals("--선택해주세요--")) {
						AppUtil.errorAlert("종류를 선택해주세요.", null);

					} else {
						// 삽입
						System.out.println("3차");

						String binsertSql = "insert into book values(?,?,?,?,?,?)";

						try {
							pstmt = con.prepareStatement(binsertSql);
							pstmt.setString(1, txtBookname.getText());
							pstmt.setString(2, txtWriter.getText());
							pstmt.setString(3, choiceBox.getValue());
							pstmt.setString(4, txtpublisher.getText());
							pstmt.setString(5, book.getbUserAdded());
							pstmt.setString(6, "");

							pstmt.executeUpdate();
							System.out.println("삽입 성공!!");

							AppUtil.inforAlert("책이 추가되었습니다.", null);

							txtBookname.setText("");
							txtWriter.setText("");
							txtpublisher.setText("");
							// cGenre.setValue("선택해주세요.");

						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("삽입 실패!");
						}
					}
//					
				}
			}
		}
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

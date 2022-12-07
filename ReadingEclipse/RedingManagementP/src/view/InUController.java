package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import domain.book;
import domain.user;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.AppUtil;
import util.JDBCUtil;

public class InUController {
	JDBCUtil db = new JDBCUtil();
	Connection con = db.getConnection();

	user user = new user();
	book book = new book();

	PreparedStatement pstmt = null;
	ResultSet rs = null;

	// 회원가입 창
	@FXML
	private TextField txtId;
	@FXML
	private PasswordField pfPassword;
	@FXML
	private TextField txtNickname;
	@FXML
	private PasswordField pfAgainPassword;

	private boolean jungbock = false;

	// 메인으로 씬 바꾸기
	@FXML
	private Button mainChangeBtn;

	public void mainChangeScene() {
		try {
			Parent login = FXMLLoader.load(getClass().getResource("/view/MainLayout.fxml"));
			Scene scene = new Scene(login);
			scene.getStylesheets().add(getClass().getResource("/view/Layout.css").toExternalForm());
			Stage primaryStage = (Stage) mainChangeBtn.getScene().getWindow();
			primaryStage.setTitle("reading book story");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 중복확인
	public void jungbockHakin() {
		if (txtId.getText().isEmpty()) {
			AppUtil.errorAlert("아이디를 입력해주세요.", null);
		} else {
			String selectsql = "select id from users where id='" + txtId.getText() + "'";

			try {
				pstmt = con.prepareStatement(selectsql);
				rs = pstmt.executeQuery(selectsql);

				if (rs.next()) {
					System.out.println("이미 있는 아이디");

					AppUtil.errorAlert("이미 있는 아이디입니다.", "불가능한 아이디");
					return;

				} else {
					System.out.println("없음");
					jungbock = true;
					pstmt = null;

					// 알림
					AppUtil.inforAlert("가능한 아이디입니다.", null);
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 유저 추가하기 (가입 버튼을 눌렀을 경우)
	// 유저객체에 넣기
	public void insertUser() {

		if (txtNickname.getText().isEmpty()) {

			AppUtil.errorAlert("닉네임을 입력해주세요.", null);
			return;
		} else {
			user.setUsernickname(txtNickname.getText());

			if (txtId.getText().isEmpty()) {

				// 공백 알림
				AppUtil.errorAlert("아이디를 입력해주세요.", null);
				return;

			} else {
				user.setUserId(txtId.getText());
				if (pfPassword.getText().isEmpty()) {
					// 공백 알림
					AppUtil.errorAlert("비밀번호를 입력해주세요.", null);
					return;

				} else {
					if (pfPassword.getText().isEmpty()) {
						// 공알림
						AppUtil.errorAlert("비밀번호 확인을 입력해주세요.", null);
						return;

					} else {

						if (pfPassword.getText().equals(pfAgainPassword.getText())) {
							// 중복알림
							if (jungbock == true) {

								user.setUserPassword(pfPassword.getText());

								// 다 맞을 경우 sql에 삽입
								String UinsertSql = "insert into users values(?,?,?)";

								try {
									pstmt = con.prepareStatement(UinsertSql);
									pstmt.setString(1, user.getUsernickname());
									pstmt.setString(2, user.getUserId());
									pstmt.setString(3, user.getUserPassword());

									pstmt.executeUpdate();
									System.out.println("삽입 성공!!");

									AppUtil.inforAlert("회원가입이 완료되었습니다.", null);

									txtId.setText("");
									txtNickname.setText("");
									pfPassword.setText("");

									try {
										Parent login = FXMLLoader.load(getClass().getResource("/view/MainLayout.fxml"));
										Scene scene = new Scene(login);
										scene.getStylesheets()
												.add(getClass().getResource("/view/Layout.css").toExternalForm());
										Stage primaryStage = (Stage) mainChangeBtn.getScene().getWindow();
										primaryStage.setTitle("reading book story");
										primaryStage.setScene(scene);
									} catch (Exception e) {
										e.printStackTrace();
									}

								} catch (Exception e) {
									e.printStackTrace();
									System.out.println("삽입 실패!");
								}

							} else {
								System.out.println("중복확인");

								AppUtil.errorAlert("아이디 중복 확인을 해주세요.", null);
								return;
								// 중복확인해달라고 하기
							}
						} else {
							System.out.println("비번 틀림");

							AppUtil.errorAlert("비밀번호 확인과 다릅니다.", null);
							return;
						}
					}
				}
			}
		}

	}

}

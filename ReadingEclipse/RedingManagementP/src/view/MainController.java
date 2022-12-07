package view;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import domain.book;
import domain.user;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.AppUtil;
import util.JDBCUtil;

public class MainController {

	JDBCUtil db = new JDBCUtil();
	Connection con = db.getConnection();
	private Stage pop;

	user user = new user();
	int login = 0;

	PreparedStatement pstmt = null;
	ResultSet rs = null;

	@FXML
	private ListView<book> bookList;
	private ObservableList<book> bItems;

	@FXML
	private TableView<book> tBookList;

	@FXML
	private void initialize() {
		loadData();
	}

	public void loadData() {
		// bItems.removeAll(bItems);

		bItems = FXCollections.observableArrayList(); // 개체가 뉴를 통해 만들어짐
		bookList.setItems(bItems);

		String sql = "select * from book where user_added='" + loginId.getText() + "'";

		try {
			System.out.println("들어감");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String name = rs.getString("name");
				String writer = rs.getString("writer");
				String genre = rs.getString("genre");
				String publisher = rs.getString("publisher");

				book book = new book(name, writer, genre, publisher);

				bItems.addAll(book);
				// bookList.getItems().addAll(bItems);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delBook() {
		int idx = bookList.getSelectionModel().getSelectedIndex();

		System.out.println(bookList.getSelectionModel().getSelectedItem());
		if (idx >= 0) {

			String dSql = "DELETE FROM book WHERE user_added='" + loginId.getText() + "' AND name='"
					+ bookList.getSelectionModel().getSelectedItem() + "'";

			bItems.remove(idx);
			try {
				pstmt = con.prepareStatement(dSql);
				pstmt.executeUpdate();
				loadData();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 삭제문

		} else {
			AppUtil.errorAlert("삭제할 아이템을 설정하세요.", "에러");
		}
	}

	// 메인
	@FXML
	private Pane userPane;
	@FXML
	private Pane loginPane;
	@FXML
	private TextField loginId;
	@FXML
	private PasswordField loginpassword;

	// login pane
	@FXML
	private Label userName;

	@FXML
	private Button inUserBtn;

	// 회원가입으로 씬 바꾸기
	public void InUserChangeScene() {
		try {
			Parent login = FXMLLoader.load(getClass().getResource("/view/InsertUserLayout.fxml"));
			Scene scene = new Scene(login);
			Stage primaryStage = (Stage) inUserBtn.getScene().getWindow();
			primaryStage.setTitle("join the membership");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 소감한줄 팝업
	@FXML
	private Button impressionBtn;

	public void impressionPopup() {
		if (login == 1) {
			Stage mainStage = (Stage) impressionBtn.getScene().getWindow();

			pop = new Stage(StageStyle.DECORATED);
			pop.initModality(Modality.WINDOW_MODAL);
			pop.initOwner(mainStage);

			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/view/Sogam.fxml"));
				Parent root = (Parent) loader.load();
				Scene scene = new Scene(root);

				scene.getStylesheets().add(getClass().getResource("/view/Layout.css").toExternalForm());

				ImpressionController change = loader.getController();
				change.initData(loginId.getText(), loginpassword.getText());

				Stage primaryStage = (Stage) impressionBtn.getScene().getWindow();
				primaryStage.setScene(scene);
				primaryStage.setTitle("impression");
				primaryStage.setResizable(false);
				primaryStage.show();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			AppUtil.warAlert("로그인 후 이용 가능합니다.", null);
			return;
		}

	}

	// 책추가 팝업
	@FXML
	private Button inBookBtn;

	public void inBookPopup() {
		if (login == 1) {
			Stage mainStage = (Stage) inBookBtn.getScene().getWindow();

			pop = new Stage(StageStyle.DECORATED);
			pop.initModality(Modality.WINDOW_MODAL);
			pop.initOwner(mainStage);

			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/view/InsertBookLayout.fxml"));
				Parent root = (Parent) loader.load();
				Scene scene = new Scene(root);

				scene.getStylesheets().add(getClass().getResource("/view/Layout.css").toExternalForm());

				InBController change = loader.getController();
				change.initData(loginId.getText(), loginpassword.getText());

				Stage primaryStage = (Stage) inBookBtn.getScene().getWindow();
				primaryStage.setScene(scene);
				primaryStage.setTitle("add book");
				primaryStage.setResizable(false);
				primaryStage.show();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			AppUtil.warAlert("로그인 후 이용 가능합니다.", null);
			return;
		}
	}

	// 팝업 닫기
	@FXML
	private Button closebtn;

	public void close() {
		pop = (Stage) closebtn.getScene().getWindow();
		pop.close();
	}

	// 로그인 버튼

	@FXML
	private Button loginBtn;

	public void login() {

		if (loginId.getText().isEmpty()) {
			// 공백 알림
			AppUtil.errorAlert("아이디를 입력해주세요.", null);
			return;

		} else {
			if (loginpassword.getText().isEmpty()) {
				// 공백 알림
				AppUtil.errorAlert("비밀번호를 입력해주세요.", null);
				return;

			} else {
				String selectsql = "select * from users where id='" + loginId.getText() + "'";

				try {
					pstmt = con.prepareStatement(selectsql);
					rs = pstmt.executeQuery(selectsql);

					if (rs.next()) {
						System.out.println("1차 성공");

						if (rs.getString(3).contentEquals(loginpassword.getText())) {

							System.out.println("로그인 성공");

							// 유저창 보여주기
							System.out.println(rs.getString(1));
							loginPane.setVisible(false);

							// 열번호
							login = 1;
							userPane.setVisible(true);
							userName.setText(rs.getString(1) + "님");

							loadData();
							return;
							// rs = pstmt.executeQuery();
						} else {
							// 비번 틀림
							System.out.println("실패");
							rs = null;
							pstmt = null;

							// 알림
							AppUtil.errorAlert("비밀번호가 틀립니다.", null);
							return;

						}

					}
					// 아이디가 없음
					AppUtil.errorAlert("존재하지 않는 아이디입니다.", null);
					return;

				} catch (SQLException e) {
					e.printStackTrace();
				}
				// db오류

			}
		}
	}

	// 로그아웃
	public void logout() {
		userName.setText("");
		loginId.setText("");
		loginpassword.setText("");

		userPane.setVisible(false);
		loginPane.setVisible(true);
		login = 0;

		loadData();

		try {

			rs = pstmt.executeQuery();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void delUserAlert() {
		boolean choice = AppUtil.DelUserAlert();

		if (choice == true) {
			String dSql = "DELETE FROM users WHERE id='" + loginId.getText() + "'";

			try {
				pstmt = con.prepareStatement(dSql);
				pstmt.executeUpdate();

				userName.setText("");
				loginId.setText("");
				loginpassword.setText("");

				userPane.setVisible(false);
				loginPane.setVisible(true);
				login = 0;

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return;
		}
	}

	@FXML
	TextField upNickname;
	@FXML
	Pane upNickPane;

	// 업데이트

	// 확인
	public void updateNick() {
		if (upNickname.getText().isEmpty()) {
			AppUtil.errorAlert(null, "공백은 불가능합니다.");
			backUpNick();
		} else {
			String sql = "UPDATE users SET nickname='" + upNickname.getText() + "' WHERE id='" + loginId.getText()
					+ "'";

			try {
				System.out.println("들어감");
				pstmt = con.prepareStatement(sql);
				pstmt.executeUpdate();

				userName.setText(upNickname.getText() + "님");
				upNickname.setText("");

				upNickPane.setVisible(false);
				userPane.setVisible(true);

				AppUtil.inforAlert(null, "닉네임이 변경되었습니다.");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 취소
	public void backUpNick() {
		upNickPane.setVisible(false);
		userPane.setVisible(true);
	}

	// 닉넴 변경
	public void changeUpNick() {
		upNickPane.setVisible(true);
		userPane.setVisible(false);
	}

	@FXML
	Button upBookBtn;

	// 도서 수정
	public void upBookPopup() {
		if (login == 1) {

			int idx = bookList.getSelectionModel().getSelectedIndex();

			if (idx >= 0) {

				Stage mainStage = (Stage) upBookBtn.getScene().getWindow();

				pop = new Stage(StageStyle.DECORATED);
				pop.initModality(Modality.WINDOW_MODAL);
				pop.initOwner(mainStage);

				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/view/Upbook.fxml"));
					Parent root = (Parent) loader.load();
					Scene scene = new Scene(root);

					scene.getStylesheets().add(getClass().getResource("/view/Layout.css").toExternalForm());

					UpBookController pop = loader.getController();
					book bookName = new book();
					bookName.setbName(bookList.getSelectionModel().getSelectedItem().getbName());
					pop.initData(bookName.getbName(), loginId.getText(), loginpassword.getText());
					pop.loadData();

					Stage primaryStage = (Stage) inBookBtn.getScene().getWindow();
					primaryStage.setScene(scene);
					primaryStage.setTitle("Revise the book editing");
					primaryStage.setResizable(false);
					primaryStage.show();

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				AppUtil.errorAlert("수정할 아이템을 설정하세요.", "에러");
			}

		} else {
			AppUtil.warAlert("로그인 후 이용 가능합니다.", null);
			return;
		}
	}

	public static void inforpopData() {
		popup = false;
	}

	static boolean popup = false;

	public void bookInforPopup() {
		if (login == 1) {
			if (popup == false) {
				int idx = bookList.getSelectionModel().getSelectedIndex();

				if (idx >= 0) {
					Stage mainStage = (Stage) upBookBtn.getScene().getWindow();

					pop = new Stage(StageStyle.DECORATED);
					pop.initModality(Modality.WINDOW_MODAL);
					pop.initOwner(mainStage);

					try {
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("/view/ClickInfor.fxml"));
						Parent root = (Parent) loader.load();
						Scene scene = new Scene(root);

						BookInforController pop = loader.getController();
						book bookName = new book();
						bookName.setbName(bookList.getSelectionModel().getSelectedItem().getbName());
						pop.initData(bookName.getbName(), loginId.getText());
						pop.loadData();
						scene.getStylesheets().add(getClass().getResource("/view/Layout.css").toExternalForm());

						Stage stage = new Stage();
						stage.setScene(scene);
						stage.setTitle("Book information.");
						stage.initStyle(StageStyle.UNDECORATED);
						stage.setResizable(false);
						stage.show();
						popup = true;
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {
					AppUtil.errorAlert("볼 아이템을 설정하세요.", "에러");
				}

			} else {
				return;
			}
		} else {
			AppUtil.warAlert("로그인 후 이용 가능합니다.", null);
			return;
		}
	}

	public void init(String idData, String passwordData) {
		loginId.setText(idData);
		loginpassword.setText(passwordData);
		login();
	}
	
	public void genre() {
		Stage mainStage = (Stage) upBookBtn.getScene().getWindow();

		pop = new Stage(StageStyle.DECORATED);
		pop.initModality(Modality.WINDOW_MODAL);
		pop.initOwner(mainStage);

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/genreLayout.fxml"));
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/view/Layout.css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("The field of books");
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
